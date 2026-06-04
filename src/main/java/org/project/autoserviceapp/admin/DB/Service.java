package org.project.autoserviceapp.admin.DB;

import org.project.autoserviceapp.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Service {
    private int service_id;
    private String service_name;
    private int storage_id;
    private String service_deadlines;
    private int service_price;
    private int worker_id;

    //Конструкторы
    public Service() {}

    public Service(int service_id, String service_name, int storage_id, String service_deadlines, int service_price, int worker_id) {
        this.service_id = service_id;
        this.service_name = service_name;
        this.storage_id = storage_id;
        this.service_deadlines = service_deadlines;
        this.service_price = service_price;
        this.worker_id = worker_id;
    }

    //Геттеры и сеттеры
    public int getService_id() { return service_id; }
    public void setService_id(int service_id) { this.service_id = service_id; }

    public String getService_name() { return service_name; }
    public void setService_name(String service_name) { this.service_name = service_name; }

    public int getStorage_id() { return storage_id; }
    public void setStorage_id(int storage_id) { this.storage_id = storage_id; }

    public String getService_deadlines() { return service_deadlines; }
    public void setService_deadlines(String service_deadlines) { this.service_deadlines = service_deadlines; }

    public int getService_price() { return service_price; }
    public void setService_price(int service_price) { this.service_price = service_price; }

    public int getWorker_id() { return worker_id; }
    public void setWorker_id(int worker_id) { this.worker_id = worker_id; }

    //Добавление всех Услуг
    public static List<Service> getAll() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM Service";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                services.add(new Service(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getInt("storage_id"),
                        rs.getString("service_deadlines"),
                        rs.getInt("service_price"),
                        rs.getInt("worker_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    //Кнопка "Добавить"
    public static boolean add(Service service) {
        String sql = "INSERT INTO Service (service_name, storage_id, service_deadlines, service_price, worker_id) " + "VALUES (?, ?, ?, ?, ?)";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, service.getService_name());
            stmt.setInt(2, service.getStorage_id());
            stmt.setString(3, service.getService_deadlines());
            stmt.setInt(4, service.getService_price());
            stmt.setInt(5, service.getWorker_id());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    service.setService_id(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Кнопка "Изменить"
    public static boolean update(Service service) {
        String sql = "UPDATE Service SET service_name=?, storage_id=?, service_deadlines=?, " + "service_price=?, worker_id=? WHERE service_id=?";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, service.getService_name());
            stmt.setInt(2, service.getStorage_id());
            stmt.setString(3, service.getService_deadlines());
            stmt.setInt(4, service.getService_price());
            stmt.setInt(5, service.getWorker_id());
            stmt.setInt(6, service.getService_id());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Кнопка "Удалить"
    public static boolean delete(int id) {
        String sql = "DELETE FROM Service WHERE service_id = ?";

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