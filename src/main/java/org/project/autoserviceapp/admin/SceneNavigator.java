package org.project.autoserviceapp.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneNavigator {
    //Получаем имя администратора
    private static String adminName;
    //Устанавливаем имя администратора в приветствии
    public static void setAdminName(String name) {
        adminName = name;
    }
    //Главная страница
    public static void goToHome(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/org/project/autoserviceapp/admin/admin_home.fxml"));
            Parent root = loader.load();

            AdminHomeController controller = loader.getController();
            controller.setAdminName(adminName);
            controller.setPrimaryStage(stage);

            stage.setScene(new Scene(root, 1000, 600));
            stage.setTitle("AVTO67 - Главное меню");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Список клиентов
    public static void goToClients(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/org/project/autoserviceapp/admin/admin_clients.fxml"));
            Parent root = loader.load();

            AdminClientsController controller = loader.getController();
            controller.setAdminName(adminName);
            controller.setPrimaryStage(stage);

            stage.setScene(new Scene(root, 1000, 600));
            stage.setTitle("AVTO67 - Клиенты");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Список сотрудников
    public static void goToWorkers(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/org/project/autoserviceapp/admin/admin_workers.fxml"));
            Parent root = loader.load();

            AdminWorkersController controller = loader.getController();
            controller.setAdminName(adminName);
            controller.setPrimaryStage(stage);

            stage.setScene(new Scene(root, 1000, 600));
            stage.setTitle("AVTO67 - Сотрудники");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Активные заказы
    public static void goToActiveOrders(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/org/project/autoserviceapp/admin/admin_activeOrders.fxml"));
            Parent root = loader.load();

            AdminActiveOrdersController controller = loader.getController();
            controller.setAdminName(adminName);
            controller.setPrimaryStage(stage);

            stage.setScene(new Scene(root, 1000, 600));
            stage.setTitle("AVTO67 - Активные заказы");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Архив заказов
    public static void goToHistoryOrders(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/org/project/autoserviceapp/admin/admin_historyOrders.fxml"));
            Parent root = loader.load();

            AdminHistoryOrdersController controller = loader.getController();
            controller.setAdminName(adminName);
            controller.setPrimaryStage(stage);

            stage.setScene(new Scene(root, 1000, 600));
            stage.setTitle("AVTO67 - Архив заказов");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Склад запчастей
    public static void goToStorage(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/org/project/autoserviceapp/admin/admin_storage.fxml"));
            Parent root = loader.load();

            AdminStorageController controller = loader.getController();
            controller.setAdminName(adminName);
            controller.setPrimaryStage(stage);

            stage.setScene(new Scene(root, 1000, 600));
            stage.setTitle("AVTO67 - Склад запчастей");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Логин
    public static void goToLogin(Stage stage) {
        try {
            Parent root = FXMLLoader.load(SceneNavigator.class.getResource("/org/project/autoserviceapp/login/autoservice_login.fxml"));
            stage.setScene(new Scene(root, 550, 400));
            stage.setTitle("AVTO67 - Вход");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}