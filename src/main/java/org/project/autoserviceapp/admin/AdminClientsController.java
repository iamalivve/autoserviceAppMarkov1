package org.project.autoserviceapp.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.project.autoserviceapp.DatabaseConnection;
import org.project.autoserviceapp.admin.DB.Client;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminClientsController {

    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client, Integer> colId;
    @FXML private TableColumn<Client, String> colFirstName;
    @FXML private TableColumn<Client, String> colLastName;
    @FXML private TableColumn<Client, String> colLogin;
    @FXML private TableColumn<Client, String> colPassword;
    @FXML private TableColumn<Client, String> colPhone;
    @FXML private TableColumn<Client, String> colEmail;

    @FXML private TextField idField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField loginField;
    @FXML private TextField passwordField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;

    @FXML private Button exitbutton;

    @FXML
    public void initialize() {
        System.out.println("=== AdminClientsController инициализирован ===");

        // Настройка колонок
        colId.setCellValueFactory(new PropertyValueFactory<>("client_id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("client_name"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("client_family"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("client_login"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("client_password"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("client_phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("client_email"));

        System.out.println("Колонки настроены");

        // Загрузка данных
        loadClients();

        // Выбор строки
        clientsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newVal) -> {
                    if (newVal != null) {
                        fillFields(newVal);
                    }
                }
        );
    }

    private void loadClients() {
        System.out.println("Загрузка клиентов из БД...");
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";  // ← ИСПРАВЛЕНО: Clients → Client

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("client_id"),
                        rs.getString("client_name"),
                        rs.getString("client_family"),
                        rs.getString("client_login"),
                        rs.getString("client_password"),
                        rs.getString("client_phoneNumber"),
                        rs.getString("client_email")
                );
                clients.add(client);
                System.out.println("Найден клиент: " + client.getClient_name() + " " + client.getClient_family());
            }

            clientsTable.setItems(FXCollections.observableArrayList(clients));
            System.out.println("Загружено клиентов: " + clients.size());

        } catch (SQLException e) {
            System.out.println("Ошибка при загрузке: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fillFields(Client c) {
        idField.setText(String.valueOf(c.getClient_id()));
        firstNameField.setText(c.getClient_name());
        lastNameField.setText(c.getClient_family());
        loginField.setText(c.getClient_login());
        passwordField.setText(c.getClient_password());
        phoneField.setText(c.getClient_phoneNumber());
        emailField.setText(c.getClient_email());
    }

    private void clearFields() {
        idField.clear();
        firstNameField.clear();
        lastNameField.clear();
        loginField.clear();
        passwordField.clear();
        phoneField.clear();
        emailField.clear();
        clientsTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void actionClear() {
        clearFields();
    }

    @FXML
    private void actionDelete() {
        Client selected = clientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите клиента");
            return;
        }

        String sql = "DELETE FROM Client WHERE client_id = ?";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, selected.getClient_id());
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                loadClients();
                clearFields();
                showAlert("Успех", "Клиент удалён");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось удалить: " + e.getMessage());
        }
    }

    @FXML
    private void actionEdit() {
        Client selected = clientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите клиента");
            return;
        }

        String sql = "UPDATE Client SET client_name=?, client_family=?, client_login=?, " +  "client_password=?, client_phoneNumber=?, client_email=? WHERE client_id=?";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstNameField.getText());
            stmt.setString(2, lastNameField.getText());
            stmt.setString(3, loginField.getText());
            stmt.setString(4, passwordField.getText());
            stmt.setString(5, phoneField.getText());
            stmt.setString(6, emailField.getText());
            stmt.setInt(7, selected.getClient_id());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                loadClients();
                clearFields();
                showAlert("Успех", "Данные обновлены");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось обновить: " + e.getMessage());
        }
    }

    @FXML
    private void actionAdd() {
        if (firstNameField.getText().isEmpty()) {
            showAlert("Ошибка", "Введите имя");
            return;
        }

        String sql = "INSERT INTO Client (client_name, client_family, client_login, " +  "client_password, client_phoneNumber, client_email) VALUES (?, ?, ?, ?, ?, ?)";

        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstNameField.getText());
            stmt.setString(2, lastNameField.getText());
            stmt.setString(3, loginField.getText());
            stmt.setString(4, passwordField.getText());
            stmt.setString(5, phoneField.getText());
            stmt.setString(6, emailField.getText());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                loadClients();
                clearFields();
                showAlert("Успех", "Клиент добавлен");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось добавить: " + e.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void actionExitButton(ActionEvent event) {
        ((Stage) exitbutton.getScene().getWindow()).close();
        openLoginController();
    }

    public void openLoginController() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/project/autoserviceapp/login/autoservice_login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("AVTO67");
            stage.setScene(new Scene(root, 550, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}