package app.io;

import app.model.ModelEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVGenerator {
    private final String filename;

    public CSVGenerator(String filename) {
        this.filename = filename;
    }

    public void generate(List<ModelEntity> items) {
        if (items.isEmpty()) {
            System.err.println("CSVGenerator: Empty List. Nothing to generate");
            return;
        }

        try (FileWriter writer = new FileWriter(filename)) {
            StringBuilder str = new StringBuilder();

            for (String field : items.getFirst().getFields()) {
                str.append(field).append(",");
            }
            str.deleteCharAt(str.length() - 1);
            writer.append(str.toString()).append("\n");

            for (ModelEntity item : items) {
                writer.append(item.toCSV()).append("\n");
            }
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
