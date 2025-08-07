package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ReceptionistController {

    @FXML private TableView<String[]> queueTable;
    @FXML private TableColumn<String[], String> firstNameColumn;
    @FXML private TableColumn<String[], String> lastNameColumn;
    @FXML private TableColumn<String[], String> dobColumn;
    @FXML private TableColumn<String[], String> regDateColumn;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker dobPicker;
    @FXML private DatePicker regDatePicker;

    @FXML private TextField searchField;
    @FXML private Label searchResultLabel;

    private ReceptionistSocket client;

    public void initClient(ReceptionistSocket client) throws IOException {
        this.client = client; // Provided by login
        setupTable();
        refreshQueue();
    }

    private void setupTable() {
        firstNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
        lastNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
        dobColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
        regDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[3]));
    }

    // ------------------ Register Patient ------------------
    @FXML
    private void handleRegisterPatient() {
        String fn = firstNameField.getText();
        String ln = lastNameField.getText();

        String dob = (dobPicker.getValue() != null)
                ? dobPicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : "";

        String reg = (regDatePicker.getValue() != null)
                ? regDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : "";

        if (fn.isEmpty() || ln.isEmpty() || dob.isEmpty() || reg.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        try {
            client.addPatient(fn, ln, dob, reg);
            showAlert("Success", "Patient registered successfully.");
            refreshQueue();
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not register patient.");
        }
    }

    // ------------------ Search Patient ------------------
    @FXML
    private void handleSearchPatient() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            searchResultLabel.setText("Please enter a name or DOB to search.");
            return;
        }

        try {
            String result = client.searchPatient(searchTerm);
            if (result == null || result.isEmpty()) {
                searchResultLabel.setText("No matching patient found.");
            } else {
                searchResultLabel.setText(result);
            }
        } catch (IOException e) {
            searchResultLabel.setText("Error searching patient.");
        }
    }

    // ------------------ Remove from Queue ------------------
    @FXML
    private void handleRemoveFromQueue() {
        String[] selected = queueTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a patient from the queue.");
            return;
        }

        try {
            String patientName = selected[0] + " " + selected[1];
            client.removeFromQueue(patientName);
            showAlert("Success", "Patient removed from queue.");
            refreshQueue();
        } catch (IOException e) {
            showAlert("Error", "Could not remove patient from queue.");
        }
    }

    // ------------------ Mark as Seen ------------------
    @FXML
    private void handleMarkAsSeen() {
        String[] selected = queueTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a patient from the queue.");
            return;
        }

        try {
            String patientName = selected[0] + " " + selected[1];
            client.markAsSeen(patientName);
            showAlert("Success", "Patient marked as seen.");
            refreshQueue();
        } catch (IOException e) {
            showAlert("Error", "Could not mark patient as seen.");
        }
    }

    // ------------------ Refresh Queue ------------------
    @FXML
    private void handleRefreshQueue() {
        refreshQueue();
    }

    private void refreshQueue() {
        ObservableList<String[]> patients = FXCollections.observableArrayList();
        try {
            client.getPatientQueue((line) -> {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    patients.add(parts);
                }
            });
        } catch (IOException e) {
            showAlert("Error", "Could not refresh patient queue.");
        }
        queueTable.setItems(patients);
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        dobPicker.setValue(null);
        regDatePicker.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    void setReceptionistInfo(String username) {
        System.out.println("Receptionist logged in: " + username);
    }
}

