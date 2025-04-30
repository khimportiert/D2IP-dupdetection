package app.main;

import app.io.CSVReader;
import app.io.FilterTesting;
import app.io.SampleDupPrint;
import app.misc.StopWatch;
import app.model.Dup;
import app.model.Notebook;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        String currentDir = System.getProperty("user.dir");
        CSVReader fr = new CSVReader(new File(currentDir + "/data/Z1.csv").getAbsolutePath());
        CSVReader frSolution = new CSVReader(new File(currentDir + "/data/ZY1.csv").getAbsolutePath());
        CSVReader frSolution2 = new CSVReader(new File(currentDir + "/data/ZY2.csv").getAbsolutePath());

        StopWatch.start();

        //LinkedList<Notebook> notebooks = fr.read(Notebook.class);
        LinkedList<Dup> dups1 = frSolution.read(Dup.class);
        LinkedList<Dup> dups2 = frSolution.read(Dup.class);
        System.out.println("f1 score: " + FilterTesting.evaluateF1(dups2, dups2));



        StopWatch.stop();
    }
}