package org.project.autoserviceapp.admin.DB;

import org.project.autoserviceapp.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private int client_id;
    private String client_name;
    private String client_family;
    private String client_login;
    private String client_password;
    private String client_phoneNumber;
    private String client_email;

    //Конструкторы
    public Client() {}

    public Client(int client_id, String client_name, String client_family, String client_login, String client_password, String client_phoneNumber, String client_email) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.client_family = client_family;
        this.client_login = client_login;
        this.client_password = client_password;
        this.client_phoneNumber = client_phoneNumber;
        this.client_email = client_email;
    }

    //Геттеры и сеттеры
    public int getClient_id() { return client_id; }
    public void setClient_id(int client_id) { this.client_id = client_id; }

    public String getClient_name() { return client_name; }
    public void setClient_name(String client_name) { this.client_name = client_name; }

    public String getClient_family() { return client_family; }
    public void setClient_family(String client_family) { this.client_family = client_family; }

    public String getClient_login() { return client_login; }
    public void setClient_login(String client_login) { this.client_login = client_login; }

    public String getClient_password() { return client_password; }
    public void setClient_password(String client_password) { this.client_password = client_password; }

    public String getClient_phoneNumber() { return client_phoneNumber; }
    public void setClient_phoneNumber(String client_phoneNumber) { this.client_phoneNumber = client_phoneNumber; }

    public String getClient_email() { return client_email; }
    public void setClient_email(String client_email) { this.client_email = client_email; }



    //Получить все данные
    public static List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("client_id"),
                        rs.getString("client_name"),
                        rs.getString("client_family"),
                        rs.getString("client_login"),
                        rs.getString("client_password"),
                        rs.getString("client_phoneNumber"),
                        rs.getString("client_email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    //Добавить
    public static boolean add(Client client) {
        String sql = "INSERT INTO Client (client_name, client_family, client_login, " + "client_password, client_phoneNumber, client_email) VALUES (?, ?, ?, ?, ?, ?)";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getClient_name());
            stmt.setString(2, client.getClient_family());
            stmt.setString(3, client.getClient_login());
            stmt.setString(4, client.getClient_password());
            stmt.setString(5, client.getClient_phoneNumber());
            stmt.setString(6, client.getClient_email());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    client.setClient_id(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Обновить
    public static boolean update(Client client) {
        String sql = "UPDATE Client SET client_name=?, client_family=?, client_login=?, " + "client_password=?, client_phoneNumber=?, client_email=? WHERE client_id=?";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getClient_name());
            stmt.setString(2, client.getClient_family());
            stmt.setString(3, client.getClient_login());
            stmt.setString(4, client.getClient_password());
            stmt.setString(5, client.getClient_phoneNumber());
            stmt.setString(6, client.getClient_email());
            stmt.setInt(7, client.getClient_id());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Удалить
    public static boolean delete(int id) {
        String sql = "DELETE FROM Client WHERE client_id = ?";

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