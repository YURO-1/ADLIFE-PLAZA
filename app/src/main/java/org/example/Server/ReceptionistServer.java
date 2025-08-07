package org.example.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.ArrayList;

public class ReceptionistServer {
    private static final int PORT = 6000;
    private static final String PATIENTS_CSV = "app/src/main/resources/CSV/patients.csv";
    private static final String QUEUE_CSV = "app/src/main/resources/CSV/doctor_queue.csv";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Receptionist Server running on port " + PORT);
            var pool = Executors.newCachedThreadPool();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            String command = in.readLine();
            System.out.println("Received command: " + command);

            switch (command) {
                case "register_patient":
                    handleRegisterPatient(in, out);
                    break;
                case "search_patient":
                    handleSearchPatient(in, out);
                    break;
                case "remove_from_queue":
                    handleRemoveFromQueue(in, out);
                    break;
                case "mark_as_seen":
                    handleMarkAsSeen(in, out);
                    break;
                default:
                    out.write("UNKNOWN_COMMAND\n");
                    out.flush();
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRegisterPatient(BufferedReader in, BufferedWriter out) throws IOException {
        String firstName = in.readLine();
        String lastName = in.readLine();
        String dob = in.readLine();
        String regDate = in.readLine();

        System.out.println("Registering patient: " + firstName + " " + lastName);

        String patientEntry = firstName + "," + lastName + "," + dob + "," + regDate;
        String queueEntry = firstName + " " + lastName + ",Waiting";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATIENTS_CSV, true))) {
            writer.write(patientEntry + "\n");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(QUEUE_CSV, true))) {
            writer.write(queueEntry + "\n");
        }

        out.write("SUCCESS\n");
        out.flush();
        System.out.println("Patient registered successfully");
    }

    private static void handleSearchPatient(BufferedReader in, BufferedWriter out) throws IOException {
        String keyword = in.readLine();
        System.out.println("Searching for: " + keyword);
        
        StringBuilder result = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_CSV))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(keyword.toLowerCase())) {
                    result.append(line).append("\n");
                }
            }
        }

        String response = result.toString().isEmpty() ? "No results\n" : result.toString();
        out.write(response);
        out.flush();
        System.out.println("Search completed");
    }

    private static void handleRemoveFromQueue(BufferedReader in, BufferedWriter out) throws IOException {
        String patientName = in.readLine();
        System.out.println("Removing from queue: " + patientName);

        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(QUEUE_CSV))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(patientName + ",")) {
                    lines.add(line);
                } else {
                    found = true;
                }
            }
        }

        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(QUEUE_CSV))) {
                for (String line : lines) {
                    writer.write(line + "\n");
                }
            }
            out.write("SUCCESS\n");
            System.out.println("Patient removed from queue");
        } else {
            out.write("FAILED\n");
            System.out.println("Patient not found in queue");
        }
        out.flush();
    }

    private static void handleMarkAsSeen(BufferedReader in, BufferedWriter out) throws IOException {
        String patientName = in.readLine();
        System.out.println("Marking as seen: " + patientName);

        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(QUEUE_CSV))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(patientName + ",")) {
                    lines.add(patientName + ",Seen");
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        }

        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(QUEUE_CSV))) {
                for (String line : lines) {
                    writer.write(line + "\n");
                }
            }
            out.write("SUCCESS\n");
            System.out.println("Patient marked as seen");
        } else {
            out.write("FAILED\n");
            System.out.println("Patient not found in queue");
        }
        out.flush();
    }
}
