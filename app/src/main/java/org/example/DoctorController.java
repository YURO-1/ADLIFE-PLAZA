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
    private final DoctorSocket doctorSocket = new DoctorSocket();

    @FXML
    public void initialize() {
        // Configure table columns
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load initial queue data
        loadQueueData();

        queueTable.setItems(patientQueue);

        // Handle patient selection
        queueTable.setOnMouseClicked((MouseEvent event) -> {
            currentPatient = queueTable.getSelectionModel().getSelectedItem();
            if (currentPatient != null) {
                patientDetailsLabel.setText("Patient: " + currentPatient.getName() + " | DOB: " + currentPatient.getDob());
                loadMedicalHistory(currentPatient.getName());
            }
        });

        // Set welcome message
        welcomeLabel.setText("Welcome, Dr. Adrian");
    }

    private void loadQueueData() {
        try {
            String queueData = doctorSocket.getQueue();
            if (queueData != null && !queueData.equals("ERROR")) {
                patientQueue.clear();
                String[] patients = queueData.split(";;");
                for (String patient : patients) {
                    if (!patient.trim().isEmpty()) {
                        String[] parts = patient.split(",");
                        if (parts.length >= 2) {
                            String name = parts[0];
                            String status = parts[1];
                            String dob = parts.length > 2 ? parts[2] : "Unknown";
                            patientQueue.add(new Patient(name, dob, status));
                        }
                    }
                }
            } else {
                // Load dummy data if server is not available
                loadDummyData();
            }
        } catch (Exception e) {
            System.err.println("Error loading queue data: " + e.getMessage());
            loadDummyData();
        }
    }

    private void loadDummyData() {
        patientQueue.addAll(
            new Patient("John Doe", "1990-05-12", "Waiting"),
            new Patient("Jane Smith", "1985-09-22", "Waiting"),
            new Patient("Mark Thomas", "2000-03-17", "Waiting")
        );
    }

    private void loadMedicalHistory(String patientName) {
        try {
            String history = doctorSocket.getMedicalHistory(patientName);
            if (history != null && !history.equals("ERROR")) {
                medicalHistoryArea.setText(history);
            } else {
                medicalHistoryArea.setText("No medical history found for " + patientName);
            }
        } catch (Exception e) {
            System.err.println("Error loading medical history: " + e.getMessage());
            medicalHistoryArea.setText("Error loading medical history");
        }
    }

    @FXML
    private void handleBeginConsultation() {
        currentPatient = queueTable.getSelectionModel().getSelectedItem();
        if (currentPatient == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a patient to begin consultation.");
            return;
        }

        try {
            boolean success = doctorSocket.beginConsultation(currentPatient.getName());
            if (success) {
                currentPatient.setStatus("In Consultation");
                queueTable.refresh();
                patientDetailsLabel.setText("Patient: " + currentPatient.getName() + " | DOB: " + currentPatient.getDob());
                showAlert(Alert.AlertType.INFORMATION, "Consultation started for " + currentPatient.getName());
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to start consultation.");
            }
        } catch (Exception e) {
            System.err.println("Error starting consultation: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error starting consultation: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveHistory() {
        if (currentPatient == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a patient first.");
            return;
        }

        String history = medicalHistoryArea.getText().trim();
        if (history.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter medical history before saving.");
            return;
        }

        try {
            boolean success = doctorSocket.saveMedicalHistory(currentPatient.getName(), history);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Medical history saved successfully for " + currentPatient.getName());
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to save medical history.");
            }
        } catch (Exception e) {
            System.err.println("Error saving medical history: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error saving medical history: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ADLIFE PLAZA");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");

        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            System.exit(0);
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("ADLIFE PLAZA");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
