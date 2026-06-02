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
import org.project.autoserviceapp.admin.DB.Service;
import java.io.IOException;

public class AdminServiceController {

    //Столбцы таблицы
    @FXML private TableView<Service> servicesTable;
    @FXML private TableColumn<Service, Integer> colId;
    @FXML private TableColumn<Service, String> colName;
    @FXML private TableColumn<Service, Integer> colStorageId;
    @FXML private TableColumn<Service, String> colDeadlines;
    @FXML private TableColumn<Service, Integer> colPrice;
    @FXML private TableColumn<Service, Integer> colWorkerId;

    //Текстовые поля
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField storageIdField;
    @FXML private TextField deadlinesField;
    @FXML private TextField priceField;
    @FXML private TextField workerIdField;

    //Кнопки
    @FXML private Button clearButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private Button addButton;

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
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("service_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("service_name"));
        colStorageId.setCellValueFactory(new PropertyValueFactory<>("storage_id"));
        colDeadlines.setCellValueFactory(new PropertyValueFactory<>("service_deadlines"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("service_price"));
        colWorkerId.setCellValueFactory(new PropertyValueFactory<>("worker_id"));

        loadServices();

        servicesTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newVal) -> { if (newVal != null) fillFields(newVal); }
        );
    }

    //Выгрузка данных
    private void loadServices() {
        servicesTable.setItems(FXCollections.observableArrayList(Service.getAll()));
    }

    //Метод заполнения полей
    private void fillFields(Service s) {
        idField.setText(String.valueOf(s.getService_id()));
        nameField.setText(s.getService_name());
        storageIdField.setText(String.valueOf(s.getStorage_id()));
        deadlinesField.setText(s.getService_deadlines());
        priceField.setText(String.valueOf(s.getService_price()));
        workerIdField.setText(String.valueOf(s.getWorker_id()));
    }

    //Метод очистки
    private void clearFields() {
        idField.clear(); nameField.clear(); storageIdField.clear();
        deadlinesField.clear(); priceField.clear(); workerIdField.clear();
        servicesTable.getSelectionModel().clearSelection();
    }

    //Кнопка "Очистить"
    @FXML private void actionClear() { clearFields(); }

    //Кнопка "Удалить"
    @FXML
    private void actionDelete() {
        Service selected = servicesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите услугу");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение удаления");
        confirm.setHeaderText("Удалить услугу \"" + selected.getService_name() + "\"?");
        confirm.setContentText("Вы действительно хотите удалить эту услугу?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (Service.delete(selected.getService_id())) {
                loadServices();
                clearFields();
                showAlert("Успех", "Услуга удалена");
            } else {
                showAlert("Ошибка БД", "Не удалось удалить услугу");
            }
        }
    }

    //Кнопка "Изменить"
    @FXML
    private void actionEdit() {
        Service selected = servicesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите услугу");
            return;
        }
        selected.setService_name(nameField.getText());
        selected.setStorage_id(Integer.parseInt(storageIdField.getText()));
        selected.setService_deadlines(deadlinesField.getText());
        selected.setService_price(Integer.parseInt(priceField.getText()));
        selected.setWorker_id(Integer.parseInt(workerIdField.getText()));

        if (Service.update(selected)) {
            loadServices();
            clearFields();
            showAlert("Успех", "Данные обновлены");
        } else {
            showAlert("Ошибка БД", "Не удалось обновить данные");
        }
    }

    //Кнопка "Добавить"
    @FXML
    private void actionAdd() {
        if (nameField.getText().isEmpty()) {
            showAlert("Ошибка", "Введите название услуги");
            return;
        }
        Service newService = new Service();
        newService.setService_name(nameField.getText());
        newService.setStorage_id(Integer.parseInt(storageIdField.getText()));
        newService.setService_deadlines(deadlinesField.getText());
        newService.setService_price(Integer.parseInt(priceField.getText()));
        newService.setWorker_id(Integer.parseInt(workerIdField.getText()));

        if (Service.add(newService)) {
            loadServices();
            clearFields();
            showAlert("Успех", "Услуга добавлена");
        } else {
            showAlert("Ошибка БД", "Не удалось добавить услугу");
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
    @FXML private void handleServicesButton() {}
    @FXML private void handleActiveOrdersButton() { SceneNavigator.goToActiveOrders(primaryStage); }
    @FXML private void handleHistoryOrdersButton() { SceneNavigator.goToHistoryOrders(primaryStage); }
    @FXML private void handleStorageButton() { SceneNavigator.goToStorage(primaryStage); }
    @FXML public void actionExitButton(ActionEvent event) {
        SceneNavigator.goToLogin(primaryStage);
    }
}