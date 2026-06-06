package org.project.autoserviceapp.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceCardController implements Initializable {

    // Назначаем обработчик для кнопки добавления
    @Override
    public void initialize(URL location, ResourceBundle resources){
        if (cardAddButton != null) {
            cardAddButton.setOnAction(event -> addToSelectedServices());
        }
    }

    @FXML
    private Button cardAddButton;

    @FXML
    private StackPane cardForm;

    @FXML
    private ImageView cardImageView;

    @FXML
    private Label cardPrice;

    @FXML
    private Label cardServiceName;

    private ClientController clientController; // Ссылка на родительский контроллер

    private getService getS; // Объект услуги

    private Image image;

    private Integer id;
    private String serviceName;
    private Integer storageId;
    private String serviceDeadlines;
    private Double servicePrice;

    private String path;

    // Устанавливаем данные услуги в карточку
    public void setData(getService getS){
        this.getS = getS;
        // Извлекаем данные из объекта
        id = getS.getId();
        serviceName = getS.getServiceName();
        storageId = getS.getStorageId();
        serviceDeadlines = getS.getServiceDeadlines();
        servicePrice = getS.getServicePrice();

        // Формируем путь к изображению
        path = "File:" + getS.getImage();

        // Обновляем текстовые метки
        cardServiceName.setText(serviceName);
        cardPrice.setText(servicePrice + " руб.");

        // Загружаем и устанавливаем изображение
        image = new Image(path, 200, 124, false, true);
        cardImageView.setImage(image);
    }


    // Устанавливаем ссылку на родительский контроллер
    public void setClientController(ClientController controller) {
        this.clientController = controller;
    }

    // Обработчик нажатия кнопки добавления услуги. Добавляет текущую услугу в заказ через родительский контроллер
    private void addToSelectedServices() {
        if (clientController != null && getS != null) {  // Проверяем, что getS не null
            clientController.addServiceToOrder(this.getS);
        }
    }
}
