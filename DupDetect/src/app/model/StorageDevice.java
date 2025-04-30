package app.model;

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
public class StorageDevice implements ModelEntity {
    private String id;
    private String name;
    private String price;
    private String brand;
    private String description;
    private String category;

    public StorageDevice() { }

    @Override
    public int getId() {
        return Integer.parseInt(id);
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
}
