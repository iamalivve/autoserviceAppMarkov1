module autoserviceApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens org.project.autoserviceapp;
    exports org.project.autoserviceapp;
    exports org.project.autoserviceapp.login;
    opens org.project.autoserviceapp.login;
    exports org.project.autoserviceapp.admin;
    opens org.project.autoserviceapp.admin;
    exports org.project.autoserviceapp.admin.DB;
    opens org.project.autoserviceapp.admin.DB;
    exports org.project.autoserviceapp.client;
    opens org.project.autoserviceapp.client;
}