package org.example;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class DoctorSocket {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public DoctorSocket(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /** Get the current patient queue (same as receptionist) */
    public void getPatientQueue(Consumer<String> lineHandler) throws IOException {
        out.println("GET_QUEUE");
        String line;
        while (!(line = in.readLine()).equals("END_OF_QUEUE")) {
            lineHandler.accept(line);
        }
    }

    /** Get details entered by receptionist for a specific patient */
    public String getPatientDetails(String patientName) throws IOException {
        out.println("GET_PATIENT_DETAILS");
        out.println(patientName);
        return in.readLine(); // Expect a CSV line with details
    }

    /** Save medical history for a patient */
    public void saveMedicalHistory(String patientName, String doctorId, String notes) {
        out.println("SAVE_MEDICAL_HISTORY");
        out.println(patientName + "," + doctorId + "," + notes);
    }

    /** Get medical history for a patient */
    public void getMedicalHistory(String patientName, Consumer<String> historyHandler) throws IOException {
        out.println("GET_MEDICAL_HISTORY");
        out.println(patientName);
        String line;
        while (!(line = in.readLine()).equals("END_OF_HISTORY")) {
            historyHandler.accept(line);
        }
    }

    /** Close the connection */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}


