package org.example;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.function.Consumer;

public class ReceptionistSocket {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public ReceptionistSocket(String host, int port) throws IOException {
        socket = new Socket(host, port);
        socket.setSoTimeout(5000); // Optional: 5s read timeout
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /** Request patient queue from the server */
    public void getPatientQueue(Consumer<String> lineHandler) throws IOException {
        out.println("GET_QUEUE");

        String line;
        while ((line = in.readLine()) != null) {
            if (line.equals("END_OF_QUEUE")) break;
            lineHandler.accept(line);
        }
    }

    /** Add a patient to the server's patient list */
    public String addPatient(String firstName, String lastName, String dob, String regDate) throws IOException {
        out.println("ADD_PATIENT");
        out.println(firstName + "," + lastName + "," + dob + "," + regDate);
        out.flush();

        String response = in.readLine();
        return (response != null) ? response : "No response from server.";
    }

    /** Search for a patient */
    public String searchPatient(String searchTerm) throws IOException {
        out.println("SEARCH_PATIENT");
        out.println(searchTerm);
        out.flush();

        try {
            return in.readLine();
        } catch (SocketTimeoutException e) {
            return "Search timed out.";
        }
    }

    /** Remove a patient from the queue */
    public String removeFromQueue(String patientName) throws IOException {
        out.println("REMOVE_FROM_QUEUE");
        out.println(patientName);
        out.flush();

        String response = in.readLine();
        return (response != null) ? response : "No response from server.";
    }

    /** Mark a patient as seen */
    public String markAsSeen(String patientName) throws IOException {
        out.println("MARK_AS_SEEN");
        out.println(patientName);
        out.flush();

        String response = in.readLine();
        return (response != null) ? response : "No response from server.";
    }

    /** Close the connection */
    public void close() throws IOException {
        try {
            out.println("DISCONNECT");
        } catch (Exception ignored) {}

        in.close();
        out.close();
        socket.close();
    }
}
