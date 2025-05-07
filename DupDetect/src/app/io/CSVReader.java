package app.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class CSVReader {
    private String fileName;
    private final String delimiter = ",";

    /**
     * Provides functionality to read a .csv file
     */
    public CSVReader() { }

    /**
     * Provides functionality to read a .csv file
     * @param fileName Path to the .csv file
     */
    public CSVReader(String fileName) {
        this.fileName = fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Reads a .csv file and converts each row into an instance of the specified entity type.
     * <p>
     * The entity class must have fields that match the column names of the CSV file.
     *
     * @param <T>        The type of the entity.
     * @param entityType The class object of the entity type. Its attributes must match the CSV columns.
     * @return           A {@code ArrayList} containing objects of the specified entity type.
     */
    public <T> ArrayList<T> read(Class<T> entityType) {
        ArrayList<T> items = new ArrayList<>();

        if (fileName == null) {
            System.err.println("No file specified");
            System.exit(1);
        }

        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String[] headers = null;

            if ((line = br.readLine()) != null) {
                headers = line.split(delimiter, -1);
            }

            if (headers == null) {
                System.err.println("Error reading .csv headers. Is the file empty?");
                System.exit(1);
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter, headers.length);
                try {
                    T entity = createEntity(entityType, headers, values);
                    items.add(entity);
                } catch (ArrayIndexOutOfBoundsException e) {
                    // TODO Handle this
//                    System.err.println("Error parsing entity: " + e);
//                    System.err.println("At: " + line);
                } catch (NumberFormatException e) {
                    // TODO
                }
            }
        }

        catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

        catch (Exception e) {
            System.err.println("Error parsing entity: " + e);
            System.err.println("Do the fields match the .csv header?");
            e.printStackTrace();
            System.exit(1);
        }

        return items;
    }

    private <T> T createEntity(Class<T> entityType, String[] headers, String[] values) throws NumberFormatException, Exception {
        T instance = entityType.getDeclaredConstructor().newInstance();

        for (int i = 0; i < headers.length; i++) {
            String fieldName = headers[i].trim();
            String value = values[i].trim();

            Field field = entityType.getDeclaredField(fieldName);
            field.setAccessible(true);

//            System.out.println(field.getType());

            if (field.getType() == int.class) {
                try {
                    field.set(instance, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException();
                }
            } else {
                field.set(instance, value);
            }
        }

        return instance;
    }

}
