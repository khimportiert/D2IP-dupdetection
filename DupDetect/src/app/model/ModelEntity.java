package app.model;

public interface ModelEntity {
    int getId();
    String toCSV();
    String[] getFields();
    int compareTo(ModelEntity b);
}
