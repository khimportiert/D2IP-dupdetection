package app.io;

import app.model.Dup;
import app.model.Notebook;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class FilterTesting {
    public static Dup findDups(LinkedList<Notebook> notebooks)
    {
        return null;
    }

    public static double evaluateF1(LinkedList<Dup> trueDups, LinkedList<Dup> declaredDups)
    {
        double tp = 0; // in both
        double fp = 0; // in declaredDups not in trueDups
        double fn = 0; // in trueDups not in declaredDups

        LinkedList<Dup> wrongDups = new LinkedList<>();
        for(Dup d : trueDups)
        {
            Dup newDup = new Dup();
            newDup.setRid(d.getLid());
            newDup.setLid(d.getRid());
            wrongDups.add(newDup);
        }
        System.out.println("wrongDups old: " + wrongDups.size());

        LinkedList<Dup> safe = trueDups;
        trueDups.addAll(wrongDups);

        System.out.println("trueDups original length: " + trueDups.size());
        HashSet<Dup> allDups = new HashSet<>(trueDups);

        System.out.println("trueDups length: " + trueDups.size());
        System.out.println("allDups length: " + allDups.size());

        double precision = tp / (tp + fp);
        double recall = tp / (tp + fn);

        double f1 = (2 * precision * recall)/(precision + recall);

        return f1;
    }
}
