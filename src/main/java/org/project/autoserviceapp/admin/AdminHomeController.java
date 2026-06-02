package org.project.autoserviceapp.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.project.autoserviceapp.DatabaseConnection;
import org.project.autoserviceapp.admin.DB.Client;
import org.project.autoserviceapp.admin.DB.Order;
import org.project.autoserviceapp.admin.DB.Storage;

import java.sql.*;

public class AdminHomeController {

    //Счетчики статистики
    @FXML private Label clientsCountLabel;
    @FXML private Label activeOrdersCountLabel;
    @FXML private Label storageCountLabel;

    //Интерфейс левой части
    @FXML private Button houseButton;
    @FXML private Button clientsButton;
    @FXML private Button workersButton;
    @FXML private Button servicesButton;
    @FXML private Button activeOrdersButton;
    @FXML private Button historyOrdersButton;
    @FXML private Button storageButton;
    @FXML private Label admins_name;
    @FXML private Button exitbutton;

    //Переменные
    private Stage primaryStage;
    private String adminName;

    //Устанавливает главное окно
    public void setPrimaryStage(Stage stage) { this.primaryStage = stage; }

    //Устанавливает имя администратора
    public void setAdminName(String name) {
        this.adminName = name;
        if (admins_name != null) admins_name.setText(name);
        loadCounts();
    }

    //Предзагрузка
    @FXML public void initialize() { loadCounts(); }

    //Метод вывода числа на счетчики
    private void loadCounts() {
        clientsCountLabel.setText(String.valueOf(Client.getAll().size()));
        activeOrdersCountLabel.setText(String.valueOf(Order.getActiveOrders().size()));

        String sql = "SELECT SUM(storage_sum) FROM Storage";
        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) storageCountLabel.setText(String.valueOf(rs.getInt(1)));
        } catch (SQLException e) { storageCountLabel.setText("0"); }
    }

    //Методы реализации смены Scene по нажатию кнопки
    @FXML private void handleHouseButton() {loadCounts();}
    @FXML private void handleClientsButton() {SceneNavigator.goToClients(primaryStage);}
    @FXML private void handleWorkersButton() {SceneNavigator.goToWorkers(primaryStage);}
    @FXML private void handleServicesButton() {SceneNavigator.goToServices(primaryStage);}
    @FXML private void handleActiveOrdersButton() {SceneNavigator.goToActiveOrders(primaryStage);}
    @FXML private void handleHistoryOrdersButton() {SceneNavigator.goToHistoryOrders(primaryStage);}
    @FXML private void handleStorageButton() {SceneNavigator.goToStorage(primaryStage);}
    @FXML public void actionExitButton(ActionEvent event) {SceneNavigator.goToLogin(primaryStage);}
}