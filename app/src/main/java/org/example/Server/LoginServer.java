package org.example.Server;


import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class LoginServer {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Login Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("\nClient connected: " + socket.getInetAddress());

                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    String username = in.readLine();
                    String password = in.readLine();

                    System.out.println("Server received username: " + username);
                    System.out.println("Server received password: " + password);

                    String role = checkCredentials(username, password);
                    System.out.println("Matched role: " + role);

                    out.println(role);
                }
            }
        } catch (IOException e) {
        }
    }

    private static String checkCredentials(String username, String password) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(getCsvFilePath()));
            for (String line : lines.subList(1, lines.size())) { // Skip header
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return parts[2]; // Role
                }
            }
        } catch (IOException e) {
        }
        return "Invalid";
    }

    // Loads CSV file from resources/CSV folder
    private static String getCsvFilePath() throws IOException {
        InputStream is = LoginServer.class.getResourceAsStream("/CSV/user_details.csv");
        if (is == null) {
            throw new FileNotFoundException("CSV file not found in resources/CSV");
        }

        File tempFile = File.createTempFile("user_details", ".csv");
        tempFile.deleteOnExit();
        try (FileOutputStream os = new FileOutputStream(tempFile)) {
            is.transferTo(os);
        }
        return tempFile.getAbsolutePath();
    }
}
