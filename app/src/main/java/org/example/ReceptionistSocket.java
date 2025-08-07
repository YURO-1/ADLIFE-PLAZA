package org.example;

import java.io.*;
import java.net.Socket;

public class ReceptionistSocket {
    private final String host;
    private final int port;

    public ReceptionistSocket(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean registerPatient(String firstName, String lastName, String dob, String regDate) {
        try (Socket socket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Registering patient: " + firstName + " " + lastName);
            
            out.write("register_patient\n");
            out.write(firstName + "\n");
            out.write(lastName + "\n");
            out.write(dob + "\n");
            out.write(regDate + "\n");
            out.flush();

            String response = in.readLine();
            boolean success = "SUCCESS".equals(response);
            System.out.println("Registration result: " + (success ? "SUCCESS" : "FAILED"));
            return success;
        } catch (IOException e) {
            System.err.println("Error registering patient: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String searchPatient(String keyword) {
        try (Socket socket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Searching for patient with keyword: " + keyword);
            
            out.write("search_patient\n");
            out.write(keyword + "\n");
            out.flush();

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                response.append(line).append("\n");
            }
            
            String result = response.toString().trim();
            System.out.println("Search result: " + (result.isEmpty() ? "No results found" : result));
            return result.isEmpty() ? "No patients found matching '" + keyword + "'" : result;
        } catch (IOException e) {
            System.err.println("Error searching patient: " + e.getMessage());
            e.printStackTrace();
            return "Error connecting to server.";
        }
    }

    public boolean removeFromQueue(String patientName) {
        try (Socket socket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Removing patient from queue: " + patientName);
            
            out.write("remove_from_queue\n");
            out.write(patientName + "\n");
            out.flush();

            String response = in.readLine();
            boolean success = "SUCCESS".equals(response);
            System.out.println("Remove from queue result: " + (success ? "SUCCESS" : "FAILED"));
            return success;
        } catch (IOException e) {
            System.err.println("Error removing from queue: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean markAsSeen(String patientName) {
        try (Socket socket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Marking patient as seen: " + patientName);
            
            out.write("mark_as_seen\n");
            out.write(patientName + "\n");
            out.flush();

            String response = in.readLine();
            boolean success = "SUCCESS".equals(response);
            System.out.println("Mark as seen result: " + (success ? "SUCCESS" : "FAILED"));
            return success;
        } catch (IOException e) {
            System.err.println("Error marking as seen: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
