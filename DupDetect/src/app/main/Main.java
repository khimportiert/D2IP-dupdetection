package app.main;

import app.io.CSVReader;
import app.io.SampleDupPrint;
import app.io.SampleGenerator;
import app.model.Dup;
import app.model.StorageDevice;

import java.io.File;
import java.util.ArrayList;

public class Main {
    static final String CURRENT_DIR = System.getProperty("user.dir");
    static final String FILE_1 = CURRENT_DIR + "/data/Z2.csv";
    static final String FILE_2 = CURRENT_DIR + "/data/ZY2.csv";
    static final String OUT_1 = CURRENT_DIR + "/dataSample/Z2.csv";
    static final String OUT_2 = CURRENT_DIR + "/dataSample/ZY2.csv";

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");


        CSVReader fr = new CSVReader(new File(OUT_1).getAbsolutePath());
        CSVReader dr = new CSVReader(CURRENT_DIR + "/data/partitions_dup.csv");

        ArrayList<StorageDevice> storageDevices = fr.read(StorageDevice.class);
        ArrayList<Dup> duplicates = dr.read(Dup.class);

//        for (Dup dup : duplicates) {
//            int lid = dup.getLid();
//            int rid = dup.getRid();
//
//            System.out.println(storageDevices.get(lid).getId() + "," + storageDevices.get(rid).getId());
//            System.out.println(storageDevices.get(lid).getName());
//            System.out.println(storageDevices.get(rid).getName());
//            System.out.println();
//        }

        SampleDupPrint.showSamples(50, 5000, FILE_1, FILE_2, StorageDevice.class);


//        SampleGenerator gen = new SampleGenerator(FILE_1, FILE_2, OUT_1, OUT_2);
//        gen.generate(100_000, StorageDevice.class);
//
//        CSVReader or1 = new CSVReader(new File(OUT_1).getAbsolutePath());
//        CSVReader or2 = new CSVReader(new File(OUT_2).getAbsolutePath());
//        ArrayList<ModelEntity> bks = new ArrayList<>();
//        ArrayList<ModelEntity> devices = new ArrayList<>(or1.read(StorageDevice.class));
//        ArrayList<Dup> dupes = new ArrayList<>(or2.read(Dup.class));
//
//        for(Dup dup : dupes) {
//            bks.add(devices.get(dup.getLid()));
//            bks.add(devices.get(dup.getRid()));
//        }
//
//        bks.forEach(ModelEntity::tokenize);
//        Tokenizer.writeFileTest(bks);
    }


}