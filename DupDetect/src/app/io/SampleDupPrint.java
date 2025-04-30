package app.io;

import app.model.Dup;
import app.model.Notebook;

import java.io.File;
import java.util.ArrayList;

public class SampleDupPrint {
    //public static void main(String[] args) {}

    public static void showSamples(int amount, int startingIndex)
    {
        String currentDir = System.getProperty("user.dir");
        CSVReader fr = new CSVReader(new File(currentDir + "/data/Z1.csv").getAbsolutePath());
        CSVReader frSolution = new CSVReader(new File(currentDir + "/data/ZY1.csv").getAbsolutePath());

        ArrayList<Notebook> notebookList = fr.read(Notebook.class);
        ArrayList<Dup> dupList = frSolution.read(Dup.class);

        for(int i = 0; i < amount; i++)
        {
            Dup dup = dupList.get(startingIndex + i);
            //System.out.println();
            System.out.println(notebookList.get(dup.getLid()));
            System.out.println("equals");
            System.out.println(notebookList.get(dup.getRid()));
            System.out.println();
        }
    }


}
