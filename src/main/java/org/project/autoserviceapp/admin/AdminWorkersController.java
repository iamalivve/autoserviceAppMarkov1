package org.project.autoserviceapp.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.project.autoserviceapp.admin.DB.Worker;

public class AdminWorkersController {

    @FXML private TableView<Worker> workersTable;
    @FXML private TableColumn<Worker, Integer> colId;
    @FXML private TableColumn<Worker, String> colFamily;
    @FXML private TableColumn<Worker, String> colName;
    @FXML private TableColumn<Worker, String> colLastName;
    @FXML private TableColumn<Worker, String> colPhone;
    @FXML private TableColumn<Worker, String> colEmail;
    @FXML private TableColumn<Worker, String> colRole;
    @FXML private TableColumn<Worker, String> colSchedule;
    @FXML private TableColumn<Worker, Double> colSalary;

    @FXML private TextField idField;
    @FXML private TextField familyField;
    @FXML private TextField nameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField roleField;
    @FXML private TextField scheduleField;
    @FXML private TextField salaryField;

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
        colId.setCellValueFactory(new PropertyValueFactory<>("worker_id"));
        colFamily.setCellValueFactory(new PropertyValueFactory<>("worker_family"));
        colName.setCellValueFactory(new PropertyValueFactory<>("worker_name"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("worker_lastName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("worker_phoneNum"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("worker_email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colSchedule.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        loadWorkers();

        workersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newVal) -> { if (newVal != null) fillFields(newVal); }
        );
    }

    private void loadWorkers() {
        workersTable.setItems(FXCollections.observableArrayList(Worker.getAll()));
    }

    private void fillFields(Worker w) {
        idField.setText(String.valueOf(w.getWorker_id()));
        familyField.setText(w.getWorker_family());
        nameField.setText(w.getWorker_name());
        lastNameField.setText(w.getWorker_lastName());
        phoneField.setText(w.getWorker_phoneNum());
        emailField.setText(w.getWorker_email());
        roleField.setText(w.getRole());
        scheduleField.setText(w.getSchedule());
        salaryField.setText(String.valueOf(w.getSalary()));
    }

    private void clearFields() {
        idField.clear(); familyField.clear(); nameField.clear(); lastNameField.clear();
        phoneField.clear(); emailField.clear(); roleField.clear(); scheduleField.clear(); salaryField.clear();
        workersTable.getSelectionModel().clearSelection();
    }

    @FXML private void actionClear() { clearFields(); }

    @FXML private void actionDelete() {
        Worker selected = workersTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Ошибка", "Выберите сотрудника"); return; }
        if (Worker.delete(selected.getWorker_id())) {
            loadWorkers(); clearFields();
            showAlert("Успех", "Сотрудник удалён");
        }
    }

    @FXML private void actionEdit() {
        Worker selected = workersTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Ошибка", "Выберите сотрудника"); return; }

        selected.setWorker_family(familyField.getText());
        selected.setWorker_name(nameField.getText());
        selected.setWorker_lastName(lastNameField.getText());
        selected.setWorker_phoneNum(phoneField.getText());
        selected.setWorker_email(emailField.getText());
        selected.setRole(roleField.getText());
        selected.setSchedule(scheduleField.getText());
        selected.setSalary(Double.parseDouble(salaryField.getText()));

        if (Worker.update(selected)) {
            loadWorkers(); clearFields();
            showAlert("Успех", "Данные обновлены");
        }
    }

    @FXML private void actionAdd() {
        if (nameField.getText().isEmpty()) { showAlert("Ошибка", "Введите имя"); return; }

        Worker newWorker = new Worker();
        newWorker.setWorker_family(familyField.getText());
        newWorker.setWorker_name(nameField.getText());
        newWorker.setWorker_lastName(lastNameField.getText());
        newWorker.setWorker_phoneNum(phoneField.getText());
        newWorker.setWorker_email(emailField.getText());
        newWorker.setRole(roleField.getText());
        newWorker.setSchedule(scheduleField.getText());
        newWorker.setSalary(Double.parseDouble(salaryField.getText()));

        if (Worker.add(newWorker)) {
            loadWorkers(); clearFields();
            showAlert("Успех", "Сотрудник добавлен");
        }
    }

    //Навигация

    @FXML private void handleHouseButton() { SceneNavigator.goToHome(primaryStage); }
    @FXML private void handleClientsButton() { SceneNavigator.goToClients(primaryStage); }
    @FXML private void handleWorkersButton() {}
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