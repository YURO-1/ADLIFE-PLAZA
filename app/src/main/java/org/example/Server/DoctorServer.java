package org.example.Server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class DoctorServer {

    private static final int PORT = 5555;
    private static final File QUEUE_FILE = new File("src/main/resources/CSV/doctor_queue.csv");
    private static final File HISTORY_FILE = new File("src/main/resources/CSV/medical_history.csv");

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Doctor Server started on port " + PORT);

            while (true) {
                Socket client = serverSocket.accept();
                pool.execute(new DoctorClientHandler(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        pool.shutdown();
    }

    static class DoctorClientHandler implements Runnable {
        private Socket socket;

        public DoctorClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
            ) {
                String request;

                while ((request = reader.readLine()) != null) {
                    System.out.println("Request: " + request);
                    String[] tokens = request.split("::");

                    switch (tokens[0]) {
                        case "GET_QUEUE":
                            writer.write(readFile(QUEUE_FILE));
                            writer.newLine();
                            writer.flush();
                            break;

                        case "UPDATE_QUEUE":
                            writeFile(QUEUE_FILE, tokens[1]);
                            writer.write("QUEUE_UPDATED");
                            writer.newLine();
                            writer.flush();
                            break;

                        case "SAVE_HISTORY":
                            appendToFile(HISTORY_FILE, tokens[1]);
                            writer.write("HISTORY_SAVED");
                            writer.newLine();
                            writer.flush();
                            break;

                        default:
                            writer.write("UNKNOWN_COMMAND");
                            writer.newLine();
                            writer.flush();
                            break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Connection error: " + e.getMessage());
            }
        }

        private String readFile(File file) throws IOException {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append(";;");
                }
            }
            return sb.toString();
        }

        private void writeFile(File file, String content) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content.replace(";;", System.lineSeparator()));
            }
        }

        private void appendToFile(File file, String line) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
