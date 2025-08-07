package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty dob;
    private final StringProperty status;

    public Patient(String id, String name, String dob, String status) {
        this(id, name, dob);
    }

    public Patient(String id, String name, String dob) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.dob = new SimpleStringProperty(dob);
        this.status = new SimpleStringProperty("Waiting");
    }

    // ID
    public String getId() {
        return id.get();
    }
    public void setId(String value) {
        id.set(value);
    }
    public StringProperty idProperty() {
        return id;
    }

    // Name
    public String getName() {
        return name.get();
    }
    public void setName(String value) {
        name.set(value);
    }
    public StringProperty nameProperty() {
        return name;
    }

    // DOB
    public String getDob() {
        return dob.get();
    }
    public void setDob(String value) {
        dob.set(value);
    }
    public StringProperty dobProperty() {
        return dob;
    }

    // Status
    public String getStatus() {
        return status.get();
    }
    public void setStatus(String value) {
        status.set(value);
    }
    public StringProperty statusProperty() {
        return status;
    }
}
