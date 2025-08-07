package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DoctorDashboard extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/DoctorDashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Doctor Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true); // Open maximized so it readjusts automatically
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
