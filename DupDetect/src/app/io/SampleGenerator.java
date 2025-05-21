package app.io;

import app.model.Dup;
import app.model.ModelEntity;
import app.model.Notebook;

import java.util.*;

public class SampleGenerator {
    private final String CURRENT_DIR = System.getProperty("user.dir");

    private String dataFileName = CURRENT_DIR + "/data/Z1.csv";
    private String dupFileName = CURRENT_DIR + "/data/ZY1.csv";
    private String outputDataFileName = CURRENT_DIR + "/dataSample/sample.csv";
    private String outputDupFileName = CURRENT_DIR + "/dataSample/sample_dup.csv";

    public SampleGenerator() {
    }

    public SampleGenerator(String dataFileName, String dupFileName) {
        this.dataFileName = dataFileName;
        this.dupFileName = dupFileName;
    }

    public SampleGenerator(String dataFileName, String dupFileName, String outputDataFileName, String outputDupFileName) {
        this(dataFileName, dupFileName);
        this.outputDataFileName = outputDataFileName;
        this.outputDupFileName = outputDupFileName;
    }

    public void generateNotebookSample(int sampleSize) {
        generate(sampleSize, Notebook.class);
    }

    public <T> void generate(int sampleSize, Class<T> entityType) {
        CSVReader readerData = new CSVReader(dataFileName);
        CSVReader readerDup = new CSVReader(dupFileName);

        Comparator<Dup> invertedComparator = (d1, d2) -> Integer.compare(d1.getRid(), d2.getId());

        ArrayList<T> entityList = readerData.read(entityType);
        Queue<T> entitiyQueue = new LinkedList<>(entityList);

        TreeSet<Dup> duplicates = new TreeSet<>(readerDup.read(Dup.class));
        TreeSet<Dup> duplicatesInv = new TreeSet<>(invertedComparator);
        duplicatesInv.addAll(readerDup.read(Dup.class));

        if (sampleSize <= 1 || sampleSize > entitiyQueue.size()) {
            System.err.println("Invalid sample size");
            System.exit(1);
        }

        ArrayList<T> entityOut = new ArrayList<>();
        for (int i = 0; i < sampleSize; i++) {
            entityOut.add(entitiyQueue.poll());
        }

        int i = 0;
        HashSet<ModelEntity> duplicateOut = new HashSet<>();
        for (T _item : entityOut) {
            if (_item instanceof ModelEntity item) {
                for (Dup dup : duplicates) {
                    if (dup.getLid() > item.getId())
                        break;
                    else {
                        if (dup.getLid() == item.getId() && dup.getRid() <= sampleSize)
                            duplicateOut.add(dup);
                    }
                }
                for (Dup dup : duplicatesInv) {
                    if (dup.getRid() > item.getId())
                        break;
                    else {
                        if (dup.getRid() == item.getId() && dup.getLid() <= sampleSize)
                            duplicateOut.add(dup);
                    }
                }

                i++;
                if (i % 5000 == 0) {
                    System.out.println(i + " / " + sampleSize);
                }
            }
        }

        ArrayList<ModelEntity> duplicateOutList = new ArrayList<>(duplicateOut);

        CSVGenerator dataGen = new CSVGenerator(outputDataFileName);
        CSVGenerator dupGen = new CSVGenerator(outputDupFileName);

        dataGen.generate((List<ModelEntity>) entityOut);
        dupGen.generate(duplicateOutList);
    }

}
