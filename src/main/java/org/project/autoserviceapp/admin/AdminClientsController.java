package org.project.autoserviceapp.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.project.autoserviceapp.admin.DB.Client;

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
    @FXML private Label admins_name;

    private Stage primaryStage;
    private String adminName;

    public void setPrimaryStage(Stage stage) { this.primaryStage = stage; }

    public void setAdminName(String name) {
        this.adminName = name;
        if (admins_name != null) admins_name.setText(name);
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("client_id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("client_name"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("client_family"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("client_login"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("client_password"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("client_phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("client_email"));

        loadClients();

        clientsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newVal) -> { if (newVal != null) fillFields(newVal); }
        );
    }

    private void loadClients() {
        clientsTable.setItems(FXCollections.observableArrayList(Client.getAll()));
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
        idField.clear(); firstNameField.clear(); lastNameField.clear();
        loginField.clear(); passwordField.clear(); phoneField.clear(); emailField.clear();
        clientsTable.getSelectionModel().clearSelection();
    }

    @FXML private void actionClear() { clearFields(); }

    @FXML private void actionDelete() {
        Client selected = clientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Ошибка", "Выберите клиента"); return; }
        if (Client.delete(selected.getClient_id())) {
            loadClients(); clearFields();
            showAlert("Успех", "Клиент удалён");
        }
    }

    @FXML private void actionEdit() {
        Client selected = clientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Ошибка", "Выберите клиента"); return; }

        selected.setClient_name(firstNameField.getText());
        selected.setClient_family(lastNameField.getText());
        selected.setClient_login(loginField.getText());
        selected.setClient_password(passwordField.getText());
        selected.setClient_phoneNumber(phoneField.getText());
        selected.setClient_email(emailField.getText());

        if (Client.update(selected)) {
            loadClients(); clearFields();
            showAlert("Успех", "Данные обновлены");
        }
    }

    @FXML private void actionAdd() {
        if (firstNameField.getText().isEmpty()) { showAlert("Ошибка", "Введите имя"); return; }

        Client newClient = new Client();
        newClient.setClient_name(firstNameField.getText());
        newClient.setClient_family(lastNameField.getText());
        newClient.setClient_login(loginField.getText());
        newClient.setClient_password(passwordField.getText());
        newClient.setClient_phoneNumber(phoneField.getText());
        newClient.setClient_email(emailField.getText());

        if (Client.add(newClient)) {
            loadClients(); clearFields();
            showAlert("Успех", "Клиент добавлен");
        }
    }

    //Навигация

    @FXML private void handleHouseButton() { SceneNavigator.goToHome(primaryStage); }
    @FXML private void handleClientsButton() {}
    @FXML private void handleWorkersButton() { SceneNavigator.goToWorkers(primaryStage); }
    @FXML private void handleActiveOrdersButton() { SceneNavigator.goToActiveOrders(primaryStage); }
    @FXML private void handleHistoryOrdersButton() { SceneNavigator.goToHistoryOrders(primaryStage); }
    @FXML private void handleStorageButton() { SceneNavigator.goToStorage(primaryStage); }

    @FXML public void actionExitButton(ActionEvent event) {
        SceneNavigator.goToLogin(primaryStage);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(msg);
        alert.showAndWait();
    }
}