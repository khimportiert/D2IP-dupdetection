package app.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class DictionaryTree {
    public static TreeSet<String> tree = new TreeSet<>();

    static {
        ArrayList<String> words = new ArrayList<>();
        String CURRENT_DIR = System.getProperty("user.dir");
        try (BufferedReader br = new BufferedReader(new FileReader(CURRENT_DIR + "/data/dictionary.txt"))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tree = new TreeSet<>(words);
    }
}
