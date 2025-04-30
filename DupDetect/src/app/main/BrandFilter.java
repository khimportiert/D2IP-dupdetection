package app.main;

import app.io.CSVReader;
import app.model.Brand;
import app.model.Notebook;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ArrayList;

public class BrandFilter {
    public static void main(String[] args) throws IOException {
        String currentDir = System.getProperty("user.dir");
        CSVReader fr = new CSVReader(new File(currentDir + "/data/Z1.csv").getAbsolutePath());
        CSVReader bfr = new CSVReader(new File(currentDir + "/data/brands.txt").getAbsolutePath());

        ArrayList<Notebook> notebooks = fr.read(Notebook.class);
        ArrayList<Brand> brands = bfr.read(Brand.class);

        HashSet<String> brandsLists = new HashSet<>();

        int START = 500;
        int OFFSET = 20;

        while (true) {
            int i = START;
            int j = START + OFFSET;
            brands.forEach(b -> brandsLists.add(b.toCSV()));
            while (i < j) {
                int finalI = i;
                if (brandsLists.stream().noneMatch(b -> notebooks.get(finalI).getTitle().toLowerCase().contains(b.toLowerCase()))) {
                    System.out.println(notebooks.get(finalI).getTitle());
                    i++;
                } else {
                    i++;
                    j++;
                }
            }
            System.out.println(i-OFFSET + " - " + j);
            String input = System.console().readLine();

            Files.write(Paths.get(currentDir + "/data/brands.txt"), (input+" \n").getBytes(), StandardOpenOption.APPEND);

            brands = bfr.read(Brand.class);
        }
    }
}
