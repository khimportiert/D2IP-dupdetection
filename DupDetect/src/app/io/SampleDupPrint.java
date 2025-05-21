package app.io;

import app.model.Dup;
import app.model.ModelEntity;
import app.model.Notebook;

import java.io.File;
import java.util.ArrayList;

public class SampleDupPrint {
    //public static void main(String[] args) {}

    public static void showSamples(int amount, int startingIndex) {
        String currentDir = System.getProperty("user.dir");
        showSamples(amount, startingIndex, currentDir + "/data/Z1.csv", currentDir + "/data/ZY1.csv", Notebook.class);
    }

    public static <T> void showSamples(int amount, int startingIndex, String file_data, String file_solution, Class<T> entity_class)
    {
        CSVReader fr = new CSVReader(new File(file_data).getAbsolutePath());
        CSVReader frSolution = new CSVReader(new File(file_solution).getAbsolutePath());

        ArrayList<T> notebookList = fr.read(entity_class);
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
