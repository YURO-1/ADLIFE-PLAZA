package org.example.Server;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;

public class ReceptionistServer {
    private final String patientFile = "/CSV/patients.csv";
    private final String queueFile = "/CSV/doctor_queue.csv";

    public ReceptionistServer() {
        ensureFilesExist();
    }

    private void ensureFilesExist() {
        try {
            Files.createDirectories(Paths.get("CSV"));
            if (!Files.exists(Paths.get(patientFile))) {
                Files.createFile(Paths.get(patientFile));
            }
            if (!Files.exists(Paths.get(queueFile))) {
                Files.createFile(Paths.get(queueFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addPatient(String firstName, String lastName, String dob, String regDate) throws IOException {
        String newEntry = String.join(",", firstName, lastName, dob, regDate);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(patientFile, true))) {
            writer.write(newEntry);
            writer.newLine();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(queueFile, true))) {
            writer.write(newEntry);
            writer.newLine();
        }
    }

    public synchronized String searchPatient(String term) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(patientFile));
        for (String line : lines) {
            if (line.toLowerCase().contains(term.toLowerCase())) {
                return line;
            }
        }
        return null;
    }

    public synchronized void getPatientQueue(Consumer<String> callback) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(queueFile));
        for (String line : lines) {
            callback.accept(line);
        }
    }

    public synchronized void removeFromQueue(String fullName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(queueFile));
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                String name = parts[0] + " " + parts[1];
                if (!name.equalsIgnoreCase(fullName)) {
                    updated.add(line);
                }
            }
        }
        Files.write(Paths.get(queueFile), updated);
    }

    public synchronized void markAsSeen(String fullName) throws IOException {
        removeFromQueue(fullName);
    }

    // ------------------ Main Method for Testing ------------------
    public static void main(String[] args) {
        ReceptionistServer server = new ReceptionistServer();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("1. Add Patient");
            System.out.println("2. Search Patient");
            System.out.println("3. View Queue");
            System.out.println("4. Remove from Queue");
            System.out.println("5. Mark as Seen");
            System.out.print("Choose an option: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> {
                    System.out.print("First Name: ");
                    String fn = scanner.nextLine();
                    System.out.print("Last Name: ");
                    String ln = scanner.nextLine();
                    System.out.print("DOB (yyyy-mm-dd): ");
                    String dob = scanner.nextLine();
                    System.out.print("Reg Date (yyyy-mm-dd): ");
                    String reg = scanner.nextLine();
                    server.addPatient(fn, ln, dob, reg);
                    System.out.println("✅ Patient added.");
                }
                case 2 -> {
                    System.out.print("Search Term: ");
                    String term = scanner.nextLine();
                    String result = server.searchPatient(term);
                    System.out.println((result != null) ? "Found: " + result : "❌ No match found.");
                }
                case 3 -> {
                    System.out.println("👥 Patient Queue:");
                    server.getPatientQueue(line -> System.out.println("• " + line));
                }
                case 4 -> {
                    System.out.print("Full name to remove (e.g., John Doe): ");
                    String name = scanner.nextLine();
                    server.removeFromQueue(name);
                    System.out.println("🗑️ Removed from queue.");
                }
                case 5 -> {
                    System.out.print("Full name to mark as seen: ");
                    String name = scanner.nextLine();
                    server.markAsSeen(name);
                    System.out.println("✅ Marked as seen.");
                }
                default -> System.out.println("❌ Invalid option.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
