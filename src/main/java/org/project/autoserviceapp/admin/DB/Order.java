package org.project.autoserviceapp.admin.DB;

import org.project.autoserviceapp.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private int order_id;
    private int client_id;
    private String order_number;
    private String order_status;
    private String orderDate;
    private String orderEndDate;
    private double totalPrice;
    private int service_id;

    //Конструкторы
    public Order() {}

    public Order(int order_id, int client_id, String order_number, String order_status, String orderDate, String orderEndDate, double totalPrice, int service_id) {
        this.order_id = order_id;
        this.client_id = client_id;
        this.order_number = order_number;
        this.order_status = order_status;
        this.orderDate = orderDate;
        this.orderEndDate = orderEndDate;
        this.totalPrice = totalPrice;
        this.service_id = service_id;
    }

    //Геттеры и сеттеры
    public int getOrder_id() { return order_id; }
    public void setOrder_id(int order_id) { this.order_id = order_id; }

    public int getClient_id() { return client_id; }
    public void setClient_id(int client_id) { this.client_id = client_id; }

    public String getOrder_number() { return order_number; }
    public void setOrder_number(String order_number) { this.order_number = order_number; }

    public String getOrder_status() { return order_status; }
    public void setOrder_status(String order_status) { this.order_status = order_status; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getOrderEndDate() { return orderEndDate; }
    public void setOrderEndDate(String orderEndDate) { this.orderEndDate = orderEndDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public int getService_id() { return service_id; }
    public void setService_id(int service_id) { this.service_id = service_id; }

    //Добавление данных для Активных Заказов
    public static List<Order> getActiveOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE order_status != 'Готов'";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getInt("client_id"),
                        rs.getString("order_number"),
                        rs.getString("order_status"),
                        rs.getString("orderDate"),
                        rs.getString("orderEndDate"),
                        rs.getDouble("totalPrice"),
                        rs.getInt("service_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    //Добавление данных для Архива Заказов
    public static List<Order> getArchiveOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE order_status = 'Готов'";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getInt("client_id"),
                        rs.getString("order_number"),
                        rs.getString("order_status"),
                        rs.getString("orderDate"),
                        rs.getString("orderEndDate"),
                        rs.getDouble("totalPrice"),
                        rs.getInt("service_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    //Кнопка "Добавить"
    public static boolean add(Order order) {
        String sql = "INSERT INTO Orders (client_id, order_number, order_status, orderDate, totalPrice, service_id) " + "VALUES (?, ?, ?, date('now'), ?, ?)";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getClient_id());
            stmt.setString(2, order.getOrder_number());
            stmt.setString(3, "В работе");
            stmt.setDouble(4, order.getTotalPrice());
            stmt.setInt(5, order.getService_id());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Кнопка "Изменить"
    public static boolean update(Order order) {
        String sql = "UPDATE Orders SET client_id=?, order_number=?, order_status=?, " + "orderEndDate=?, totalPrice=?, service_id=? WHERE order_id=?";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getClient_id());
            stmt.setString(2, order.getOrder_number());
            stmt.setString(3, order.getOrder_status());
            stmt.setString(4, order.getOrderEndDate());
            stmt.setDouble(5, order.getTotalPrice());
            stmt.setInt(6, order.getService_id());
            stmt.setInt(7, order.getOrder_id());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Кнопка "Удалить"
    public static boolean delete(int id) {
        String sql = "DELETE FROM Orders WHERE order_id = ?";

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

    //Сбор информации для статистики в контроллере "Главная страница"
    public static Map<String, Double> getMonthlySalesData() {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT strftime('%m.%Y', orderEndDate) as month, SUM(totalPrice) as total " + "FROM Orders WHERE order_status = 'Готов' AND orderEndDate IS NOT NULL " + "GROUP BY month ORDER BY orderEndDate";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String month = rs.getString("month");
                double total = rs.getDouble("total");

                if (month != null && !month.isEmpty()) {
                    data.put(month, total);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (data.isEmpty()) {
            data.put("Нет данных", 0.0);
        }
        return data;
    }
}