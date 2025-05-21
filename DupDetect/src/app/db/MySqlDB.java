package app.db;

import app.model.StorageDevice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MySqlDB {
    private static final String URL = "jdbc:mysql://localhost:3306/dup_detect?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void insertStorageDevices(List<StorageDevice> storageDevices) {
        String sql = "INSERT INTO storage_device (id, name, price, brand, description, category) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (StorageDevice s : storageDevices) {
                pstmt.setInt(1, s.getId());
                pstmt.setString(2, s.getName());
                pstmt.setString(3, s.getPrice());
                pstmt.setString(4, s.getBrand());
                pstmt.setString(5, s.getDescription());
                pstmt.setString(6, s.getCategory());

                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
