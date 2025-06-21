package app.main;

public class Main {
    static final String CURRENT_DIR = System.getProperty("user.dir");
    static final String FILE_1 = CURRENT_DIR + "/data/Z2.csv";
    static final String FILE_2 = CURRENT_DIR + "/data/ZY2.csv";
    static final String OUT_1 = CURRENT_DIR + "/dataSample/Z2.csv";
    static final String OUT_2 = CURRENT_DIR + "/dataSample/ZY2.csv";

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");





//        SampleGenerator gen = new SampleGenerator(FILE_1, FILE_2, OUT_1, OUT_2);
//        gen.generate(20_000, StorageDevice.class);
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