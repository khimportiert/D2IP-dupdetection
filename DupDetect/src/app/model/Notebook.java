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
public class Notebook implements ModelEntity, Comparable<ModelEntity> {
    private String id;
    private String title;
    private TreeSet<Token> tokens = new TreeSet<>();

    public Notebook() {}

    public Notebook(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public TreeSet<Token> getTokens() {
        return tokens;
    }

    public void setTokens(TreeSet<Token> tokens) {
        this.tokens = tokens;
    }

    public int getId() {
        return Integer.parseInt(id);
    }

    public String getTitle() {
        return title;
    }

    public void tokenize() {
        tokens = Tokenizer.tokenizeStorageDevice(this);
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String toCSV() {
        return id + "," + title;
    }

    public String[] getFields() {
        return new String[] { "id", "title" };
    }

    @Override
    public int compareTo(ModelEntity other) {
        return Integer.compare(this.getId(), other.getId());
    }
}
