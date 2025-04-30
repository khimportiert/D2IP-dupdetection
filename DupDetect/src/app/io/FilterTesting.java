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
        double tp = 0; // in both
        double fp = 0; // in declaredDups not in trueDups
        double fn = 0; // in trueDups not in declaredDups
        int i = 0;

        LinkedList<Dup> allDupsList = new LinkedList<>();

        allDupsList.addAll(trueDups);
        allDupsList.addAll(declaredDups);

        HashSet<Dup> allDups = new HashSet<>(allDupsList);

        for(Dup d : allDups)
        {
            boolean trueDupe = trueDups.contains(d);
            boolean declaredDupe = declaredDups.contains(d);

            if(declaredDupe)
            {
                if(trueDupe)
                {
                    tp++;
                }
                else
                {
                    fp++;
                }
            } else if (trueDupe) {
                fn++;
            }
        }

        double precision = tp / (tp + fp);
        double recall = tp / (tp + fn);

        double f1 = (2 * precision * recall)/(precision + recall);

        return f1;
    }
}
