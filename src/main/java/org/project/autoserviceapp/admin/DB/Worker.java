package org.project.autoserviceapp.admin.DB;

import org.project.autoserviceapp.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Worker {
    private int worker_id;
    private String worker_family;
    private String worker_name;
    private String worker_lastName;
    private String worker_phoneNum;
    private String worker_email;
    private String role;
    private String schedule;
    private double salary;

    //Конструкторы
    public Worker() {}

    public Worker(int worker_id, String worker_family, String worker_name, String worker_lastName,
                  String worker_phoneNum, String worker_email, String role, String schedule, double salary) {
        this.worker_id = worker_id;
        this.worker_family = worker_family;
        this.worker_name = worker_name;
        this.worker_lastName = worker_lastName;
        this.worker_phoneNum = worker_phoneNum;
        this.worker_email = worker_email;
        this.role = role;
        this.schedule = schedule;
        this.salary = salary;
    }

    //Геттеры и сеттеры
    public int getWorker_id() { return worker_id; }
    public void setWorker_id(int worker_id) { this.worker_id = worker_id; }

    public String getWorker_family() { return worker_family; }
    public void setWorker_family(String worker_family) { this.worker_family = worker_family; }

    public String getWorker_name() { return worker_name; }
    public void setWorker_name(String worker_name) { this.worker_name = worker_name; }

    public String getWorker_lastName() { return worker_lastName; }
    public void setWorker_lastName(String worker_lastName) { this.worker_lastName = worker_lastName; }

    public String getWorker_phoneNum() { return worker_phoneNum; }
    public void setWorker_phoneNum(String worker_phoneNum) { this.worker_phoneNum = worker_phoneNum; }

    public String getWorker_email() { return worker_email; }
    public void setWorker_email(String worker_email) { this.worker_email = worker_email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }


    //Получение всех сотрудников
    public static List<Worker> getAll() {
        List<Worker> workers = new ArrayList<>();
        String sql = "SELECT * FROM Worker";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                workers.add(new Worker(
                        rs.getInt("worker_id"),
                        rs.getString("worker_family"),
                        rs.getString("worker_name"),
                        rs.getString("worker_lastName"),
                        rs.getString("worker_phoneNum"),
                        rs.getString("worker_email"),
                        rs.getString("role"),
                        rs.getString("schedule"),
                        rs.getDouble("salary")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workers;
    }

    //Добавить
    public static boolean add(Worker worker) {
        String sql = "INSERT INTO Worker (worker_family, worker_name, worker_lastName, worker_phoneNum, worker_email, role, schedule, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, worker.getWorker_family());
            stmt.setString(2, worker.getWorker_name());
            stmt.setString(3, worker.getWorker_lastName());
            stmt.setString(4, worker.getWorker_phoneNum());
            stmt.setString(5, worker.getWorker_email());
            stmt.setString(6, worker.getRole());
            stmt.setString(7, worker.getSchedule());
            stmt.setDouble(8, worker.getSalary());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Обновить
    public static boolean update(Worker worker) {
        String sql = "UPDATE Worker SET worker_family=?, worker_name=?, worker_lastName=?, worker_phoneNum=?, worker_email=?, role=?, schedule=?, salary=? WHERE worker_id=?";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, worker.getWorker_family());
            stmt.setString(2, worker.getWorker_name());
            stmt.setString(3, worker.getWorker_lastName());
            stmt.setString(4, worker.getWorker_phoneNum());
            stmt.setString(5, worker.getWorker_email());
            stmt.setString(6, worker.getRole());
            stmt.setString(7, worker.getSchedule());
            stmt.setDouble(8, worker.getSalary());
            stmt.setInt(9, worker.getWorker_id());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Удалить
    public static boolean delete(int id) {
        String sql = "DELETE FROM Worker WHERE worker_id = ?";

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