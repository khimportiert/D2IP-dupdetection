package app.main;

import app.io.CSVGenerator;
import app.io.CSVReader;
import app.io.FilterTesting;
import app.misc.StopWatch;
import app.model.Dup;
import app.model.ModelEntity;
import app.model.Notebook;
import app.model.StorageDevice;
import app.token.Token;
import app.token.TokenEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class Detection {
    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        CSVReader fr = new CSVReader(new File(currentDir + "/data/Z2.csv").getAbsolutePath());

        ArrayList<StorageDevice> notebooks = fr.read(StorageDevice.class);

        TreeSet<TokenEntity> tokenEntities = new TreeSet<>();
        ArrayList<Dup> duplicates = new ArrayList<>();
        int dupesCount = 0;

        StopWatch.start();

        for (int i = 0; i < notebooks.size(); i++) {
            TokenEntity tokenEntity = new TokenEntity(notebooks.get(i));

            // THROW OUT IF IT HAS TOO FEW TOKENS i.e. Notebook{id='30730', title='"Lenovo Laptop'}
            if (tokenEntity.getTokens().size() < 2) {
                continue;
            }

            if (tokenEntities.contains(tokenEntity)) {
                TokenEntity contained = tokenEntities.ceiling(tokenEntity);
                Dup dup = new Dup(contained.getId(), tokenEntity.getId());
                duplicates.add(dup);
                dupesCount++;
//                TokenEntity contained = tokenEntities.stream().filter(t -> t.equals(tokenEntity)).findFirst().get();
//                System.out.println(contained.getId() + "\tContaining:  " + contained);
//                System.out.println(tokenEntity.getId() + "\tTokenEntity: " + tokenEntity);
//                System.out.println();
            } else {
                tokenEntities.add(tokenEntity);
            }

            if (i % 10000 == 0) {
                System.out.println(i + " / " + notebooks.size() + " - " + dupesCount + " duplicates" );
            }
        }

        StopWatch.peek();



        CSVGenerator gen = new CSVGenerator(currentDir + "/data/my_zy1.csv");
        ArrayList<ModelEntity> modList = new ArrayList<>(duplicates);
        gen.generate(modList);

//        CSVReader dr = new CSVReader(currentDir + "/data/ZY1.csv");
//        ArrayList<Dup> trueDups =  dr.read(Dup.class);
//        double f1 = FilterTesting.evaluateF1(trueDups, duplicates);

        StopWatch.stop();

//        System.out.println(f1);
//        System.out.println(dupesCount);
    }
}
