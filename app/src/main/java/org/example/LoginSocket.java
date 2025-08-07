package org.example;

import java.io.*;
import java.net.*;

public class LoginSocket {

    public static String login(String username, String password) {
        System.out.println("Connecting to login server...");

        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to login server, sending credentials...");
            out.println(username);
            out.println(password);

            String role = in.readLine();
            System.out.println("Server responded with role: " + role);

            return role;
        } catch (IOException e) {
            System.out.println("Error connecting to login server: " + e.getMessage());
            return "Error";
        }
    }
}

