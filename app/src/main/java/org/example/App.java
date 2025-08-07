package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/LoginScreenUI.fxml"));
        Scene scene = new Scene(root, 900, 600); // Default size

        primaryStage.setTitle("ADLIFE PLAZA");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true); // Allow resizing
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


        
