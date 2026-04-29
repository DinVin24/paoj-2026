package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;

        int N = scanner.nextInt();

        File file = new File(OUTPUT_FILE);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < N; i++) {
                int id = scanner.nextInt();
                double suma = Double.parseDouble(scanner.next());
                String data = scanner.next();
                String tipStr = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(tipStr);

                // 0-3
                dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array());

                // 4-11
                dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array());

                // 12-21
                String paddedData = String.format("%-10s", data);
                if (paddedData.length() > 10) paddedData = paddedData.substring(0, 10);
                dos.write(paddedData.getBytes("ASCII"));

                // 22
                dos.writeByte(tip == TipTranzactie.CREDIT ? 0 : 1);

                // 23
                dos.writeByte(0); //PENDING

                // 24-31
                dos.write(new byte[8]);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            while (scanner.hasNext()) {
                String command = scanner.next();
                if ("READ".equals(command)) {
                    int idx = scanner.nextInt();
                    printRecord(raf, idx);
                } else if ("UPDATE".equals(command)) {
                    int idx = scanner.nextInt();
                    String statusStr = scanner.next();
                    byte statusByte = getStatusByte(statusStr);

                    raf.seek(idx * (long) RECORD_SIZE + 23);
                    raf.writeByte(statusByte);

                    System.out.println("Updated [" + idx + "]: " + statusStr);
                } else if ("PRINT_ALL".equals(command)) {
                    long fileLength = raf.length();
                    int numRecords = (int) (fileLength / RECORD_SIZE);
                    for (int i = 0; i < numRecords; i++) {
                        printRecord(raf, i);
                    }
                }
            }
        }
        
        scanner.close();
    }

    private static byte getStatusByte(String status) {
        switch (status) {
            case "PENDING": return 0;
            case "PROCESSED": return 1;
            case "REJECTED": return 2;
            default: return 0;
        }
    }

    private static String getStatusString(byte status) {
        switch (status) {
            case 0: return "PENDING";
            case 1: return "PROCESSED";
            case 2: return "REJECTED";
            default: return "UNKNOWN";
        }
    }

    private static void printRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek(idx * (long) RECORD_SIZE);
        byte[] record = new byte[RECORD_SIZE];
        raf.readFully(record);

        ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
        
        int id = buffer.getInt();
        double suma = buffer.getDouble();
        
        byte[] dataBytes = new byte[10];
        buffer.get(dataBytes);
        String data = new String(dataBytes, "ASCII").trim();
        
        byte tipByte = buffer.get();
        String tip = (tipByte == 0) ? "CREDIT" : "DEBIT";
        
        byte statusByte = buffer.get();
        String status = getStatusString(statusByte);

        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                idx, id, data, tip, suma, status);
    }
}
