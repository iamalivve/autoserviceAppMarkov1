module autoserviceApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires java.sql;

    opens org.project.autoserviceapp;
    exports org.project.autoserviceapp;
}