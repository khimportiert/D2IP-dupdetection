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

        StopWatch.start();

        //LinkedList<Notebook> notebooks = fr.read(Notebook.class);
        LinkedList<Dup> dup = frSolution.read(Dup.class);

        FilterTesting.evaluateF1(dup, dup);

        SampleDupPrint.showSamples(50,150);

        StopWatch.stop();
    }
}