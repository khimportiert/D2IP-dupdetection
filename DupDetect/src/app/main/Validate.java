package app.main;

import app.io.CSVReader;
import app.io.FilterTesting;
import app.model.Dup;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

public class Validate {
    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        CSVReader fr = new CSVReader(new File(currentDir + "/dataSample/ZY2.csv").getAbsolutePath());
        CSVReader dr = new CSVReader(currentDir + "/data/partitions_dup_multi.csv");
        ArrayList<Dup> trueDups =  fr.read(Dup.class);
        ArrayList<Dup> myDups =  dr.read(Dup.class);
        System.out.println(myDups.size());
        System.out.println(new TreeSet<>(myDups).size());
        double f1 = FilterTesting.evaluateF1(new TreeSet<>(trueDups), new TreeSet<>(myDups));
        System.out.println(f1 + " score");
    }
}
