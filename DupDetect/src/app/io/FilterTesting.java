package app.io;

import app.misc.StopWatch;
import app.model.Dup;
import app.model.Notebook;

import java.util.HashSet;
import java.util.*;

public class FilterTesting {
    public static ArrayList<Dup> findDups(List<Notebook> notebooks)
    {
        ArrayList<Dup> foundDups = new ArrayList<>();

        int n = notebooks.size();

        ArrayList<String> stringArr = new ArrayList<String>();
        for(int i = 0; i < n; i++)
        {
            stringArr.add(notebooks.get(i).getTitle().trim().toLowerCase());

        }

        for (int i = 0; i < n; i++)
        {
            for(int j = 0; j < i; j++)
            {
                String notebookI = stringArr.get(i);
                String notebookJ = stringArr.get(j);

                if (notebookI.equals(notebookJ))
                {
                    Dup newDup = new Dup(i,j);
                    foundDups.add(newDup);
                }
            }
            if(i%100 == 0) {
                System.out.println("i: " + i);
                StopWatch.peek();
            }
        }

        return foundDups;
    }

    public static double evaluateF1(TreeSet<Dup> trueDupes, TreeSet<Dup> declaredDupes)
    {
        // count[0] := true positives
        // count[1] := false positives
        // count[2] := false negatives
        double[] counts = new double[3];

        ArrayList<Dup> allDupesList = new ArrayList<>(trueDupes);
        allDupesList.addAll(declaredDupes);

        HashSet<Dup> allDupes = new HashSet<>(allDupesList);

        int i = 0;

        for (Dup d : allDupes) {
            boolean trueDupe = trueDupes.contains(d);
            boolean declaredDupe = declaredDupes.contains(d);

            if (declaredDupe) {
                if (trueDupe) {
                    counts[0]++;
                } else {
                    counts[1]++;
                }
            } else if (trueDupe) {
                counts[2]++;
            }

            if (i % 10000 == 0) {
                System.out.println(i + " / " + allDupes.size());
            }
            i++;
        }


        double precision =  counts[0] / ( counts[0] +  counts[1]); // true positive / (true positive + false positive)
        double recall =  counts[0] / ( counts[0] +  counts[2]); // true positive / (true positive + false negative)

        System.out.println("true positives: " + counts[0]);
        System.out.println("false positives: " + counts[1]);
        System.out.println("false negatives: " + counts[2]);

        if (counts[0] +  counts[1] == 0) {
            precision = 0;
        }

        if (counts[0] +  counts[2] == 0) {
            recall = 0;
        }

        double f1 = (2 * precision * recall)/(precision + recall);

        if (precision + recall == 0) {
            f1 = 0;
        }

        return f1;
    }

    public static double evaluateF1(List<Dup> trueDups, List<Dup> declaredDups)
    {
        double [] counts =new double[3];

        ArrayList<Dup> allDupsList = new ArrayList<>(trueDups);
        allDupsList.addAll(declaredDups);

        HashSet<Dup> allDups = new HashSet<>(allDupsList);

        for (Dup d : allDups) {
            boolean trueDupe = trueDups.contains(d);
            boolean declaredDupe = declaredDups.contains(d);

            if (declaredDupe) {
                if (trueDupe) {
                    counts[0]++;
                } else {
                    counts[1]++;
                }
            } else if (trueDupe) {
                counts[2]++;
            }
        }

        double precisioon =  counts[0] / ( counts[0] +  counts[1]);
        double recall =  counts[0] / ( counts[0] +  counts[2]);

        double f1 = (2 * precisioon * recall)/(precisioon + recall);

        return f1;
//        double [] counts =new double[3];
//        int i = 0;
//
//        ArrayList<Dup> allDupsList = new ArrayList<>();
//
//        allDupsList.addAll(trueDups);
//        allDupsList.addAll(declaredDups);
//
//        HashSet<Dup> allDups = new HashSet<>(allDupsList);
//
//        allDups.parallelStream().forEach(d -> {
//            boolean trueDupe = trueDups.contains(d);
//            boolean declaredDupe = declaredDups.contains(d);
//
//            synchronized (counts) {
//            if(declaredDupe) {
//                if(trueDupe) {
//                    counts[0]++;
//                }
//                else {
//                    counts[1]++;
//                }
//            } else if (trueDupe) {
//                counts[2]++;
//            }}
//        });
//
//        double precisioon =  counts[0] / ( counts[0] +  counts[1]);
//        double recall =  counts[0] / ( counts[0] +  counts[2]);
//
//        double f1 = (2 * precisioon * recall)/(precisioon + recall);
//
//        return f1;
    }
}
