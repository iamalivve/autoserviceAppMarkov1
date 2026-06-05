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

    private ClientController clientController;

    private getService getS;

    private Image image;

    private Integer id;
    private String serviceName;
    private Integer storageId;
    private String serviceDeadlines;
    private Double servicePrice;

    private String path;

    public void setData(getService getS){
        this.getS = getS;
        id = getS.getId();
        serviceName = getS.getServiceName();
        storageId = getS.getStorageId();
        serviceDeadlines = getS.getServiceDeadlines();
        servicePrice = getS.getServicePrice();

        path = "File:" + getS.getImage();

        cardServiceName.setText(serviceName);
        cardPrice.setText(servicePrice + " руб.");

        image = new Image(path, 200, 124, false, true);
        cardImageView.setImage(image);
    }

    public void setClientController(ClientController controller) {
        this.clientController = controller;
    }

    private void addToSelectedServices() {
        if (clientController != null && getS != null) {  // Проверяем, что getS не null
            clientController.addServiceToOrder(this.getS);
        }
    }
}
