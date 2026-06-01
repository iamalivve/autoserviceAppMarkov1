package org.project.autoserviceapp.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.project.autoserviceapp.admin.DB.Order;

public class AdminHistoryOrdersController {

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> colId;
    @FXML private TableColumn<Order, Integer> colClientId;
    @FXML private TableColumn<Order, String> colOrderNumber;
    @FXML private TableColumn<Order, String> colStatus;
    @FXML private TableColumn<Order, String> colOrderDate;
    @FXML private TableColumn<Order, String> colOrderEndDate;
    @FXML private TableColumn<Order, Double> colTotalPrice;
    @FXML private TableColumn<Order, String> colServiceName;

    @FXML private TextField idField;
    @FXML private TextField clientIdField;
    @FXML private TextField orderNumberField;
    @FXML private TextField statusField;
    @FXML private TextField totalPriceField;
    @FXML private TextField serviceIdField;
    @FXML private TextField orderDateField;
    @FXML private TextField orderEndDateField;

    @FXML private Button clearButton;
    @FXML private Button deleteButton;
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
        colId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colClientId.setCellValueFactory(new PropertyValueFactory<>("client_id"));
        colOrderNumber.setCellValueFactory(new PropertyValueFactory<>("order_number"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colOrderEndDate.setCellValueFactory(new PropertyValueFactory<>("orderEndDate"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colServiceName.setCellValueFactory(new PropertyValueFactory<>("service_name"));

        loadOrders();

        ordersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newVal) -> { if (newVal != null) fillFields(newVal); }
        );
    }

    private void loadOrders() {
        ordersTable.setItems(FXCollections.observableArrayList(Order.getArchiveOrders()));
    }

    private void fillFields(Order o) {
        idField.setText(String.valueOf(o.getOrder_id()));
        clientIdField.setText(String.valueOf(o.getClient_id()));
        orderNumberField.setText(o.getOrder_number());
        statusField.setText(o.getOrder_status());
        totalPriceField.setText(String.valueOf(o.getTotalPrice()));
        serviceIdField.setText(String.valueOf(o.getService_id()));
        orderDateField.setText(o.getOrderDate());
        orderEndDateField.setText(o.getOrderEndDate());
    }

    private void clearFields() {
        idField.clear(); clientIdField.clear(); orderNumberField.clear();
        statusField.clear(); totalPriceField.clear(); serviceIdField.clear();
        orderDateField.clear(); orderEndDateField.clear();
        ordersTable.getSelectionModel().clearSelection();
    }

    @FXML private void actionClear() { clearFields(); }

    @FXML private void actionDelete() {
        Order selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Ошибка", "Выберите заказ"); return; }
        if (Order.delete(selected.getOrder_id())) {
            loadOrders(); clearFields();
            showAlert("Успех", "Заказ удалён из архива");
        }
    }

    //Навигация

    @FXML private void handleHouseButton() { SceneNavigator.goToHome(primaryStage); }
    @FXML private void handleClientsButton() { SceneNavigator.goToClients(primaryStage); }
    @FXML private void handleWorkersButton() { SceneNavigator.goToWorkers(primaryStage); }
    @FXML private void handleActiveOrdersButton() { SceneNavigator.goToActiveOrders(primaryStage); }
    @FXML private void handleHistoryOrdersButton() {}
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