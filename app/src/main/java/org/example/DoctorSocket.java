package org.example;

import java.io.*;
import java.net.Socket;

public class DoctorSocket {
    private static final String HOST = "localhost";
    private static final int PORT = 5555;

    public String sendRequest(String request) {
        try (
            Socket socket = new Socket(HOST, PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            writer.write(request);
            writer.newLine();
            writer.flush();

            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
