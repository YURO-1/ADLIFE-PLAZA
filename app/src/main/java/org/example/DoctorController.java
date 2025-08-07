package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class DoctorController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Patient> queueTable;

    @FXML
    private TableColumn<Patient, String> patientNameColumn;

    @FXML
    private TableColumn<Patient, String> dobColumn;

    @FXML
    private TableColumn<Patient, String> statusColumn;

    @FXML
    private Label patientDetailsLabel;

    @FXML
    private TextArea medicalHistoryArea;

    @FXML
    private Button beginConsultationButton;

    @FXML
    private Button saveHistoryButton;

    private ObservableList<Patient> patientQueue = FXCollections.observableArrayList();

    private Patient currentPatient = null;

    @FXML
    public void initialize() {
        // Configure table columns
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load dummy data (replace this with CSV/socket loading)
        patientQueue.addAll(
            new Patient("John Doe", "1990-05-12", "Waiting"),
            new Patient("Jane Smith", "1985-09-22", "Waiting"),
            new Patient("Mark Thomas", "2000-03-17", "Waiting")
        );

        queueTable.setItems(patientQueue);

        // Handle patient selection
        queueTable.setOnMouseClicked((MouseEvent event) -> {
            currentPatient = queueTable.getSelectionModel().getSelectedItem();
            if (currentPatient != null) {
                patientDetailsLabel.setText("Patient: " + currentPatient.getName() + " | DOB: " + currentPatient.getDob());
                medicalHistoryArea.setText(""); // Optionally load history here
            }
        });
    }

    @FXML
    private void handleBeginConsultation() {
        currentPatient = queueTable.getSelectionModel().getSelectedItem();
        if (currentPatient != null) {
            currentPatient.setStatus("In Consultation");
            queueTable.refresh();
            patientDetailsLabel.setText("Patient: " + currentPatient.getName() + " | DOB: " + currentPatient.getDob());
        } else {
            showAlert("Please select a patient to begin consultation.");
        }
    }

    @FXML
    private void handleSaveHistory() {
        if (currentPatient != null && !medicalHistoryArea.getText().isEmpty()) {
            // You'd typically save this to a file or server
            System.out.println("Saving history for " + currentPatient.getName() + ": " + medicalHistoryArea.getText());
            showAlert("Medical history saved.");
        } else {
            showAlert("Select a patient and enter some medical history first.");
        }
    }

    @FXML
    private void handleLogout() {
        System.exit(0);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
