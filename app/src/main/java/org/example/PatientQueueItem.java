package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PatientQueueItem {
    private final StringProperty patientName;
    private final StringProperty status;

    public PatientQueueItem(String patientName, String status) {
        this.patientName = new SimpleStringProperty(patientName);
        this.status = new SimpleStringProperty(status);
    }

    public String getPatientName() { return patientName.get(); }
    public StringProperty patientNameProperty() { return patientName; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
}

