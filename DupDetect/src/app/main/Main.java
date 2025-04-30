package app.main;

import app.io.CSVReader;
import app.misc.StopWatch;
import app.model.Notebook;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        String currentDir = System.getProperty("user.dir");
        CSVReader fr = new CSVReader(new File(currentDir + "/data/Z1.csv").getAbsolutePath());

        StopWatch.start();

        LinkedList<Notebook> notebooks = fr.read(Notebook.class);
        System.out.println(notebooks.getFirst());

        StopWatch.stop();

    }
}