package app.model;

public class Brand implements ModelEntity {
    private String name;

    public Brand() { }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public String toCSV() {
        return name;
    }

    @Override
    public String[] getFields() {
        return new String[] {"name"};
    }

    @Override
    public int compareTo(ModelEntity b) {
        return 0;
    }
}
