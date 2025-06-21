package app.main;

import app.io.CSVGenerator;
import app.io.CSVReader;
import app.misc.StopWatch;
import app.model.Dup;
import app.model.StorageDevice;
import app.token.Token;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildPartition {
    static final int THREADS = 16;
    static final String CURRENT_DIR = System.getProperty("user.dir");
    static final String FILE_1 = CURRENT_DIR + "/data/Z2.csv";

    private static final byte[] LOOKUP_TABLE = new byte[256];

    static {
        for (int i = 0; i < 256; i++) {
            if (i >= 'a' && i <= 'z') {
                LOOKUP_TABLE[i] = (byte) i;
            } else if (i >= 'A' && i <= 'Z') {
                LOOKUP_TABLE[i] = (byte) (i + 0x20);
            } else if (i >= '0' && i <= '9') {
                LOOKUP_TABLE[i] = (byte) (i);
            } else if (i == '.' || i == '-' || i == '/') {
                LOOKUP_TABLE[i] = (byte) (i);
            } else {
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

            int i = brands.indexOf(brand);
            if (i == -1) i = brands.size();

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

//        partitioning(brands, sizes);

        StopWatch.start();
        @SuppressWarnings("unchecked")
        ArrayList<StorageDevice>[][]
                partitions = load(CURRENT_DIR+"/data/partitions.kyro", ArrayList[][].class);

        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        List<Future<List<Dup>>> futures = new ArrayList<>();

        ArrayList<Dup> duplicates = new ArrayList<>();

        for (int i = 0; i < brands.size(); i++) {
            for (int j = 0; j < sizes.size(); j++) {
                HashMap<String, Integer> WORD_COUNT = new HashMap<>();

                partitions[i][j].forEach(device -> {
                    device.setSanitizedName(sanitize(device.getName()));

                    // +++++ Sorting by Word Count ASC +++++
                    device.setTokens(new TreeSet<>(
                            Comparator.comparingInt(o -> WORD_COUNT.getOrDefault(o.getValue(), 0))
                    ));
                    // +++++ Sorting by Word Count ASC +++++

                    // +++++ Remove Partitioning Keys
                    Matcher m1 = Pattern.compile(device.getBrand().toLowerCase()).matcher(device.getSanitizedName());
                    device.setSanitizedName(m1.replaceAll("").trim());
                    Matcher m2 = Pattern.compile("(?i)(\\d+\\s?(GB|TB))").matcher(device.getSanitizedName());
                    device.setSanitizedName(m2.replaceAll("").trim());

                    // +++++ Set Tokens
                    boolean isPrinted = false;
                    for (String word : device.getSanitizedName().split(" ")) {
                        if (word.isEmpty())
                            continue;

//                        if (word.length() < 2 && !isPrinted) {
//                            System.out.println(device.getSanitizedName());
//                            isPrinted = true;
//                        }

                        Token token = new Token(word, Token.Type.WORD);
                        device.getTokens().add(token);
                    }

                    // +++++ TF-IDF +++++
                    device.getTokens().forEach(token -> {
                        int count = WORD_COUNT.getOrDefault(token.getValue(), 1);
                        WORD_COUNT.put(token.getValue(), count + 1);
                    });
                    // +++++ TF-IDF +++++
                });

                // +++++ Jaro-Winkler-Distance +++++
//                JaroWinklerDistance distance = new JaroWinklerDistance();
                LevenshteinDistance  distance = new LevenshteinDistance ();

                AtomicInteger progress = new AtomicInteger(0);
                int barWidth = 50;
                long totalSteps = (long) partitions[i][j].size() * partitions[i][j].size() / 2;

                System.out.println(brands.get(i) + " - " + sizes.get(j) + ": ");

                for (int n = 0; n < partitions[i][j].size(); n++) {
                    int finalN = n;
                    int finalI = i;
                    int finalJ = j;
                    Callable<List<Dup>> task = () -> {
                        List<Dup> localDups = new ArrayList<>();
                        for (int m = finalN + 1; m < partitions[finalI][finalJ].size(); m++) {

                            int current = progress.incrementAndGet();
                            if (current % 20_000 == 0) {
                                double percent = (current * 100.0) / totalSteps;
                                long barFilled = ((long) current * barWidth) / totalSteps;

                                String bar = "=".repeat((int) barFilled) + " ".repeat((int) (barWidth - barFilled));
                                System.out.printf("\r[%s] %5.2f%%", bar, percent);
                                System.out.flush();
                            }


                            String device1 = partitions[finalI][finalJ].get(finalN).getTokens().toString();
                            String device2 = partitions[finalI][finalJ].get(m).getTokens().toString();

//                            System.out.println(device1);
//                            System.out.println(device2);

//                            double d = distance.apply(device1, device2); // Jaro-Winkler
                            double d = distance.apply(device1, device2) / (double) Math.max(device1.length(), device2.length());

                            if (d < 0.2) {
                                Dup dup = new Dup(partitions[finalI][finalJ].get(finalN).getId(), partitions[finalI][finalJ].get(m).getId());
                                localDups.add(dup);
                            }
                        }
                        return localDups;
                    };

                    futures.add(executor.submit(task));

                }

//                ArrayList<ModelEntity> l = new ArrayList<>(partitions[i][j]);
//                l.forEach(ModelEntity::tokenize);
//                File folder = new File(CURRENT_DIR + "/data/tokens/" + brands.get(i));
//                folder.mkdirs();
//                Tokenizer.writeFileTest(l,  folder.getAbsolutePath() + "/" + sizes.get(j) + ".txt");

//                System.out.println(WORD_COUNT);

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

        CSVGenerator wr = new CSVGenerator(CURRENT_DIR+"/data/partitions_dup.csv");
        wr.generate(new ArrayList<>(duplicates));

    }
}
