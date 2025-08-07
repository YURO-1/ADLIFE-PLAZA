package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.ReceptionistSocket;

public class ReceptionistController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker dobPicker;
    @FXML private DatePicker regDatePicker;
    @FXML private Button registerButton;

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Label searchResultLabel;

    @FXML private TableView<PatientQueueItem> queueTable;
    @FXML private TableColumn<PatientQueueItem, String> patientNameColumn;
    @FXML private TableColumn<PatientQueueItem, String> statusColumn;
    @FXML private Button removeButton;
    @FXML private Button markSeenButton;

    private final ObservableList<PatientQueueItem> queueData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Initialize with dummy or empty data
        queueTable.setItems(queueData);
    }

    @FXML
    private void handleRegisterPatient() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String dob = (dobPicker.getValue() != null) ? dobPicker.getValue().toString() : "";
        String regDate = (regDatePicker.getValue() != null) ? regDatePicker.getValue().toString() : "";

        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || regDate.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required.");
            return;
        }

        ReceptionistSocket socketClient = new ReceptionistSocket("localhost", 6000);
        boolean success = socketClient.registerPatient(firstName, lastName, dob, regDate);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Patient registered and added to queue.");
            queueData.add(new PatientQueueItem(firstName + " " + lastName, "Waiting"));
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration failed.");
        }
    }

    @FXML
    private void handleSearchPatient() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            searchResultLabel.setText("Please enter a name.");
            return;
        }

        ReceptionistSocket socketClient = new ReceptionistSocket("localhost", 6000);
        String result = socketClient.searchPatient(keyword);

        searchResultLabel.setText(result);
    }

    @FXML
    private void handleRemoveFromQueue() {
        PatientQueueItem selected = queueTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            queueData.remove(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Please select a patient to remove.");
        }
    }

    @FXML
    private void handleMarkAsSeen() {
        PatientQueueItem selected = queueTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("Seen");
            queueTable.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Please select a patient to mark as seen.");
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        dobPicker.setValue(null);
        regDatePicker.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class to represent each row in the queue table
    public static class PatientQueueItem {
        private final String name;
        private String status;

        public PatientQueueItem(String name, String status) {
            this.name = name;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
