package app.main;

import app.io.CSVGenerator;
import app.io.CSVReader;
import app.misc.StopWatch;
import app.model.Dup;
import app.model.StorageDevice;
import app.token.Token;
import app.token.Tokenizer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildPartition {
    static final int THREADS = 16;
    static final String CURRENT_DIR = System.getProperty("user.dir");
    static final String FILE_1 = CURRENT_DIR + "/dataSample/Z2.csv";

    private static final byte[] LOOKUP_TABLE = new byte[256];

    static {
        for (int i = 0; i < 256; i++) {
            if (i >= 'a' && i <= 'z') {
                LOOKUP_TABLE[i] = (byte) i;
            } else if (i >= 'A' && i <= 'Z') {
                LOOKUP_TABLE[i] = (byte) (i + 0x20);
            } else if (i >= '0' && i <= '9') {
                LOOKUP_TABLE[i] = (byte) (i);
            }
            else if (i == '.' || i == '-' || i == '/') {
                LOOKUP_TABLE[i] = (byte) (i);
            }
            else {
                LOOKUP_TABLE[i] = ' ';
            }
        }
    }

    private static String sanitize(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            sb.append((char) LOOKUP_TABLE[c & 0xFF]);
        }
        return sb.toString();
    }

    private static Kryo createKryoInstance() {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.register(ArrayList.class);
        kryo.register(StorageDevice.class);
        kryo.register(ArrayList[][].class);
        return kryo;
    }

    public static void save(Object obj, String filename) throws IOException {
        Kryo kryo = createKryoInstance();
        try (Output output = new Output(new BufferedOutputStream(new FileOutputStream(filename)))) {
            kryo.writeObject(output, obj);
        }
    }

    public static <T> T load(String filename, Class<T> type) throws IOException {
        Kryo kryo = createKryoInstance();
        try (Input input = new Input(new BufferedInputStream(new FileInputStream(filename)))) {
            return kryo.readObject(input, type);
        }
    }

    private static void partitioning(ArrayList<String> brands, ArrayList<String> sizes) throws IOException {
        CSVReader fr = new CSVReader(new File(FILE_1).getAbsolutePath());
        ArrayList<StorageDevice> storageDevices = fr.read(StorageDevice.class);

        @SuppressWarnings("unchecked")
        ArrayList<StorageDevice>[][] partitions = new ArrayList[brands.size()+1][sizes.size()+1];
        for (int i = 0; i < brands.size()+1; i++) {
            for (int j = 0; j < sizes.size()+1; j++) {
                partitions[i][j] = new ArrayList<>();
            }
        }

        for (StorageDevice sd : storageDevices) {
            String brand = sd.getBrand().toLowerCase();
            String name = sd.getName();

            Matcher matcher = Pattern.compile(String.join("|", brands), Pattern.CASE_INSENSITIVE).matcher(name);
            if (matcher.find()) {
                brand = matcher.group();
            }

            int i = brands.indexOf(brand.toLowerCase());
            if (i == -1) {
                i = brands.size();
            } else {
                sd.setBrand(brand); // TODO Hier wird das Dataset geändert!
            }

            Matcher m = Pattern.compile("(?i)(\\d+\\s?(GB|TB))").matcher(name);
            int j = sizes.size();

            if (m.find()) {
                // TODO Allow multiple sizes? 32GB/128GB (1214075: SanDisk SD SDHC (32GB) - (KSSD10V2/128GB) GB Ultra 32)
                String size = m.group(1).replaceAll("\\s+", "");
                int foundIndex = sizes.indexOf(size);
                if (foundIndex != -1) j = foundIndex;
            }

            partitions[i][j].add(sd);
        }

        save(partitions, CURRENT_DIR+"/data/partitions.kyro");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        ArrayList<String> brands = new ArrayList<>(List.of(new String[]{"sandisk", "sony", "kingston", "lexar", "intenso", "toshiba", "samsung", "pny", "transcend"}));
        ArrayList<String> sizes = new ArrayList<>(List.of(new String[]{"4GB", "8GB", "10GB", "16GB", "32GB", "64GB", "128GB", "256GB", "512GB"}));

        partitioning(brands, sizes);

        StopWatch.start();
        @SuppressWarnings("unchecked")
        ArrayList<StorageDevice>[][]
                partitions = load(CURRENT_DIR+"/data/partitions.kyro", ArrayList[][].class);

        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        ArrayList<Dup> duplicates = new ArrayList<>();

        for (int i = 0; i < brands.size(); i++) {
            for (int j = 0; j < sizes.size(); j++) {

                List<Future<List<Dup>>> futures = new ArrayList<>();

                HashMap<String, Integer> WORD_COUNT = new HashMap<>();

                partitions[i][j].forEach(device -> {
                    device.setSanitizedName(sanitize(device.getName()));

                    // +++++ Sorting by Word Count ASC +++++
//                    device.setTokens(new TreeSet<>(
//                            Comparator.comparingInt(o -> WORD_COUNT.getOrDefault(o.getValue(), 0))
//                    ));
                    // +++++ Sorting by Word Count ASC +++++

                    // +++++ Remove Partitioning Keys
                    Matcher m1 = Pattern.compile(device.getBrand().toLowerCase()).matcher(device.getSanitizedName());
                    device.setSanitizedName(m1.replaceAll("").trim());
                    Matcher m2 = Pattern.compile("(?i)(\\d+\\s?(GB|TB))").matcher(device.getSanitizedName());
                    device.setSanitizedName(m2.replaceAll("").trim());

                    // +++++ Set Tokens
                    for (String word : device.getSanitizedName().split(" ")) {
                        if (word.isEmpty())
                            continue;
                        if (word.length() < 2)
                            continue;

                        Token token = new Token(word, Token.Type.WORD);
                        if (!device.getTokensArrayList().contains(token)) {
                            device.getTokensArrayList().add(token);
                        }
                    }

                    // +++++ TF-IDF +++++
                    device.getTokensArrayList().forEach(token -> {
                        int count = WORD_COUNT.getOrDefault(token.getValue(), 0);
                        WORD_COUNT.put(token.getValue(), count + 1);
                    });
                    // +++++ TF-IDF +++++
                });

                partitions[i][j].forEach(device -> {
                    device.getTokensArrayList().sort(
                            Comparator.comparingInt(o -> WORD_COUNT.getOrDefault(o.getValue(), 0))
                    );
                });

                JaroWinklerDistance distance = new JaroWinklerDistance();
                LevenshteinDistance levenstein = new LevenshteinDistance();
                final List<StorageDevice> part = partitions[i][j];
                final int n = part.size();
                AtomicInteger progress = new AtomicInteger(0);
                int barWidth = 50;
                long totalSteps = (long)n * (n - 1) / 2;

                System.out.println(brands.get(i) + " - " + sizes.get(j) + ": ");

                for (int tid = 0; tid < THREADS; tid++) {
                    final int threadId = tid;
                    futures.add(executor.submit(() -> {
                        List<Dup> localDups = new ArrayList<>();
                        for (int a = threadId; a < n; a += THREADS) {

                            String device1 = "";
                            for (Token t : part.get(a).getTokensArrayList()) {
                                device1 += t.getValue() + " ";
                            }

                            for (int b = a + 1; b < n; b++) {
                                int current = progress.incrementAndGet();
                                if (current % 20_000 == 0) {
                                    double percent = (current * 100.0) / totalSteps;
                                    long barFilled = ((long) current * barWidth) / totalSteps;
                                    String bar = "=".repeat((int) barFilled) + " ".repeat((int) (barWidth - barFilled));
//                                    System.out.printf("\r[%s] %5.2f%%", bar, percent);
//                                    System.out.flush();
                                }

                                String device2 = "";
                                for (Token t : part.get(b).getTokensArrayList()) {
                                    device2 += t.getValue() + " ";
                                }

                                HashSet<Token> set1 = new HashSet<>(part.get(a).getTokensArrayList());
                                HashSet<Token> set2 = new HashSet<>(part.get(b).getTokensArrayList());

//                                ArrayList<Byte> bytes1 = new ArrayList<>();
//                                for (Token t : set1) {
//                                    for (byte c : t.getValue().getBytes()) {
//                                        bytes1.add(c);
//                                    }
//                                }
//                                bytes1.sort(Comparator.comparing(Byte::byteValue));
//                                byte[] byteArr1 = new byte[bytes1.size()];
//                                for (int ii = 0; ii < bytes1.size(); ii++) {
//                                    byteArr1[ii] = bytes1.get(ii);
//                                }
//                                String device1SortedAlpha = new String(byteArr1);
//
//                                ArrayList<Byte> bytes2 = new ArrayList<>();
//                                for (Token t : set2) {
//                                    for (byte c : t.getValue().getBytes()) {
//                                        bytes2.add(c);
//                                    }
//                                }
//                                bytes2.sort(Comparator.comparing(Byte::byteValue));
//                                byte[] byteArr2 = new byte[bytes2.size()];
//                                for (int ii = 0; ii < bytes2.size(); ii++) {
//                                    byteArr2[ii] = bytes2.get(ii);
//                                }
//                                String device2SortedAlpha = new String(byteArr2);
//
//                                if (levenstein.apply(device1SortedAlpha, device2SortedAlpha) <= 2) {
//                                    localDups.add(new Dup(part.get(a).getId(), part.get(b).getId()));
//                                }


                                HashSet<Token> intersectionTokens = new HashSet<>(set1);
                                HashSet<Token> combinedTokens =  new HashSet<>();
                                combinedTokens.addAll(set1);
                                combinedTokens.addAll(set2);
                                intersectionTokens.retainAll(set2);
                                int dupCount = set1.size() + set2.size() - combinedTokens.size();
                                double score = set1.size() + set2.size() - dupCount != 0 ? (double) dupCount / (set1.size() + set2.size() - dupCount) : 0;

                                double weighted_score = 0;
                                for (Token t : intersectionTokens) {
                                    int count = WORD_COUNT.get(t.getValue());
                                    weighted_score += 1 / Math.log(count);
                                }
                                weighted_score /= intersectionTokens.size();

                                if (weighted_score > 0.25) {
                                    localDups.add(new Dup(part.get(a).getId(), part.get(b).getId()));
                                }

                                if (score >= 0.75) { // TODO lange Wörter haben mehr gewicht
//                                    localDups.add(new Dup(part.get(a).getId(), part.get(b).getId()));

//                                    StringBuilder sb1 = new StringBuilder();
//                                    part.get(a).getTokensArrayList().forEach(t -> {
//                                        sb1.append(t.getValue() + " (" + WORD_COUNT.get(t.getValue()) + ")");
//                                        sb1.append(", ");
//                                    });
//                                    sb1.append("\n");
//                                    System.out.println(sb1);
//                                    StringBuilder sb2 = new StringBuilder();
//                                    part.get(b).getTokensArrayList().forEach(t -> {
//                                        sb2.append(t.getValue() + " (" + WORD_COUNT.get(t.getValue()) + ")");
//                                        sb2.append(", ");
//                                    });
//                                    sb2.append("\n");
//                                    System.out.println(sb2);
//                                    System.out.println();
                                }

//                                if (device1.equals(device2)) {
//                                    Dup dup = new Dup(part.get(a).getId(), part.get(b).getId());
//                                    localDups.add(dup);
//                                }

//                                double d = distance.apply(device1, device2); // Jaro-Winkler
//                                if (d < 0.13) {
//                                    localDups.add(new Dup(part.get(a).getId(), part.get(b).getId()));
//                                }
                            }
                        }
                        return localDups;
                    }));

                }

                for (Future<List<Dup>> future : futures) {
                    try {
                        duplicates.addAll(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println();
                System.out.println("DupCount: " + duplicates.size());

            }

            StopWatch.peek();
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        CSVGenerator wr = new CSVGenerator(CURRENT_DIR+"/data/partitions_dup_multi.csv");
        wr.generate(new ArrayList<>(duplicates));

    }
}
