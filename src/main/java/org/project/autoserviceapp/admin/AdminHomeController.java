package org.project.autoserviceapp.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.project.autoserviceapp.DatabaseConnection;
import java.sql.*;

public class AdminHomeController {

    @FXML private Label clientsCountLabel;
    @FXML private Label activeOrdersCountLabel;
    @FXML private Label storageCountLabel;
    @FXML private Label admins_name;
    @FXML private Button exitbutton;

    private Stage primaryStage;
    private String adminName;

    public void setPrimaryStage(Stage stage) { this.primaryStage = stage; }

    public void setAdminName(String name) {
        this.adminName = name;
        if (admins_name != null) admins_name.setText(name);
        loadCounts();
    }

    @FXML
    public void initialize() {
        loadCounts();
    }

    //Навигация

    @FXML private void handleHouseButton() { loadCounts(); }
    @FXML private void handleClientsButton() { SceneNavigator.goToClients(primaryStage); }
    @FXML private void handleWorkersButton() { SceneNavigator.goToWorkers(primaryStage); }
    @FXML private void handleActiveOrdersButton() { SceneNavigator.goToActiveOrders(primaryStage); }
    @FXML private void handleHistoryOrdersButton() { SceneNavigator.goToHistoryOrders(primaryStage); }
    @FXML private void handleStorageButton() { SceneNavigator.goToStorage(primaryStage); }

    @FXML public void actionExitButton(ActionEvent event) {
        SceneNavigator.goToLogin(primaryStage);
    }

    //Счетчик

    private void loadCounts() {
        loadClientsCount();
        loadActiveOrdersCount();
        loadStorageCount();
    }

    private void loadClientsCount() {
        String sql = "SELECT COUNT(*) FROM Client";
        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) clientsCountLabel.setText(String.valueOf(rs.getInt(1)));
        } catch (SQLException e) { clientsCountLabel.setText("0"); }
    }

    private void loadActiveOrdersCount() {
        String sql = "SELECT COUNT(*) FROM Orders WHERE order_status != 'Готов'";
        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) activeOrdersCountLabel.setText(String.valueOf(rs.getInt(1)));
        } catch (SQLException e) { activeOrdersCountLabel.setText("0"); }
    }

    private void loadStorageCount() {
        String sql = "SELECT SUM(storage_sum) FROM Storage";
        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) storageCountLabel.setText(String.valueOf(rs.getInt(1)));
        } catch (SQLException e) { storageCountLabel.setText("0"); }
    }
}