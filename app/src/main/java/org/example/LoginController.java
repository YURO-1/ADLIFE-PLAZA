package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Change these to your server host/port
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter username and password.");
            return;
        }

      try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

      // Send username and password
      out.println(username);
      out.println(password);

      // Log confirmation
      System.out.println("Sent username and password to login server");

            if (username.startsWith("#rcp")) {
                loadDashboard("/FXML/ReceptionistDashboard.fxml", "Receptionist Dashboard");
            } else if (username.startsWith("#dct")) {
                loadDashboard("/FXML/DoctorDashboard.fxml", "Doctor Dashboard");
            } else {
                showAlert("Error", "Invalid username format. Must start with #rcp or #dct.");
            }

        } catch (IOException e) {
            showAlert("Error", "Could not connect to server.");
            e.printStackTrace();
        }
    }

    private void loadDashboard(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle(title);
        stage.show();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
