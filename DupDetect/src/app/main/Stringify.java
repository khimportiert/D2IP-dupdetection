package app.main;

import app.io.CSVReader;
import app.misc.StopWatch;
import app.model.Notebook;
import app.model.StorageDevice;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

public class Stringify {
    public static void main(String[] args) {

        String currentDir = System.getProperty("user.dir");
        CSVReader fr = new CSVReader(new File(currentDir + "/data/Z2.csv").getAbsolutePath());

        StopWatch.start();

        ArrayList<StorageDevice> org = fr.read(StorageDevice.class);

        ArrayList<String> deviceStrings = new ArrayList<>();

        for (StorageDevice device : org) {
            deviceStrings.add(device.toString().toLowerCase()
                    .trim());
        }

        TreeSet<String> notebooks = new TreeSet<>();
        ArrayList<String> duplicates = new ArrayList<>();

        for (String str : deviceStrings) {
            if (notebooks.contains(str)) {
                duplicates.add(str);
            } else {
                notebooks.add(str);
            }
        }

        System.out.println(duplicates.size() + " duplicates found");

        StopWatch.stop();
    }
}
