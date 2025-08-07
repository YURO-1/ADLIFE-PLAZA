package org.example.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceptionistServerMain {
    private static final int PORT = 9999;

    public static void main(String[] args) {
        ReceptionistServer server = new ReceptionistServer();
        System.out.println("Receptionist server is running on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new Thread(() -> handleClient(clientSocket, server)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket, ReceptionistServer server) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String command;
            while ((command = in.readLine()) != null) {
                switch (command) {
                    case "ADD_PATIENT":
                        String[] patientData = in.readLine().split(",");
                        server.addPatient(patientData[0], patientData[1], patientData[2], patientData[3]);
                        out.println("OK");
                        break;

                    case "SEARCH_PATIENT":
                        String term = in.readLine();
                        String result = server.searchPatient(term);
                        out.println(result != null ? result : "NOT_FOUND");
                        break;

                    case "GET_QUEUE":
                        server.getPatientQueue(out::println);
                        out.println("END_OF_QUEUE");
                        break;

                    case "REMOVE_FROM_QUEUE":
                        String nameToRemove = in.readLine();
                        server.removeFromQueue(nameToRemove);
                        out.println("OK");
                        break;

                    case "MARK_AS_SEEN":
                        String nameSeen = in.readLine();
                        server.markAsSeen(nameSeen);
                        out.println("OK");
                        break;

                    case "DISCONNECT":
                        out.println("BYE");
                        return;

                    default:
                        out.println("UNKNOWN_COMMAND");
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected unexpectedly.");
        }
    }
}
