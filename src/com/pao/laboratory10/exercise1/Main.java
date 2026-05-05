package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LinkedList<Tranzactie> tranzactii = new LinkedList<>();
        while (scanner.hasNextLine()){
            String[] parts = scanner.nextLine().split("\\s+");
            String command = parts[0];
            switch (command){
                case "ENQUEUE":
                    int id = Integer.parseInt(parts[1]);
                    double suma = Double.parseDouble(parts[2]);
                    String data = parts[3];
                    TipTranzactie tip = TipTranzactie.valueOf(parts[4]);
                    Tranzactie t = new Tranzactie(id, suma, data, tip);
                    tranzactii.addLast(t);
                    break;
                case "PUSH":
                    id = Integer.parseInt(parts[1]);
                    suma = Double.parseDouble(parts[2]);
                    data = parts[3];
                    tip = TipTranzactie.valueOf(parts[4]);
                    t = new Tranzactie(id, suma, data, tip);
                    tranzactii.addFirst(t);
                    break;
                case "SIZE":
                    System.out.println("Dimensiune coada: " + tranzactii.size());
                    break;
                case "PRINT":
                    for (Tranzactie item : tranzactii){
                        System.out.println(item);
                    }
                    break;
                case "DEQUEUE":
                    if (tranzactii.isEmpty())
                        System.out.println("Coada goala.");
                    else{
                        Tranzactie procesata = tranzactii.removeFirst();
                        System.out.println("Procesat: " + procesata);
                    }
                    break;
                case "POP":
                    if (tranzactii.isEmpty())
                        System.out.println("Coada goala.");
                    else{
                        Tranzactie procesata = tranzactii.removeFirst();
                        System.out.println("Extras: " + procesata);
                    }
                    break;
                case "REMOVE_DEBIT":
                    Iterator<Tranzactie> itr = tranzactii.iterator();
                    int nr_debit = 0;
                    while (itr.hasNext()) {
                        t = itr.next();
                        if (t.getTip() == TipTranzactie.DEBIT) {
                            itr.remove();
                            nr_debit++;
                        }
                    }
                    System.out.printf("Eliminat %d tranzactii DEBIT.\n", nr_debit);
                    break;
                case "REMOVE_BELOW":
                    suma = Double.parseDouble(parts[1]);
                    int nr_threshold = 0;
                    Iterator<Tranzactie> itrb = tranzactii.iterator();
                    while (itrb.hasNext()) {
                        t = itrb.next();
                        if (t.getSuma() < suma) {
                            itrb.remove();
                            nr_threshold++;
                        }
                    }
                    System.out.printf("Eliminat %d tranzactii sub %.2f RON.\n", nr_threshold, suma);
            }
        }

    }
}
