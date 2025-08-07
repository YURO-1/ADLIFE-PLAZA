package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.DoctorSocket;

import java.io.IOException;

public class DoctorController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Patient> queueTable;
    @FXML private TableColumn<Patient, String> patientNameColumn;
    @FXML private TableColumn<Patient, String> dobColumn;
    @FXML private TableColumn<Patient, String> statusColumn;
    @FXML private Label patientDetailsLabel;
    @FXML private TextArea medicalHistoryArea;

    private DoctorSocket client;
    public void initClient(DoctorSocket client) {
    this.client = client;
}

    private String doctorId;
    private Patient selectedPatient;

    public void initialize() {
        // Set up table columns
        patientNameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        dobColumn.setCellValueFactory(data -> data.getValue().dobProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
    }

    public void setDoctorInfo(String doctorId) {
        this.doctorId = doctorId;
        welcomeLabel.setText("Welcome " + doctorId);

        try {
            client = new DoctorSocket("localhost", 12345); // Connect to shared server
            loadQueue();
        } catch (IOException e) {
            showAlert("Connection Error", "Could not connect to server.");
        }
    }

    /** Load queue from server */
    private void loadQueue() {
        queueTable.getItems().clear();
        try {
            client.getPatientQueue(line -> {
                // line = first_name,last_name,dob,registration_date
                String[] cols = line.split(",");
                if (cols.length >= 3) {
                    String fullName = cols[0] + " " + cols[1];
                    String dob = cols[2];
                    queueTable.getItems().add(new Patient(fullName, dob, "Waiting"));
                }
            });
        } catch (IOException e) {
            showAlert("Error", "Could not load patient queue.");
        }
    }

    @FXML
    private void handleBeginConsultation() {
        selectedPatient = queueTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showAlert("No patient selected", "Please select a patient first.");
            return;
        }

        try {
            // Load patient details
            String details = client.getPatientDetails(selectedPatient.getName());
            patientDetailsLabel.setText(details);

            // Load history if allowed
            client.getMedicalHistory(selectedPatient.getName(), historyLine -> {
                medicalHistoryArea.appendText(historyLine + "\n");
            });

        } catch (IOException e) {
            showAlert("Error", "Could not retrieve patient details/history.");
        }
    }

    @FXML
    private void handleSaveHistory() {
        if (selectedPatient == null) {
            showAlert("No patient selected", "Please select a patient first.");
            return;
        }
        String history = medicalHistoryArea.getText();
        client.saveMedicalHistory(selectedPatient.getName(), doctorId, history);
        showAlert("Saved", "Medical history saved successfully.");
    }

    @FXML
    private void handleLogout() {
        try {
            if (client != null) client.close();
        } catch (IOException ignored) {}
        ((Stage) welcomeLabel.getScene().getWindow()).close();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}