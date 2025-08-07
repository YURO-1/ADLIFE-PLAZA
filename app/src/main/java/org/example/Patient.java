package org.example;

public class Patient {
    private String firstname;
    private String lastname;
    private String dob;
    private String status;

    public Patient(String firstname, String lastname, String dob) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.status = "Waiting"; // default
    }

    public Patient(String firstname, String lastname, String dob, String status) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.status = status;
    }

    // Getters
    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getDob() {
        return dob;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return firstname + " " + lastname;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    // CSV Conversion
    public String toCSV() {
        return firstname + "," + lastname + "," + dob + "," + status;
    }

    public static Patient fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 4) return null;
        return new Patient(parts[0], parts[1], parts[2], parts[3]);
    }

    @Override
    public String toString() {
        return getName() + " (" + dob + ")";
    }
}
