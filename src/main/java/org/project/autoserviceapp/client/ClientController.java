package org.project.autoserviceapp.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.project.autoserviceapp.DatabaseConnection;

import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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
    private Circle top_profile;

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

    private String currentLogin;
    private String currentPassword;
    private String clientName;
    private String clientFamily;

    private String currentStoredPassword; // Хранит текущий пароль из БД

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

    private ObservableList<getService> selectedServices = FXCollections.observableArrayList();
    private VBox selectedServicesContainer;

    public void setUserCredentials(String login, String password) {
        this.currentLogin = login;
        this.currentPassword = password;
        loadUserData();
    }

    private void loadUserData() {
        Connection connectDB = null;
        Statement statement = null;
        ResultSet queryResult = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();

            String query = "SELECT client_name, client_family, client_email, client_password FROM Client WHERE client_login = '" + currentLogin + "'";

            statement = connectDB.createStatement();
            queryResult = statement.executeQuery(query);

            if (queryResult.next()) {
                clientName = queryResult.getString("client_name");
                clientFamily = queryResult.getString("client_family");
                String email = queryResult.getString("client_email");
                currentStoredPassword = queryResult.getString("client_password");

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
        } finally {
            try {
                if (queryResult != null) queryResult.close();
                if (statement != null) statement.close();
                if (connectDB != null) connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUserName(String userName) {
        this.clientName = userName;
        if (usernameMyAccount != null) {
            usernameMyAccount.setText(userName);
        }
    }

    public void switchForm(ActionEvent event){
        if (event.getSource() == OpenCatalog_btn){
            CatalogForm.setVisible(true);
            MyAccountForm.setVisible(false);
            currentPage.setText("Каталог");
        } else if (event.getSource() == OpenMyAccount_btn){
            CatalogForm.setVisible(false);
            MyAccountForm.setVisible(true);
            currentPage.setText("Профиль");
            loadUserData();
            if (profileErrorMessage != null) {
                profileErrorMessage.setText("");
            }
        }
    }

    public void userLogout(){
        ((Stage) currentPage.getScene().getWindow()).close();
        openClientLogin();
    }

    public void profileUpdate() {
        // Очищаем предыдущее сообщение
        if (profileErrorMessage != null) {
            profileErrorMessage.setText("");
        }

        if (profileSuccessMessage != null) {
            profileSuccessMessage.setText("");
        }

        // Получаем новые значения из текстовых полей
        String newName = prof_name_textarea.getText().trim();
        String newFamily = prof_family_textarea.getText().trim();
        String newEmail = prof_email_textarea.getText().trim();
        String newPassword = prof_password_field.getText().trim();
        String newPasswordConf = prof_confirmPassword_field.getText().trim();

        // Проверка на пустые поля
        if (newName.isEmpty() || newFamily.isEmpty() || newEmail.isEmpty()) {
            profileErrorMessage.setText("Заполните все обязательные поля!");
            profileErrorMessage.setTextFill(Color.RED);
            return;
        }

        // Проверка email на валидность
        if (!newEmail.contains("@") || !newEmail.contains(".")) {
            profileErrorMessage.setText("Ошибка. Введите корректный email адрес!");
            profileErrorMessage.setTextFill(Color.RED);
            return;
        }

        if (!newPassword.isEmpty() || !newPassword.isEmpty()) {
            // Если одно из полей пароля заполнено, проверяем все условия

            // Проверка, что старый пароль совпадает с паролем в БД
            if (newPassword.equals(currentStoredPassword)) {
                profileErrorMessage.setText("Пароль совпадает со старым!");
                profileErrorMessage.setTextFill(Color.RED);
                return;
            }

            // Проверка сложности пароля (минимальная длина)
            if (newPassword.length() < 4) {
                profileErrorMessage.setText("Новый пароль должен содержать не менее 4 символов!");
                profileErrorMessage.setTextFill(Color.RED);
                return;
            }

            // Проверка, что пароли совпадают
            if (!newPassword.equals(newPasswordConf)) {
                profileErrorMessage.setText("Пароли не совпадают!");
                profileErrorMessage.setTextFill(Color.RED);
                return;
            }
        }else {
            // Если поля пароля пустые, используем старый пароль из БД
            newPassword = currentStoredPassword;
        }
        currentStoredPassword = newPassword;

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
                // Обновляем локальные переменные
                clientName = newName;
                clientFamily = newFamily;

                // Обновляем отображение имени в боковой панели
                if (usernameMyAccount != null) {
                    usernameMyAccount.setText(clientName);
                }

                // Показываем сообщение об успешном обновлении
                if (profileSuccessMessage != null) {
                    profileSuccessMessage.setText("Профиль успешно обновлен!");
                    profileSuccessMessage.setTextFill(Color.GREEN);
                }

                // Очищаем поле пароля после успешного обновления
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
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connectDB != null) connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private final ObservableList<getService> listD = FXCollections.observableArrayList();

    public ObservableList<getService> orderGetData(){

        ObservableList<getService> listData = FXCollections.observableArrayList();

        Connection connectDB = null;
        Statement statement = null;
        ResultSet queryResult = null;

        try {
            String sql = "SELECT * FROM service";

            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();

            statement = connectDB.createStatement();
            queryResult = statement.executeQuery(sql);

            getService getS;

            while (queryResult.next()){
                getS = new getService(queryResult.getInt("service_id"), queryResult.getString("service_name"),
                        queryResult.getInt("storage_id"), queryResult.getString("service_deadlines"),
                        queryResult.getDouble("service_price"), queryResult.getString("Image"));

                listData.add(getS);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listData;
    }

    public void orderDisplayCard(){

        listD.clear();
        listD.addAll(orderGetData());

        int row = 0;
        int column = 0;

        try {
            orderGridPane.getColumnConstraints().clear();
            orderGridPane.getRowConstraints().clear();
            orderGridPane.getChildren().clear();

            for (int i = 0; i < listD.size(); i++){

                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/org/project/autoserviceapp/client/ServiceCard.fxml"));
                StackPane pane = load.load();

                ServiceCardController serviceCC = load.getController();
                serviceCC.setData(listD.get(i));
                serviceCC.setClientController(this); // Передаем ссылку на ClientController

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

        }catch (Exception e){
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

    private void initializeSelectedServicesContainer() {
        selectedServicesContainer = new VBox(10);
        selectedServicesContainer.setPadding(new Insets(10));
        selectedServicesScrollPane.setContent(selectedServicesContainer);
        selectedServicesScrollPane.setFitToWidth(true);
    }

    // метод для добавления услуги в заказ:
    public void addServiceToOrder(getService service) {
        // Проверяем, не добавлена ли уже эта услуга
        for (getService existing : selectedServices) {
            if (existing.getId().equals(service.getId())) {
                showTemporaryMessage("Эта услуга уже добавлена в заказ!");
                return;
            }
        }

        selectedServices.add(service);
        updateSelectedServicesDisplay();
    }

    // метод для обновления отображения выбранных услуг:
    private void updateSelectedServicesDisplay() {
        if (selectedServicesContainer == null) {
            initializeSelectedServicesContainer();
        }

        selectedServicesContainer.getChildren().clear();


        double totalPrice = 0;
        int totalCount = 0;

        for (getService service : selectedServices) {
            totalCount++;
            totalPrice += service.getServicePrice();

            HBox serviceRow = createServiceRow(service);
            selectedServicesContainer.getChildren().add(serviceRow);
        }

        totalServicesLabel.setText(String.valueOf(totalCount));
        totalPriceLabel.setText(String.format("%.2f руб.", totalPrice));
    }

    // Создание строки для отображения услуги в списке
    private HBox createServiceRow(getService service) {
        HBox row = new HBox(10);
        row.setStyle("-fx-padding: 5; -fx-background-color: #f5f5f5; -fx-background-radius: 5;");
        row.setPrefHeight(40);

        Label nameLabel = new Label(service.getServiceName());
        nameLabel.setStyle("-fx-font-size: 14px;");
        nameLabel.setPrefWidth(120);

        Label priceLabel = new Label(String.format("%.2f руб.", service.getServicePrice()));
        priceLabel.setStyle("-fx-font-size: 14px;");
        priceLabel.setPrefWidth(80);

        Button removeButton = new Button("");
        removeButton.setStyle("-fx-background-color: #FF2400; -fx-text-fill: white; -fx-background-radius: 3; -fx-cursor: hand;");
        removeButton.setPrefWidth(30);
        removeButton.setOnAction(e -> removeServiceFromOrder(service));

        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        row.getChildren().addAll(nameLabel, priceLabel, removeButton);
        return row;
    }

    // Удаление услуги из заказа
    private void removeServiceFromOrder(getService service) {
        selectedServices.remove(service);
        updateSelectedServicesDisplay();
    }

    // Очистка всего заказа
    @FXML
    private void clearOrder() {
        selectedServices.clear();
        updateSelectedServicesDisplay();
    }

    // Подтверждение заказа
    @FXML
    private void confirmOrder() {
        if (selectedServices.isEmpty()) {
            return;
        }

        // логику сохранения заказа в базу данных
        showTemporaryMessage("Заказ подтвержден!");
    }

    private void showTemporaryMessage(String message) {
        Label tempMessage = new Label(message);
        tempMessage.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 5;");

        if (selectedServicesContainer != null && selectedServicesContainer.getParent() != null) {
            // Добавляем сообщение в контейнер на время
            selectedServicesContainer.getChildren().add(0, tempMessage);

            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
            delay.setOnFinished(event -> selectedServicesContainer.getChildren().remove(tempMessage));
            delay.play();
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources){
        if (CurrentDateMyAccount != null) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedDate = currentDate.format(formatter);
            CurrentDateMyAccount.setText(formattedDate);
        }

        if (CatalogForm != null && MyAccountForm != null) {
            CatalogForm.setVisible(true);
            MyAccountForm.setVisible(false);
        }

        if (profileErrorMessage != null) {
            profileErrorMessage.setText("");
        }

        if (profileSuccessMessage != null) {
            profileSuccessMessage.setText("");
        }

        initializeSelectedServicesContainer();

        // Настройка кнопок заказа
        if (confirmOrderBtn != null) {
            confirmOrderBtn.setOnAction(event -> confirmOrder());
        }

        if (clearOrderBtn != null) {
            clearOrderBtn.setOnAction(event -> clearOrder());
        }

        orderDisplayCard();
    }
}