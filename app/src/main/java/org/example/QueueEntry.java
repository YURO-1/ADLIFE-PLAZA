/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example;

/**
 *
 * @author yuroa
 */
public class QueueEntry {
    private String patientName;
    private String status;

    public QueueEntry(String patientName, String status) {
        this.patientName = patientName;
        this.status = status;
    }

    public String getPatientName() { return patientName; }
    public String getStatus() { return status; }
}

