package com.pao.laboratory06.exercise2;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useLocale(Locale.US);
        if (!in.hasNextInt()) {
            return;
        }
        int n = in.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator c = switch (tip) {
                case "CIM" -> {
                    CIMColaborator obj = new CIMColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                case "PFA" -> {
                    PFAColaborator obj = new PFAColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                case "SRL" -> {
                    SRLColaborator obj = new SRLColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            colaboratori.add(c);
        }

        // Sortează și afișează pe tip (CIM, PFA, SRL), fiecare descrescător după venit net anual
        for (TipColaborator t : TipColaborator.values()) {
            colaboratori.stream()
                    .filter(c -> c.getTip() == t)
                    .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
                    .forEach(Colaborator::afiseaza);
        }

        // Colaborator cu venit net maxim
        Colaborator max = colaboratori.stream()
                .max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual))
                .orElse(null);
        System.out.printf("\nColaborator cu venit net maxim: ");
        if (max != null) {
            max.afiseaza();
        }

        // Colaboratori persoane juridice (SRL), descrescător după venit
        System.out.println("\nColaboratori persoane juridice:");
        colaboratori.stream()
                .filter(c -> c instanceof PersoanaJuridica)
                .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
                .forEach(Colaborator::afiseaza);

        // Sume și număr colaboratori pe tip
        System.out.println("\nSume și număr colaboratori pe tip:");
        for (TipColaborator t : TipColaborator.values()) {
            double sumaVal = 0;
            int count = 0;
            boolean found = false;
            for (Colaborator c : colaboratori) {
                if (c.getTip() == t) {
                    sumaVal += c.calculeazaVenitNetAnual();
                    count++;
                    found = true;
                }
            }
            if (found) {
                System.out.printf("%s: suma = %.2f lei, număr = %d\n", t, sumaVal, count);
            } else {
                System.out.printf("%s: suma = nu lei, număr = null\n", t);
            }
        }
    }
}
