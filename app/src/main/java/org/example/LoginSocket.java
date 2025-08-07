package org.example;

import java.io.*;
import java.net.*;

public class LoginSocket {

    static String sendCommand(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public LoginSocket(String string, int par) {
    }

    public static String login(String username, String password) {
        System.out.println("Connecting to server...");

        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to server, sending credentials...");
            out.println(username);
            out.println(password);

            String role = in.readLine();
            System.out.println("Server responded with: " + role);

            return role;
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
            return "Error";
        }
    }

    String authenticate(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void close() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

