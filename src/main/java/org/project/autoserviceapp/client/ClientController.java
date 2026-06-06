package org.project.autoserviceapp.client;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.project.autoserviceapp.DatabaseConnection;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private GridPane orderGridPane;

    @FXML
    private Button OpenCatalog_btn;

    @FXML
    private Button OpenMyAccount_btn;

    @FXML
    private Button userLogout;

    @FXML
    private AnchorPane CatalogForm;

    @FXML
    private AnchorPane MyAccountForm;

    @FXML
    private Label currentPage;

    @FXML
    private TextField prof_email_textarea;

    @FXML
    private TextField prof_family_textarea;

    @FXML
    private TextField prof_name_textarea;

    @FXML
    private PasswordField prof_password_field;

    @FXML
    private PasswordField prof_confirmPassword_field;

    @FXML
    private Button profileUpdate_btn;

    @FXML
    private Button userLogout_btn;

    @FXML
    private Label usernameMyAccount;

    @FXML
    private Label CurrentDateMyAccount;

    @FXML
    private Label profileErrorMessage;

    @FXML
    private Label profileSuccessMessage;

    @FXML
    private ScrollPane selectedServicesScrollPane;

    @FXML
    private Label totalServicesLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Button confirmOrderBtn;

    @FXML
    private Button clearOrderBtn;

    // Поля для хранения текущих данных пользователя
    private String currentLogin;
    private String currentPassword;
    private String clientName;
    private String clientFamily;
    private String currentStoredPassword;
    private int clientId;

    private ObservableList<getService> selectedServices = FXCollections.observableArrayList(); // Список выбранных услуг
    private VBox selectedServicesContainer; // Контейнер для отображения выбранных услуг

    // Храним соответствие услуги и запчасти, а также исходное количество для восстановления
    private Map<Integer, Integer> serviceStorageMap = new HashMap<>();
    private Map<Integer, Integer> originalStorageQuantities = new HashMap<>();
    private Map<Integer, Integer> serviceQuantityMap = new HashMap<>(); // Сколько единиц запчасти нужно для услуги

    private final ObservableList<getService> listD = FXCollections.observableArrayList();

    // Устанавливаем данные пользователя после входа
    public void setUserCredentials(String login, String password) {
        this.currentLogin = login;
        this.currentPassword = password;
        loadUserData(); // Загрузка данных из БД
    }

    private void loadUserData() { // Загрузка данных из БД
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet queryResult = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();

            String query = "SELECT client_id, client_name, client_family, client_email, client_password FROM Client WHERE client_login = ?";
            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, currentLogin);
            queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                clientId = queryResult.getInt("client_id");
                clientName = queryResult.getString("client_name");
                clientFamily = queryResult.getString("client_family");
                String email = queryResult.getString("client_email");
                currentStoredPassword = queryResult.getString("client_password");

                // Обновляем интерфейс если данные инициализированы
                if (usernameMyAccount != null) {
                    usernameMyAccount.setText(clientName);
                }

                if (CurrentDateMyAccount != null) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    String formattedDate = currentDate.format(formatter);
                    CurrentDateMyAccount.setText(formattedDate);
                }

                if (prof_name_textarea != null) {
                    prof_name_textarea.setText(clientName);
                }

                if (prof_family_textarea != null) {
                    prof_family_textarea.setText(clientFamily);
                }

                if (prof_email_textarea != null) {
                    prof_email_textarea.setText(email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally { // Закрываем БД
            try {
                if (queryResult != null) queryResult.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connectDB != null) connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Переключание окон (через setVisible)
    public void switchForm(ActionEvent event){
        if (event.getSource() == OpenCatalog_btn){
            CatalogForm.setVisible(true);
            MyAccountForm.setVisible(false);
            currentPage.setText("Каталог");
        } else if (event.getSource() == OpenMyAccount_btn){
            CatalogForm.setVisible(false);
            MyAccountForm.setVisible(true);
            currentPage.setText("Профиль");
            loadUserData(); // Обновляем данные
            if (profileErrorMessage != null) {
                profileErrorMessage.setText("");
            }
        }
    }

    // Выход пользователя из аккаунта
    public void userLogout(){
        ((Stage) currentPage.getScene().getWindow()).close();
        openClientLogin();
    }

    // Обновляем данные профиля в БД
    public void profileUpdate() {
        if (profileErrorMessage != null) { // Сбрасываем сообщение об ошибке если есть
            profileErrorMessage.setText("");
        }

        if (profileSuccessMessage != null) { // Сбрасываем сообщение об успешном изменении
            profileSuccessMessage.setText("");
        }

        // Получаем данные из полей
        String newName = prof_name_textarea.getText().trim();
        String newFamily = prof_family_textarea.getText().trim();
        String newEmail = prof_email_textarea.getText().trim();
        String newPassword = prof_password_field.getText().trim();
        String newPasswordConf = prof_confirmPassword_field.getText().trim();

        if (newName.isEmpty() || newFamily.isEmpty() || newEmail.isEmpty()) { // Обязательные для заполнения поля
            profileErrorMessage.setText("Заполните все обязательные поля!");
            profileErrorMessage.setTextFill(Color.RED);
            return;
        }

        if (!newEmail.contains("@") || !newEmail.contains(".")) {
            profileErrorMessage.setText("Ошибка. Введите корректный email адрес!");
            profileErrorMessage.setTextFill(Color.RED);
            return;
        }

        if (!newPassword.isEmpty()) {
            if (newPassword.equals(currentStoredPassword)) {
                profileErrorMessage.setText("Пароль совпадает со старым!");
                profileErrorMessage.setTextFill(Color.RED);
                return;
            }

            if (newPassword.length() < 4) {
                profileErrorMessage.setText("Новый пароль должен содержать не менее 4 символов!");
                profileErrorMessage.setTextFill(Color.RED);
                return;
            }

            if (!newPassword.equals(newPasswordConf)) {
                profileErrorMessage.setText("Пароли не совпадают!");
                profileErrorMessage.setTextFill(Color.RED);
                return;
            }
        } else {
            newPassword = currentStoredPassword; // Оставляем текущий пароль, если его не меняли
        }
        currentStoredPassword = newPassword;

        // Обновляем данные в базе данных
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();

            String query = "UPDATE Client SET client_name = ?, client_family = ?, client_email = ?, client_password = ? WHERE client_login = ?";

            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newFamily);
            preparedStatement.setString(3, newEmail);
            preparedStatement.setString(4, newPassword);
            preparedStatement.setString(5, currentLogin);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Обновляем локальные данные
                clientName = newName;
                clientFamily = newFamily;

                if (usernameMyAccount != null) {
                    usernameMyAccount.setText(clientName);
                }

                if (profileSuccessMessage != null) {
                    profileSuccessMessage.setText("Профиль успешно обновлен!");
                    profileSuccessMessage.setTextFill(Color.GREEN);
                }

                // Очищаем поля паролей
                if (prof_password_field != null) {
                    prof_password_field.setText("");
                }

                if (prof_confirmPassword_field != null) {
                    prof_confirmPassword_field.setText("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            profileErrorMessage.setText("Ошибка: " + e.getMessage());
            profileErrorMessage.setTextFill(Color.RED);
        } finally { // Закрываем ресурсы
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connectDB != null) connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Получаем список всех услуг
    public ObservableList<getService> orderGetData(){
        ObservableList<getService> listData = FXCollections.observableArrayList();
        Connection connectDB = null;
        PreparedStatement statement = null;
        ResultSet queryResult = null;

        try {
            String sql = "SELECT service_id, service_name, storage_id, service_deadlines, service_price, Image FROM service";
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.prepareStatement(sql);
            queryResult = statement.executeQuery();

            while (queryResult.next()){ // Создаем объект услуги и добавляем в список
                getService getS = new getService(
                        queryResult.getInt("service_id"),
                        queryResult.getString("service_name"),
                        queryResult.getInt("storage_id"),
                        queryResult.getString("service_deadlines"),
                        queryResult.getDouble("service_price"),
                        queryResult.getString("Image")
                );
                listData.add(getS); // Сохраняем соответствие услуги и склада
                serviceStorageMap.put(queryResult.getInt("service_id"), queryResult.getInt("storage_id"));
                serviceQuantityMap.put(queryResult.getInt("service_id"), 1); // По умолчанию 1 запчасть на услугу
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally { // Закрываем ресурсы
            try {
                if (queryResult != null) queryResult.close();
                if (statement != null) statement.close();
                if (connectDB != null) connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

    // Получение текущего количества запчасти на складе
    private int getStorageQuantity(int storageId) {
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            String query = "SELECT storage_sum FROM Storage WHERE storage_id = ?";
            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setInt(1, storageId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("storage_sum");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally { // Закрываем ресурсы
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connectDB != null) connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    // Обновление количества запчасти на складе
    private boolean updateStorageQuantity(int storageId, int newQuantity /*Новое количество */) {
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            String query = "UPDATE Storage SET storage_sum = ? WHERE storage_id = ?";
            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setInt(2, storageId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connectDB != null) connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Получение следующего номера заказа
    private int getNextOrderNumber() {
        Connection connectDB = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            String query = "SELECT MAX(order_number) as max_num FROM orders";
            statement = connectDB.createStatement();
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int maxNum = resultSet.getInt("max_num");
                return maxNum + 1; // Увеличиваем максимальный номер на 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connectDB != null) connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 1001; // Начальный номер заказа, если таблица пуста
    }

    // Сохранение заказа в БД
    private boolean saveOrderToDatabase() {
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            connectDB.setAutoCommit(false); // Начинаем транзакцию

            int orderNum = getNextOrderNumber(); // Получаем номер заказа
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String orderDate = currentDate.format(formatter);

            // Расчет максимального срока выполнения
            int maxDays = 0;
            for (getService service : selectedServices) {
                String deadline = service.getServiceDeadlines();
                int days = Integer.parseInt(deadline.split(" ")[0]);
                if (days > maxDays) maxDays = days;
            }
            LocalDate endDate = currentDate.plusDays(maxDays);
            String orderEndDate = endDate.format(formatter);

            double totalPrice = 0;
            for (getService service : selectedServices) {
                totalPrice += service.getServicePrice();
            }

            // Для каждой услуги создаем отдельный заказ
            for (getService service : selectedServices) {
                String orderQuery = "INSERT INTO orders (client_id, order_number, service_id, order_status, orderDate, orderEndDate, totalPrice) VALUES (?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = connectDB.prepareStatement(orderQuery);
                preparedStatement.setInt(1, clientId);
                preparedStatement.setInt(2, orderNum);
                preparedStatement.setInt(3, service.getId()); // Добавляем service_id
                preparedStatement.setString(4, "В обработке"); // Начальный статус заказа
                preparedStatement.setString(5, orderDate);
                preparedStatement.setString(6, orderEndDate);
                preparedStatement.setDouble(7, service.getServicePrice()); // Цена за одну услугу
                preparedStatement.executeUpdate();
            }

            connectDB.commit(); // Подтверждаем транзакцию
            showTemporaryMessage("Заказ №" + orderNum + " подтвержден!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connectDB != null) connectDB.rollback(); // Откатываем изменения при ошибке
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            showTemporaryMessage("Ошибка при сохранении заказа: " + e.getMessage());
            return false;
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connectDB != null) connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Добавляем услугу в заказ и обновляем склад
    public void addServiceToOrder(getService service) {
        if (service == null) {
            System.err.println("Ошибка: service равен null");
            return;
        }

        // Проверяем, не добавлена ли уже эта услуга
        for (getService existing : selectedServices) {
            if (existing.getId().equals(service.getId())) {
                showTemporaryMessage("Эта услуга уже добавлена в заказ!");
                return;
            }
        }

        Integer storageId = serviceStorageMap.get(service.getId());
        if (storageId != null) {
            // Сохраняем исходное количество
            if (!originalStorageQuantities.containsKey(storageId)) {
                int currentQuantity = getStorageQuantity(storageId);
                originalStorageQuantities.put(storageId, currentQuantity);
            }

            // Проверяем наличие запчастей
            int currentQuantity = getStorageQuantity(storageId);
            int requiredQuantity = serviceQuantityMap.getOrDefault(service.getId(), 1);

            if (currentQuantity < requiredQuantity) {
                showTemporaryMessage("Недостаточно запчастей для услуги: " + service.getServiceName());
                return;
            }

            // Уменьшаем количество запчастей
            if (updateStorageQuantity(storageId, currentQuantity - requiredQuantity)) {
                selectedServices.add(service);
                updateSelectedServicesDisplay();
                showTemporaryMessage("Услуга добавлена: " + service.getServiceName());
            } else {
                showTemporaryMessage("Ошибка при обновлении склада");
            }
        } else { // Если услуга не требует запчастей, просто добавляем
            selectedServices.add(service);
            updateSelectedServicesDisplay();
        }
    }

    // Удаляет услугу из заказа и возвращает запчасти на склад
    private void removeServiceFromOrder(getService service) {
        if (service == null) return;

        // Восстанавливаем количество запчастей
        Integer storageId = serviceStorageMap.get(service.getId());
        if (storageId != null) {
            int currentQuantity = getStorageQuantity(storageId);
            int requiredQuantity = serviceQuantityMap.getOrDefault(service.getId(), 1);
            updateStorageQuantity(storageId, currentQuantity + requiredQuantity);
        }

        selectedServices.remove(service);
        updateSelectedServicesDisplay();
        showTemporaryMessage("Услуга удалена: " + service.getServiceName());
    }

    // Очищает весь заказ и возвращает все запчасти на склад
    @FXML
    private void clearOrder() {
        if (selectedServices.isEmpty()) {
            showTemporaryMessage("Заказ пуст");
            return;
        }

        // Восстанавливаем все запчасти, связанные с услугами
        for (getService service : selectedServices) {
            Integer storageId = serviceStorageMap.get(service.getId());
            if (storageId != null) {
                int currentQuantity = getStorageQuantity(storageId);
                int requiredQuantity = serviceQuantityMap.getOrDefault(service.getId(), 1);
                updateStorageQuantity(storageId, currentQuantity + requiredQuantity);
            }
        }

        selectedServices.clear();
        originalStorageQuantities.clear();
        updateSelectedServicesDisplay();
        showTemporaryMessage("Заказ очищен");
    }

    // Подтверждает заказ и сохраняет его в базу данных
    @FXML
    private void confirmOrder() {
        if (selectedServices.isEmpty()) {
            showTemporaryMessage("Добавьте услуги в заказ!");
            return;
        }

        if (saveOrderToDatabase()) {
            selectedServices.clear();
            originalStorageQuantities.clear();
            updateSelectedServicesDisplay();
        }
    }

    // Обновляет отображение выбранных услуг в интерфейсе, показывает список услуг, количество и стоимость
    private void updateSelectedServicesDisplay() {
        if (selectedServicesContainer == null) {
            initializeSelectedServicesContainer();
        }

        selectedServicesContainer.getChildren().clear();

        if (selectedServices.isEmpty()) { // Сообщение о пустой корзине
            Label emptyLabel = new Label("Нет выбранных услуг");
            emptyLabel.setStyle("-fx-text-fill: #999; -fx-font-size: 14px;");
            selectedServicesContainer.getChildren().add(emptyLabel);
            totalServicesLabel.setText("0");
            totalPriceLabel.setText("0 руб.");
            return;
        }

        double totalPrice = 0;
        int totalCount = 0;

        // Создаем строки для каждой услуги
        for (getService service : selectedServices) {
            if (service == null) continue;

            totalCount++;
            totalPrice += service.getServicePrice();

            HBox serviceRow = createServiceRow(service);
            selectedServicesContainer.getChildren().add(serviceRow);
        }

        // Обновляем итоговые значения
        totalServicesLabel.setText(String.valueOf(totalCount));
        totalPriceLabel.setText(String.format("%.2f руб.", totalPrice));
    }

    // Создаем строку для отображения услуги в корзине
    private HBox createServiceRow(getService service) {
        if (service == null) return new HBox();

        HBox row = new HBox(10);
        row.setStyle("-fx-padding: 5; -fx-background-color: #f5f5f5; -fx-background-radius: 5;");
        row.setPrefHeight(40);

        // Название услуги
        Label nameLabel = new Label(service.getServiceName());
        nameLabel.setStyle("-fx-font-size: 14px;");
        nameLabel.setPrefWidth(120);

        // Цена услуги
        Label priceLabel = new Label(String.format("%.2f руб.", service.getServicePrice()));
        priceLabel.setStyle("-fx-font-size: 14px;");
        priceLabel.setPrefWidth(80);

        // Кнопка удаления
        Button removeButton = new Button("x");
        removeButton.setStyle("-fx-background-color: #FF2400; -fx-text-fill: white; -fx-background-radius: 3; -fx-cursor: hand;");
        removeButton.setPrefWidth(30);
        removeButton.setOnAction(e -> removeServiceFromOrder(service));

        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        row.getChildren().addAll(nameLabel, priceLabel, removeButton);
        return row;
    }

    // Инициализируем контейнер для отображения выбранных услуг
    private void initializeSelectedServicesContainer() {
        selectedServicesContainer = new VBox(10);
        selectedServicesContainer.setPadding(new Insets(10));
        selectedServicesScrollPane.setContent(selectedServicesContainer);
        selectedServicesScrollPane.setFitToWidth(true);
    }

    // Временное всплывающее сообщение
    private void showTemporaryMessage(String message) {
        Label tempMessage = new Label(message);
        tempMessage.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 5;");

        if (selectedServicesContainer != null && selectedServicesContainer.getParent() != null) {
            selectedServicesContainer.getChildren().add(0, tempMessage);

            // Автоматически скрываем сообщение через 2 секунды
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> selectedServicesContainer.getChildren().remove(tempMessage));
            delay.play();
        }
    }

    // Отображение карточки услуг в сетке GridPane
    public void orderDisplayCard(){
        listD.clear();
        listD.addAll(orderGetData()); // Получаем все услуги из БД

        int row = 0;
        int column = 0;

        try {
            orderGridPane.getColumnConstraints().clear();
            orderGridPane.getRowConstraints().clear();
            orderGridPane.getChildren().clear();

            // Создаем карточки для каждой услуги
            for (int i = 0; i < listD.size(); i++){
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/org/project/autoserviceapp/client/ServiceCard.fxml"));
                StackPane pane = load.load();

                ServiceCardController serviceCC = load.getController();
                serviceCC.setData(listD.get(i));
                serviceCC.setClientController(this);

                // Размещаем карточки в 2 колонки
                if (column == 2){
                    column = 0;
                    row++;
                }

                orderGridPane.add(pane, column++, row);
                orderGridPane.setMinHeight(GridPane.USE_COMPUTED_SIZE);
                orderGridPane.setPrefHeight(GridPane.USE_COMPUTED_SIZE);
                orderGridPane.setMaxHeight(GridPane.BASELINE_OFFSET_SAME_AS_HEIGHT);

                orderGridPane.setMinWidth(GridPane.USE_COMPUTED_SIZE);
                orderGridPane.setPrefWidth(GridPane.USE_COMPUTED_SIZE);
                orderGridPane.setMaxWidth(GridPane.BASELINE_OFFSET_SAME_AS_HEIGHT);

                GridPane.setMargin(pane, new Insets(10));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void openClientLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/project/autoserviceapp/login/autoservice_loginClient.fxml"));
            Stage registrerStage = new Stage();
            registrerStage.setTitle("Вход");
            registrerStage.setScene(new Scene(root, 550, 400));
            registrerStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        // Устанавливаем текущую дату
        if (CurrentDateMyAccount != null) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedDate = currentDate.format(formatter);
            CurrentDateMyAccount.setText(formattedDate);
        }

        // Показываем каталог, скрываем профиль
        if (CatalogForm != null && MyAccountForm != null) {
            CatalogForm.setVisible(true);
            MyAccountForm.setVisible(false);
        }

        // Очищаем сообщения об ошибках и успехе
        if (profileErrorMessage != null) {
            profileErrorMessage.setText("");
        }

        if (profileSuccessMessage != null) {
            profileSuccessMessage.setText("");
        }

        initializeSelectedServicesContainer(); // Инициализируем контейнер корзины

        // Назначаем обработчики кнопок
        if (confirmOrderBtn != null) {
            confirmOrderBtn.setOnAction(event -> confirmOrder());
        }

        if (clearOrderBtn != null) {
            clearOrderBtn.setOnAction(event -> clearOrder());
        }

        orderDisplayCard(); // Отображаем карточки услуг
    }
}