package org.project.autoserviceapp.admin.DB;

import org.project.autoserviceapp.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private int storage_id;
    private String storage_type;
    private int storage_sum;
    private int storage_numOfReserved;

    //Конструкторы
    public Storage() {}

    public Storage(int storage_id, String storage_type, int storage_sum, int storage_numOfReserved) {
        this.storage_id = storage_id;
        this.storage_type = storage_type;
        this.storage_sum = storage_sum;
        this.storage_numOfReserved = storage_numOfReserved;
    }

    //Геттеры и сеттеры
    public int getStorage_id() { return storage_id; }
    public void setStorage_id(int storage_id) { this.storage_id = storage_id; }

    public String getStorage_type() { return storage_type; }
    public void setStorage_type(String storage_type) { this.storage_type = storage_type; }

    public int getStorage_sum() { return storage_sum; }
    public void setStorage_sum(int storage_sum) { this.storage_sum = storage_sum; }

    public int getStorage_numOfReserved() { return storage_numOfReserved; }
    public void setStorage_numOfReserved(int storage_numOfReserved) { this.storage_numOfReserved = storage_numOfReserved; }

    //Добавить все данные
    public static List<Storage> getAll() {
        List<Storage> parts = new ArrayList<>();
        String sql = "SELECT * FROM Storage";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                parts.add(new Storage(
                        rs.getInt("storage_id"),
                        rs.getString("storage_type"),
                        rs.getInt("storage_sum"),
                        rs.getInt("storage_numOfReserved")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parts;
    }

    //Добавить
    public static boolean add(Storage part) {
        String sql = "INSERT INTO Storage (storage_type, storage_sum, storage_numOfReserved) VALUES (?, ?, ?)";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, part.getStorage_type());
            stmt.setInt(2, part.getStorage_sum());
            stmt.setInt(3, part.getStorage_numOfReserved());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Обновить
    public static boolean update(Storage part) {
        String sql = "UPDATE Storage SET storage_type=?, storage_sum=?, storage_numOfReserved=? WHERE storage_id=?";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, part.getStorage_type());
            stmt.setInt(2, part.getStorage_sum());
            stmt.setInt(3, part.getStorage_numOfReserved());
            stmt.setInt(4, part.getStorage_id());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Удалить
    public static boolean delete(int id) {
        String sql = "DELETE FROM Storage WHERE storage_id = ?";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}