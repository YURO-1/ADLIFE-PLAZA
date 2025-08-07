package org.example.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;

public class DoctorServer {
    private static final int PORT = 12345;
    private static final String QUEUE_CSV = "/CSV/doctor_queue.csv";
    private static final String HISTORY_CSV = "/CSV/medical_history.csv";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Doctor server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Doctor connected: " + clientSocket.getInetAddress());
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String request;
            while ((request = reader.readLine()) != null) {
                if (request.equals("GET_QUEUE")) {
                    sendQueue(writer);
                } else if (request.startsWith("GET_DETAILS:")) {
                    String name = request.substring("GET_DETAILS:".length());
                    sendPatientDetails(writer, name);
                } else if (request.startsWith("GET_HISTORY:")) {
                    String name = request.substring("GET_HISTORY:".length());
                    sendHistory(writer, name);
                } else if (request.startsWith("SAVE_HISTORY:")) {
                    String[] parts = request.split(":", 4);
                    if (parts.length == 4) {
                        String name = parts[1];
                        String doctor = parts[2];
                        String history = parts[3];
                        saveHistory(name, doctor, history);
                        writer.println("SAVED");
                    } else {
                        writer.println("ERROR: Invalid save request");
                    }
                } else {
                    writer.println("UNKNOWN_COMMAND");
                }
            }
        } catch (IOException e) {
            System.err.println("Client connection closed.");
        }
    }

    private static void sendQueue(PrintWriter writer) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(QUEUE_CSV));
        for (String line : lines) {
            writer.println(line);
        }
        writer.println("END_QUEUE");
    }

    private static void sendPatientDetails(PrintWriter writer, String fullName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(QUEUE_CSV));
        for (String line : lines) {
            if (line.contains(fullName)) {
                writer.println("Details: " + line);
                return;
            }
        }
        writer.println("Details not found.");
    }

    private static void sendHistory(PrintWriter writer, String fullName) throws IOException {
        Path path = Paths.get(HISTORY_CSV);
        if (!Files.exists(path)) {
            writer.println("No history found.");
            writer.println("END_HISTORY");
            return;
        }

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            if (line.startsWith(fullName + ",")) {
                writer.println(line);
            }
        }
        writer.println("END_HISTORY");
    }

    private static void saveHistory(String name, String doctor, String notes) {
        String entry = name + "," + doctor + "," + notes.replace("\n", " ");
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(HISTORY_CSV), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to save history: " + e.getMessage());
        }
    }
}
