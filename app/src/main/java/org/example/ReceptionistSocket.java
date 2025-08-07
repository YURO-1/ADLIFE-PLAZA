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
            out.write("register_patient\n");
            out.write(firstName + "\n");
            out.write(lastName + "\n");
            out.write(dob + "\n");
            out.write(regDate + "\n");
            out.flush();

            return "SUCCESS".equals(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String searchPatient(String keyword) {
        try (Socket socket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.write("search_patient\n");
            out.write(keyword + "\n");
            out.flush();

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error connecting to server.";
        }
    }
}
