package org.example.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class ReceptionistServer {
    private static final int PORT = 6000;
    private static final String PATIENTS_CSV = "src/main/resources/CSV/patients.csv";
    private static final String QUEUE_CSV = "src/main/resources/CSV/doctor_queue.csv";


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

            if ("register_patient".equals(command)) {
                String firstName = in.readLine();
                String lastName = in.readLine();
                String dob = in.readLine();
                String regDate = in.readLine();

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

            } else if ("search_patient".equals(command)) {
                String keyword = in.readLine();
                StringBuilder result = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_CSV))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.toLowerCase().contains(keyword.toLowerCase())) {
                            result.append(line).append("\n");
                        }
                    }
                }

                out.write(result.toString().isEmpty() ? "No results\n" : result.toString());
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
