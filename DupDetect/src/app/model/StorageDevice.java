package app.model;

import app.token.Token;
import app.token.Tokenizer;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
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
public class StorageDevice implements ModelEntity, Comparable<ModelEntity>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String price;
    private String brand;
    private String description;
    private String category;
    private TreeSet<Token> tokens = new TreeSet<>();
    private ArrayList<Token> tokensArrayList = new ArrayList<>();
    private String sanitizedName;

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

    public String getSanitizedName() {
        return sanitizedName;
    }

    public void setSanitizedName(String newName) {
        sanitizedName = newName;
    }

    public void setTokens(TreeSet<Token> newTokens) {
        tokens = newTokens;
    }

    public void setBrand(String newBrand) {
        brand = newBrand;
    }

    public TreeSet<Token> getTokens() {
        return tokens;
    }

    public ArrayList<Token> getTokensArrayList() {
        return tokensArrayList;
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
