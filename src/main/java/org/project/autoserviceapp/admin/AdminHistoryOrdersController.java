package org.project.autoserviceapp.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.project.autoserviceapp.admin.DB.Order;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class AdminHistoryOrdersController implements Initializable{

    //Столбцы таблицы
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> colId;
    @FXML private TableColumn<Order, Integer> colClientId;
    @FXML private TableColumn<Order, String> colOrderNumber;
    @FXML private TableColumn<Order, String> colStatus;
    @FXML private TableColumn<Order, String> colOrderDate;
    @FXML private TableColumn<Order, String> colOrderEndDate;
    @FXML private TableColumn<Order, Double> colTotalPrice;
    @FXML private TableColumn<Order, Integer> colServiceId;

    //Текстовые поля
    @FXML private TextField idField;
    @FXML private TextField clientIdField;
    @FXML private TextField orderNumberField;
    @FXML private TextField statusField;
    @FXML private TextField totalPriceField;
    @FXML private TextField serviceIdField;
    @FXML private TextField orderDateField;
    @FXML private TextField orderEndDateField;

    //Кнопки
    @FXML private Button clearButton;
    @FXML private Button deleteButton;

    //Интерфейс левой части
    @FXML private Button houseButton;
    @FXML private Button clientsButton;
    @FXML private Button workersButton;
    @FXML private Button servicesButton;
    @FXML private Button activeOrdersButton;
    @FXML private Button historyOrdersButton;
    @FXML private Button storageButton;
    @FXML private Button exitbutton;
    @FXML private Label admins_name;
    @FXML private ImageView brandingImageView;

    //Переменные
    private Stage primaryStage;
    private String adminName;

    //Устанавливает главное окно
    public void setPrimaryStage(Stage stage) { this.primaryStage = stage; }

    //Устанавливает имя администратора
    public void setAdminName(String name) {
        this.adminName = name;
        if (admins_name != null) admins_name.setText(name);
    }

    //Предзагрузка
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File brandingFile = new File("images/User.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        colId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colClientId.setCellValueFactory(new PropertyValueFactory<>("client_id"));
        colOrderNumber.setCellValueFactory(new PropertyValueFactory<>("order_number"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colOrderEndDate.setCellValueFactory(new PropertyValueFactory<>("orderEndDate"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colServiceId.setCellValueFactory(new PropertyValueFactory<>("service_id"));

        loadOrders();

        ordersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newVal) -> { if (newVal != null) fillFields(newVal); }
        );
    }

    //Выгрузка данных
    private void loadOrders() {
        ordersTable.setItems(FXCollections.observableArrayList(Order.getArchiveOrders()));
    }

    //Метод заполнения полей
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

    //Метод очистки
    private void clearFields() {
        idField.clear();
        clientIdField.clear();
        orderNumberField.clear();
        statusField.clear();
        totalPriceField.clear();
        serviceIdField.clear();
        orderDateField.clear();
        orderEndDateField.clear();
        ordersTable.getSelectionModel().clearSelection();
    }

    //Кнопка "Очистить"
    @FXML private void actionClear() { clearFields(); }

    //Кнопка "Удалить"
    @FXML
    private void actionDelete() {
        Order selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите заказ");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение удаления");
        confirm.setHeaderText("Удалить заказ №" + selected.getOrder_number() + " из архива?");
        confirm.setContentText("Вы действительно хотите удалить этот заказ?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (Order.delete(selected.getOrder_id())) {
                loadOrders();
                clearFields();
                showAlert("Успех", "Заказ удалён из архива");
            } else {
                showAlert("Ошибка БД", "Не удалось удалить заказ");
            }
        }
    }

    //Метод показа уведомления
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(msg);
        alert.showAndWait();
    }
    
    //Методы реализации смены Scene по нажатию кнопки
    @FXML private void handleHouseButton() { SceneNavigator.goToHome(primaryStage); }
    @FXML private void handleClientsButton() { SceneNavigator.goToClients(primaryStage); }
    @FXML private void handleWorkersButton() { SceneNavigator.goToWorkers(primaryStage); }
    @FXML private void handleServicesButton() { SceneNavigator.goToServices(primaryStage); }
    @FXML private void handleActiveOrdersButton() { SceneNavigator.goToActiveOrders(primaryStage); }
    @FXML private void handleHistoryOrdersButton() { /* уже здесь */ }
    @FXML private void handleStorageButton() { SceneNavigator.goToStorage(primaryStage); }
    @FXML public void actionExitButton(ActionEvent event) {SceneNavigator.goToLogin(primaryStage);}
}