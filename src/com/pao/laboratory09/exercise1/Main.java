package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;

        int N = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            int id = scanner.nextInt();
            double suma = Double.parseDouble(scanner.next());
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.note = "procesat";
            tranzactii.add(t);
        }

        File file = new File(OUTPUT_FILE);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(tranzactii);
        }

        List<Tranzactie> deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            deserialized = (List<Tranzactie>) ois.readObject();
        }

        while (scanner.hasNext()) {
            String command = scanner.next();
            if ("LIST".equals(command)) {
                for (Tranzactie t : deserialized) {
                    System.out.println(t);
                }
            } else if ("FILTER".equals(command)) {
                String prefix = scanner.next();
                boolean found = false;
                for (Tranzactie t : deserialized) {
                    if (t.data.startsWith(prefix)) {
                        System.out.println(t);
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("Niciun rezultat.");
                }
            } else if ("NOTE".equals(command)) {
                int id = scanner.nextInt();
                Tranzactie found = null;
                for (Tranzactie t : deserialized) {
                    if (t.id == id) {
                        found = t;
                        break;
                    }
                }
                if (found != null) {
                    System.out.println("NOTE[" + id + "]: " + found.note);
                } else {
                    System.out.println("NOTE[" + id + "]: not found");
                }
            }
        }
        
        scanner.close();
    }
}
