package org.project.autoserviceapp.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.project.autoserviceapp.admin.DB.Storage;

public class AdminStorageController {

    @FXML private TableView<Storage> storageTable;
    @FXML private TableColumn<Storage, Integer> colId;
    @FXML private TableColumn<Storage, String> colType;
    @FXML private TableColumn<Storage, Integer> colSum;
    @FXML private TableColumn<Storage, Integer> colReserved;

    @FXML private TextField idField;
    @FXML private TextField typeField;
    @FXML private TextField sumField;
    @FXML private TextField reservedField;

    @FXML private Button clearButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private Button addButton;
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
        colId.setCellValueFactory(new PropertyValueFactory<>("storage_id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("storage_type"));
        colSum.setCellValueFactory(new PropertyValueFactory<>("storage_sum"));
        colReserved.setCellValueFactory(new PropertyValueFactory<>("storage_numOfReserved"));

        loadStorage();

        storageTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newVal) -> { if (newVal != null) fillFields(newVal); }
        );
    }

    private void loadStorage() {
        storageTable.setItems(FXCollections.observableArrayList(Storage.getAll()));
    }

    private void fillFields(Storage s) {
        idField.setText(String.valueOf(s.getStorage_id()));
        typeField.setText(s.getStorage_type());
        sumField.setText(String.valueOf(s.getStorage_sum()));
        reservedField.setText(String.valueOf(s.getStorage_numOfReserved()));
    }

    private void clearFields() {
        idField.clear(); typeField.clear(); sumField.clear(); reservedField.clear();
        storageTable.getSelectionModel().clearSelection();
    }

    @FXML private void actionClear() { clearFields(); }

    @FXML private void actionDelete() {
        Storage selected = storageTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Ошибка", "Выберите запчасть"); return; }
        if (Storage.delete(selected.getStorage_id())) {
            loadStorage(); clearFields();
            showAlert("Успех", "Запчасть удалена");
        }
    }

    @FXML private void actionEdit() {
        Storage selected = storageTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Ошибка", "Выберите запчасть"); return; }

        selected.setStorage_type(typeField.getText());
        selected.setStorage_sum(Integer.parseInt(sumField.getText()));
        selected.setStorage_numOfReserved(Integer.parseInt(reservedField.getText()));

        if (Storage.update(selected)) {
            loadStorage(); clearFields();
            showAlert("Успех", "Данные обновлены");
        }
    }

    @FXML private void actionAdd() {
        if (typeField.getText().isEmpty()) { showAlert("Ошибка", "Введите тип запчасти"); return; }

        Storage newPart = new Storage();
        newPart.setStorage_type(typeField.getText());
        newPart.setStorage_sum(Integer.parseInt(sumField.getText()));
        newPart.setStorage_numOfReserved(Integer.parseInt(reservedField.getText()));

        if (Storage.add(newPart)) {
            loadStorage(); clearFields();
            showAlert("Успех", "Запчасть добавлена");
        }
    }

    //Навигация

    @FXML private void handleHouseButton() { SceneNavigator.goToHome(primaryStage); }
    @FXML private void handleClientsButton() { SceneNavigator.goToClients(primaryStage); }
    @FXML private void handleWorkersButton() { SceneNavigator.goToWorkers(primaryStage); }
    @FXML private void handleActiveOrdersButton() { SceneNavigator.goToActiveOrders(primaryStage); }
    @FXML private void handleHistoryOrdersButton() { SceneNavigator.goToHistoryOrders(primaryStage); }
    @FXML private void handleStorageButton() {}

    @FXML public void actionExitButton(ActionEvent event) {
        SceneNavigator.goToLogin(primaryStage);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(msg);
        alert.showAndWait();
    }
}