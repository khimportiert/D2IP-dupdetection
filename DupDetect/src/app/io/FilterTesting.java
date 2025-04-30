package app.io;

import app.model.Dup;
import app.model.Notebook;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class FilterTesting {
    public static Dup findDups(LinkedList<Notebook> notebooks)
    {
        int n = notebooks.size();
        for (int i = 0; i < n; i++)
        {
            for(int j = 0; j < i; j++)
            {
                String notebookI = notebooks.get(i).getTitle();
                String notebookJ = notebooks.get(j).getTitle();


            }
        }

        return null;
    }

    public static double evaluateF1(LinkedList<Dup> trueDups, LinkedList<Dup> declaredDups)
    {
        double [] counts =new double[3];
        int i = 0;

        LinkedList<Dup> allDupsList = new LinkedList<>();

        allDupsList.addAll(trueDups);
        allDupsList.addAll(declaredDups);

        HashSet<Dup> allDups = new HashSet<>(allDupsList);

        allDups.parallelStream().forEach(d -> {
            boolean trueDupe = trueDups.contains(d);
            boolean declaredDupe = declaredDups.contains(d);

            synchronized (counts) {
            if(declaredDupe)
            {
                if(trueDupe)
                {
                    counts[0]++;
                }
                else
                {
                    counts[1]++;
                }
            } else if (trueDupe) {
                counts[2]++;
            }}
        });

        double precisioon =  counts[0] / ( counts[0] +  counts[1]);
        double recall =  counts[0] / ( counts[0] +  counts[2]);

        double f1 = (2 * precisioon * recall)/(precisioon + recall);

        return f1;
    }
}
