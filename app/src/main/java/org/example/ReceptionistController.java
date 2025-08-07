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
    private final ReceptionistSocket socketClient = new ReceptionistSocket("localhost", 6000);

    @FXML
    public void initialize() {
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Initialize with dummy data for demonstration
        queueData.add(new PatientQueueItem("John Doe", "Waiting"));
        queueData.add(new PatientQueueItem("Jane Smith", "In Consultation"));
        queueData.add(new PatientQueueItem("Bob Johnson", "Seen"));
        
        queueTable.setItems(queueData);
        
        // Set default dates
        regDatePicker.setValue(java.time.LocalDate.now());
    }

    @FXML
    private void handleRegisterPatient() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String dob = (dobPicker.getValue() != null) ? dobPicker.getValue().toString() : "";
        String regDate = (regDatePicker.getValue() != null) ? regDatePicker.getValue().toString() : "";

        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all required fields (First Name, Last Name, Date of Birth).");
            return;
        }

        boolean success = socketClient.registerPatient(firstName, lastName, dob, regDate);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Patient registered successfully and added to queue.");
            queueData.add(new PatientQueueItem(firstName + " " + lastName, "Waiting"));
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration failed. Please try again.");
        }
    }

    @FXML
    private void handleSearchPatient() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            searchResultLabel.setText("Please enter a name or keyword to search.");
            return;
        }

        String result = socketClient.searchPatient(keyword);
        searchResultLabel.setText(result);
    }

    @FXML
    private void handleRemoveFromQueue() {
        PatientQueueItem selected = queueTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a patient to remove from the queue.");
            return;
        }

        String patientName = selected.getPatientName();
        boolean success = socketClient.removeFromQueue(patientName);
        
        if (success) {
            queueData.remove(selected);
            showAlert(Alert.AlertType.INFORMATION, "Patient removed from queue successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to remove patient from queue.");
        }
    }

    @FXML
    private void handleMarkAsSeen() {
        PatientQueueItem selected = queueTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a patient to mark as seen.");
            return;
        }

        String patientName = selected.getPatientName();
        boolean success = socketClient.markAsSeen(patientName);
        
        if (success) {
            selected.setStatus("Seen");
            queueTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Patient marked as seen successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to mark patient as seen.");
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        dobPicker.setValue(null);
        regDatePicker.setValue(java.time.LocalDate.now());
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("ADLIFE PLAZA");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
