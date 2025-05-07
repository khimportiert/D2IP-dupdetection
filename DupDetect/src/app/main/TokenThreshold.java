package app.main;

import app.io.CSVReader;
import app.misc.StopWatch;
import app.model.Dup;
import app.model.Notebook;
import app.token.Token;
import app.token.Tokenizer;

import java.io.File;
import java.util.*;

public class TokenThreshold {
    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        CSVReader fr = new CSVReader(new File(currentDir + "/data/Z1.csv").getAbsolutePath());
        CSVReader dr = new CSVReader(new File(currentDir + "/data/ZY1.csv").getAbsolutePath());

        StopWatch.start();

        ArrayList<Notebook> notebooks = fr.read(Notebook.class);
        ArrayList<Dup> dupes = dr.read(Dup.class);

        ArrayList<Notebook> bks = new ArrayList<>();

        for(Dup dup : dupes) {
            bks.add(notebooks.get(dup.getLid()));
            bks.add(notebooks.get(dup.getRid()));
        }

        HashMap<Token.Type, ArrayList<Double>> scores = new HashMap<>();

        for (Token.Type type : Token.Type.values()) {
            scores.put(type, new ArrayList<>());
        }

        ArrayList<Notebook> notes = bks;

        notes.forEach(Notebook::tokenize);

        for (int i = 1; i < notes.size(); i += 2) {
            HashMap<Token.Type, HashSet<Token>> tokenMap1 = Tokenizer.getGroupedTokens(notes.get(i-1));
            HashMap<Token.Type, HashSet<Token>> tokenMap2 = Tokenizer.getGroupedTokens(notes.get(i));

            tokenMap1.forEach((key1, set1) -> {
                HashSet<Token> set2 = tokenMap2.get(key1);
                int size1 = set1.size();
                int size2 = set2.size();

                HashSet<Token> combinedTokens =  new HashSet<>();
                combinedTokens.addAll(set1);
                combinedTokens.addAll(set2);

                int dupCount = size1 + size2 - combinedTokens.size();
                double score = size1 + size2 - dupCount != 0 ? (double) dupCount / (size1 + size2 - dupCount) : 0;

                scores.get(key1).add(score);
            });
        }

        for (Token.Type type : Token.Type.values()) {
            Collections.sort(scores.get(type));

            double average = scores.get(type).stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

            int size = scores.get(type).size();
            double median;
            if (size == 0) {
                median = 0.0;
            } else if (size % 2 == 1) {
                median = scores.get(type).get(size / 2);
            } else {
                median = (scores.get(type).get(size / 2 - 1) + scores.get(type).get(size / 2)) / 2.0;
            }

            double lowerThird;
            if (size == 0) {
                lowerThird = 0.0;
            } else {
                int index = (int) Math.floor(size * (1.0 / 3));
                if (index >= size) index = size - 1;
                lowerThird = scores.get(type).get(index);
            }

            System.out.printf("%s:\t avg=%.3f med=%.3f l33=%.3f\n", type, average, median, lowerThird);
        }

        long a = scores.get(Token.Type.KEYWORD).stream().filter(s -> s >= 1.0).count();
        long b = scores.get(Token.Type.OTHER).stream().filter(s -> s >= 1.0).count();

        System.out.println(scores.get(Token.Type.OTHER).size());
        System.out.println(a);
        System.out.println(b);

        StopWatch.stop();
    }
}
