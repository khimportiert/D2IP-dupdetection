package app.model;

import app.token.Token;
import app.token.Tokenizer;

import java.util.TreeSet;

/**
 * Represents a notebook entity that can be loaded from a CSV file.
 * <p>
 * This class must define at least all fields that appear in the CSV header.
 * These required fields must:
 * <ul>
 *     <li>Exactly match the CSV column names (case-sensitive)</li>
 *     <li>Be of type {@code String}</li>
 * </ul>
 * Additional fields beyond those in the CSV are allowed.
 */
public class StorageDevice implements ModelEntity, Comparable<ModelEntity> {
    private int id;
    private String name;
    private String price;
    private String brand;
    private String description;
    private String category;
    private TreeSet<Token> tokens = new TreeSet<>();

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public TreeSet<Token> getTokens() {
        return tokens;
    }

    public void tokenize() {
        tokens = Tokenizer.tokenizeStorageDevice(this);
    }

    public StorageDevice() { }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toCSV() {
        return id + "," + name + "," + price + "," + brand + "," + description + "," + category;
    }

    @Override
    public String[] getFields() {
        return new String[] {"id", "name", "price", "brand", "description", "category"};
    }

    @Override
    public int compareTo(ModelEntity b) {
        return Integer.compare(this.getId(), b.getId());
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String toString() {
        return "StorageDevice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category +
                '}';
    }
}
