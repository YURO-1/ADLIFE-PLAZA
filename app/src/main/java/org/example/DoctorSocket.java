package org.example;

import java.io.*;
import java.net.Socket;

public class DoctorSocket {
    private static final String HOST = "localhost";
    private static final int PORT = 5555;

    public String getQueue() {
        return sendRequest("GET_QUEUE");
    }

    public boolean updateQueue(String queueData) {
        String response = sendRequest("UPDATE_QUEUE::" + queueData);
        return "QUEUE_UPDATED".equals(response);
    }

    public boolean saveMedicalHistory(String patientName, String history) {
        String data = patientName + "::" + history;
        String response = sendRequest("SAVE_HISTORY::" + data);
        return "HISTORY_SAVED".equals(response);
    }

    public boolean beginConsultation(String patientName) {
        String response = sendRequest("BEGIN_CONSULTATION::" + patientName);
        return "CONSULTATION_STARTED".equals(response);
    }

    public String getMedicalHistory(String patientName) {
        return sendRequest("GET_HISTORY::" + patientName);
    }

    private String sendRequest(String request) {
        try (
            Socket socket = new Socket(HOST, PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            System.out.println("Sending request: " + request);
            writer.write(request);
            writer.newLine();
            writer.flush();

            String response = reader.readLine();
            System.out.println("Received response: " + response);
            return response;
        } catch (IOException e) {
            System.err.println("Error in socket communication: " + e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }
    }
}
