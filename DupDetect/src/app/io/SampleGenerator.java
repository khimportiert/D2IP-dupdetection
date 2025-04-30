package app.io;

public class SampleGenerator {
    private final String CURRENT_DIR = System.getProperty("user.dir");

    private final int sampleSize;
    private String dataFileName = CURRENT_DIR + "/data/Z1.csv";
    private String dupFileName = CURRENT_DIR + "/data/ZY1.csv";
    private String outputDataFileName = CURRENT_DIR + "/dataSample/sample.csv";
    private String outputDupFileName = CURRENT_DIR + "/dataSample/sample.csv";

    public SampleGenerator(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public SampleGenerator(int sampleSize, String dataFileName, String dupFileName) {
        this(sampleSize);
        this.dataFileName = dataFileName;
        this.dupFileName = dupFileName;
    }

    public SampleGenerator(int sampleSize, String dataFileName, String dupFileName, String outputDataFileName, String outputDupFileName) {
        this(sampleSize, dataFileName, dupFileName);
        this.outputDataFileName = outputDataFileName;
        this.outputDupFileName = outputDupFileName;
    }

    // load data, load dup
    // sort dup by lid, data already sorted
    //e

}
