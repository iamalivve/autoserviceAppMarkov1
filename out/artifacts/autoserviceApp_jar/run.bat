@echo off
set JAVA_FX_PATH="javafx-sdk-25.0.2\lib"
java --module-path %JAVA_FX_PATH% --add-modules javafx.controls,javafx.fxml -jar autoserviceApp.jar
pause
-jar autoserviceApp.jar