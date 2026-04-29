package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                i--;
                continue;
            }
            String[] tokens = line.split(" ");
            String type = tokens[0];
            if (type.equals("STANDARD")) {
                comenzi.add(new ComandaStandard(tokens[1], Double.parseDouble(tokens[2]), tokens[3]));
            } else if (type.equals("DISCOUNTED")) {
                comenzi.add(new ComandaRedusa(tokens[1], Double.parseDouble(tokens[2]), Integer.parseInt(tokens[3]), tokens[4]));
            } else if (type.equals("GIFT")) {
                comenzi.add(new ComandaGratuita(tokens[1], tokens[2]));
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }
        System.out.println();

        while (sc.hasNext()) {
            String cmdLine = sc.nextLine().trim();
            if (cmdLine.isEmpty()) continue;
            String[] cmdTokens = cmdLine.split(" ");
            String command = cmdTokens[0];

            if (command.equals("QUIT")) {
                break;
            }

            switch (command) {
                case "STATS" -> {
                    System.out.println("--- STATS ---");
                    Map<String, Double> averages = comenzi.stream()
                            .collect(Collectors.groupingBy(
                                    c -> {
                                        if (c instanceof ComandaStandard) return "STANDARD";
                                        if (c instanceof ComandaRedusa) return "DISCOUNTED";
                                        return "GIFT";
                                    },
                                    Collectors.averagingDouble(Comanda::pretFinal)
                            ));
                    
                    List<String> order = List.of("STANDARD", "DISCOUNTED", "GIFT");
                    for (String type : order) {
                        if (averages.containsKey(type)) {
                            System.out.printf("%s: medie = %.2f lei\n", type, averages.get(type));
                        }
                    }
                    System.out.println();
                }
                case "FILTER" -> {
                    double threshold = Double.parseDouble(cmdTokens[1]);
                    System.out.printf("--- FILTER (>= %.2f) ---\n", threshold);
                    comenzi.stream()
                            .filter(c -> c.pretFinal() >= threshold)
                            .forEach(c -> {
                                String type = c instanceof ComandaStandard ? "STANDARD" : (c instanceof ComandaRedusa ? "DISCOUNTED" : "GIFT");
                                if (c instanceof ComandaGratuita) {
                                     System.out.printf("%s: %s, gratuit - client: %s\n", type, c.nume, c.client);
                                } else {
                                     System.out.printf("%s: %s, pret: %.2f lei - client: %s\n", type, c.nume, c.pretFinal(), c.client);
                                }
                            });
                    System.out.println();
                }
                case "SORT" -> {
                    System.out.println("--- SORT (by client, then by pret) ---");
                    comenzi.stream()
                            .sorted(Comparator.comparing(Comanda::getClient).thenComparing(Comanda::pretFinal))
                            .forEach(c -> {
                                String type = c instanceof ComandaStandard ? "STANDARD" : (c instanceof ComandaRedusa ? "DISCOUNTED" : "GIFT");
                                if (c instanceof ComandaGratuita) {
                                     System.out.printf("%s: %s, gratuit - client: %s\n", type, c.nume, c.client);
                                } else {
                                     System.out.printf("%s: %s, pret: %.2f lei - client: %s\n", type, c.nume, c.pretFinal(), c.client);
                                }
                            });
                    System.out.println();
                }
                case "SPECIAL" -> {
                    System.out.println("--- SPECIAL (discount > 15%) ---");
                    comenzi.stream()
                            .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15)
                            .forEach(c -> {
                                ComandaRedusa cr = (ComandaRedusa) c;
                                System.out.printf("DISCOUNTED: %s, pret: %.2f lei (-%d%%) - client: %s\n", cr.nume, cr.pretFinal(), cr.getDiscountProcent(), cr.client);
                            });
                    System.out.println();
                }
            }
        }
    }
}
