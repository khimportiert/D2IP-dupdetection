package app.main;

import app.io.CSVReader;
import app.misc.StopWatch;
import app.model.Notebook;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        String currentDir = System.getProperty("user.dir");
        CSVReader fr = new CSVReader(currentDir + "./data/Z1.csv");

        StopWatch.start();

        List<Notebook> notebooks = fr.read(Notebook.class);
        System.out.println(notebooks.getFirst());

        StopWatch.stop();

    }
}