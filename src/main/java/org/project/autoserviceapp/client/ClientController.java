package org.project.autoserviceapp.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.project.autoserviceapp.DatabaseConnection;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

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
    private TextField prof_password_textarea;

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
                String password = queryResult.getString("client_password");

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
        String newPassword = prof_password_textarea.getText().trim();

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
                if (prof_password_textarea != null) {
                    prof_password_textarea.setText("");
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
    }
}