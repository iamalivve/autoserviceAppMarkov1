package org.project.autoserviceapp.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.project.autoserviceapp.admin.DB.Order;

public class AdminActiveOrdersController {

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> colId;
    @FXML private TableColumn<Order, Integer> colClientId;
    @FXML private TableColumn<Order, String> colOrderNumber;
    @FXML private TableColumn<Order, String> colStatus;
    @FXML private TableColumn<Order, String> colOrderDate;
    @FXML private TableColumn<Order, Double> colTotalPrice;

    @FXML private TextField idField;
    @FXML private TextField clientIdField;
    @FXML private TextField orderNumberField;
    @FXML private TextField statusField;
    @FXML private TextField totalPriceField;
    @FXML private TextField serviceIdField;

    @FXML private Button clearButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private Button addButton;
    @FXML private Button exitbutton;
    @FXML private Label admins_name;

    @FXML private Button houseButton;
    @FXML private Button clientsButton;
    @FXML private Button workersButton;
    @FXML private Button activeOrdersButton;
    @FXML private Button historyOrdersButton;
    @FXML private Button storageButton;

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
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        loadOrders();

        ordersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newVal) -> { if (newVal != null) fillFields(newVal); }
        );
    }

    private void loadOrders() {
        ordersTable.setItems(FXCollections.observableArrayList(Order.getActiveOrders()));
    }

    private void fillFields(Order o) {
        idField.setText(String.valueOf(o.getOrder_id()));
        clientIdField.setText(String.valueOf(o.getClient_id()));
        orderNumberField.setText(o.getOrder_number());
        statusField.setText(o.getOrder_status());
        totalPriceField.setText(String.valueOf(o.getTotalPrice()));
        serviceIdField.setText(String.valueOf(o.getService_id()));
    }

    private void clearFields() {
        idField.clear(); clientIdField.clear(); orderNumberField.clear();
        statusField.clear(); totalPriceField.clear(); serviceIdField.clear();
        ordersTable.getSelectionModel().clearSelection();
    }

    @FXML private void actionClear() { clearFields(); }

    @FXML private void actionDelete() {
        Order selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Ошибка", "Выберите заказ"); return; }
        if (Order.delete(selected.getOrder_id())) {
            loadOrders(); clearFields();
            showAlert("Успех", "Заказ удалён");
        }
    }

    @FXML private void actionEdit() {
        Order selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Ошибка", "Выберите заказ"); return; }

        selected.setClient_id(Integer.parseInt(clientIdField.getText()));
        selected.setOrder_number(orderNumberField.getText());
        selected.setOrder_status(statusField.getText());
        selected.setTotalPrice(Double.parseDouble(totalPriceField.getText()));
        selected.setService_id(Integer.parseInt(serviceIdField.getText()));

        if (Order.update(selected)) {
            loadOrders(); clearFields();
            showAlert("Успех", "Данные обновлены");
        }
    }

    @FXML private void actionAdd() {
        if (clientIdField.getText().isEmpty()) { showAlert("Ошибка", "Введите ID клиента"); return; }

        Order newOrder = new Order();
        newOrder.setClient_id(Integer.parseInt(clientIdField.getText()));
        newOrder.setOrder_number(orderNumberField.getText());
        newOrder.setOrder_status("В работе");
        newOrder.setTotalPrice(Double.parseDouble(totalPriceField.getText()));
        newOrder.setService_id(Integer.parseInt(serviceIdField.getText()));

        if (Order.add(newOrder)) {
            loadOrders(); clearFields();
            showAlert("Успех", "Заказ добавлен");
        }
    }

    //Навигация

    @FXML private void handleHouseButton() { SceneNavigator.goToHome(primaryStage); }
    @FXML private void handleClientsButton() { SceneNavigator.goToClients(primaryStage); }
    @FXML private void handleWorkersButton() { SceneNavigator.goToWorkers(primaryStage); }
    @FXML private void handleActiveOrdersButton() {}
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