package com.pao.proiect.food_delivery.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public final class AuditService {

    private static AuditService instance;

    // Fisierul de audit (relativ la working directory = radacina proiectului)
    private static final String AUDIT_FILE = "audit.csv";

    // ReentrantLock protejeaza scrierea in fisier daca mai multe thread-uri
    // apeleaza log() simultan
    private final ReentrantLock lock = new ReentrantLock();

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private AuditService() {
        java.io.File file = new java.io.File(AUDIT_FILE);
        if (!file.exists() || file.length() == 0) {
            lock.lock();
            try (PrintWriter pw = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
                pw.println("action_name,timestamp");
            } catch (IOException e) {
                System.err.println("[AUDIT] Eroare la initializare: " + e.getMessage());
            } finally {
                lock.unlock();
            }
        }
    }

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String actionName) {
        lock.lock();                              // blocare exclusiva
        try (PrintWriter pw = new PrintWriter(
                new FileWriter(AUDIT_FILE, true))) { // true = append mode
            String timestamp = LocalDateTime.now().format(formatter);
            pw.println(actionName + "," + timestamp);
        } catch (IOException e) {
            System.err.println("[AUDIT] Eroare la scriere: " + e.getMessage());
        } finally {
            lock.unlock();                        // eliberare garantata
        }
    }
}
