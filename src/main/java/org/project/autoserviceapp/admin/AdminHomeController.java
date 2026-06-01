package org.project.autoserviceapp.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class AdminHomeController {
    @FXML
    private Label admins_name;
    @FXML
    private Button exitbutton;
    @FXML
    private Button houseButton;
    @FXML
    private Button clientsButton;
    @FXML
    private Button workersButton;
    @FXML
    private Button activeOrdersButton;
    @FXML
    private Button historyOrdersButton;
    @FXML
    private Button storageButton;

    private Stage primaryStage;
    private String adminName;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setAdminName(String name) {
        this.adminName = name;
        if (admins_name != null) {
            admins_name.setText(name);
        }
    }
    @FXML
    private void handleHouseButton() {}

    @FXML
    private void handleClientsButton() {
        primaryStage.close();
        openClients();
    }
    @FXML
    private void handleWorkersButton() {
        primaryStage.close();
        openWorkers();
    }
    @FXML
    private void handleActiveOrdersButton() {
        primaryStage.close();
        openActiveOrders();
    }
    @FXML
    private void handleHistoryOrdersButton() {
        primaryStage.close();
        openHistoryOrders();
    }
    @FXML
    private void handleStorageButton() {
        primaryStage.close();
        openStorage();
    }
    @FXML
    public void actionExitButton(ActionEvent event) {
        primaryStage.close();
        logOut();
    }
    private void openClients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_clients.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 600);
            Stage stage = new Stage();
            stage.setTitle("AVTO67");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openWorkers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_workers.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 600);
            Stage stage = new Stage();
            stage.setTitle("AVTO67");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openActiveOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_activeOrders.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 600);
            Stage stage = new Stage();
            stage.setTitle("AVTO67");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openHistoryOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_historyOrders.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 600);
            Stage stage = new Stage();
            stage.setTitle("AVTO67");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openStorage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_storage.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 600);
            Stage stage = new Stage();
            stage.setTitle("AVTO67");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/autoserviceapp/login/autoservice_login.fxml"));
            Scene scene = new Scene(loader.load(), 550, 400);
            Stage stage = new Stage();
            stage.setTitle("AVTO67");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}