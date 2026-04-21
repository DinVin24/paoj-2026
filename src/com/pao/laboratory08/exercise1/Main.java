package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = new ArrayList<>();

        // 1. Citește studenții din FILE_PATH cu BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String nume = parts[0].trim();
                    int varsta = Integer.parseInt(parts[1].trim());
                    String oras = parts[2].trim();
                    String strada = parts[3].trim();
                    studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fișierului: " + e.getMessage());
            return;
        }

        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String fullCommand = scanner.nextLine();
        String[] cmdParts = fullCommand.split(" ", 2);
        String command = cmdParts[0].toUpperCase();

        // 3. Execută comanda
        switch (command) {
            case "PRINT":
                for (Student s : studenti) {
                    System.out.println(s);
                }
                break;

            case "SHALLOW":
                if (cmdParts.length < 2) break;
                String numeS = cmdParts[1];
                for (Student s : studenti) {
                    if (s.getNume().equals(numeS)) {
                        Student clona = s.shallowClone();
                        clona.getAdresa().setOras("MODIFICAT");
                        System.out.println("Original: " + s);
                        System.out.println("Clona: " + clona);
                        break;
                    }
                }
                break;

            case "DEEP":
                if (cmdParts.length < 2) break;
                String numeD = cmdParts[1];
                for (Student s : studenti) {
                    if (s.getNume().equals(numeD)) {
                        Student clona = s.deepClone();
                        clona.getAdresa().setOras("MODIFICAT");
                        System.out.println("Original: " + s);
                        System.out.println("Clona: " + clona);
                        break;
                    }
                }
                break;

            default:
                System.out.println("Comandă necunoscută.");
                break;
        }
    }
}
