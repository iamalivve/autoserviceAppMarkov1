package org.project.autoserviceapp.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.project.autoserviceapp.DatabaseConnection;
import org.project.autoserviceapp.admin.DB.Client;
import org.project.autoserviceapp.admin.DB.Order;
import org.project.autoserviceapp.admin.DB.Storage;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminHomeController implements Initializable {

    //Статистика
    @FXML private Label clientsCountLabel;
    @FXML private Label activeOrdersCountLabel;
    @FXML private Label storageCountLabel;

    //График
    @FXML private BarChart<String, Number> salesChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

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
        loadCounts();
        loadChart();
    }

    //Предзагрузка
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File brandingFile = new File("images/User.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        loadCounts();
        loadChart();
    }

    //Получение статистики
    private void loadCounts() {
        clientsCountLabel.setText(String.valueOf(Client.getAll().size()));
        activeOrdersCountLabel.setText(String.valueOf(Order.getActiveOrders().size()));
        loadStorageTotal();
    }
    private void loadStorageTotal() {
        String sql = "SELECT SUM(storage_sum) FROM Storage";
        DatabaseConnection dbConn = new DatabaseConnection();
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                storageCountLabel.setText(String.valueOf(rs.getInt(1)));
            } else {
                storageCountLabel.setText("0");
            }
        } catch (SQLException e) {
            storageCountLabel.setText("0");
        }
    }

    //Загрузка графика
    private void loadChart() {
        Map<String, Double> salesData = Order.getMonthlySalesData();

        //Очистка старых данных
        salesChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Выручка по месяцам");

        //Добавление на график данных
        for (Map.Entry<String, Double> entry : salesData.entrySet()) {
            String month = entry.getKey();
            Double amount = entry.getValue();

            if (month != null && !month.isEmpty()) {
                series.getData().add(new XYChart.Data<>(month, amount));
            }
        }
        salesChart.getData().add(series);

        //Оси графика
        xAxis.setLabel("Месяц");
        yAxis.setLabel("Сумма (руб)");
        salesChart.setTitle("Выручка от завершённых заказов");
        salesChart.setAnimated(false);
        //Обновление графика
        salesChart.setVisible(false);
        salesChart.setVisible(true);
    }

    //Методы реализации смены Scene по нажатию кнопки
    @FXML private void handleHouseButton() {loadCounts();loadChart();}
    @FXML private void handleClientsButton(ActionEvent event) {SceneNavigator.goToClients(primaryStage);}
    @FXML private void handleWorkersButton(ActionEvent event) {SceneNavigator.goToWorkers(primaryStage);}
    @FXML private void handleServicesButton(ActionEvent event) {SceneNavigator.goToServices(primaryStage);}
    @FXML private void handleActiveOrdersButton(ActionEvent event) {SceneNavigator.goToActiveOrders(primaryStage);}
    @FXML private void handleHistoryOrdersButton(ActionEvent event) {SceneNavigator.goToHistoryOrders(primaryStage);}
    @FXML private void handleStorageButton(ActionEvent event) {SceneNavigator.goToStorage(primaryStage);}
    @FXML public void actionExitButton(ActionEvent event) {SceneNavigator.goToLogin(primaryStage);}
}