package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Tranzactie> tranzactii = new ArrayList<>();
        int N = Integer.parseInt(scanner.nextLine());
        for (int i=1;i<=N;i++){
            String[] parts = scanner.nextLine().split("\\s+");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            TipTranzactie tip = TipTranzactie.valueOf(parts[3]);
            Tranzactie t = new Tranzactie(id, suma, data, tip);
            tranzactii.add(t);
        }
        while(scanner.hasNextLine()){
            String[] parts = scanner.nextLine().split("\\s+");
            String command = parts[0];
            switch(command){
                case "UNIQUE_IDS":
                    LinkedHashSet<Integer> unice = new LinkedHashSet<>();
                    for (Tranzactie t : tranzactii)
                        unice.add(t.getId());
                    System.out.println("IDs unice (" + unice.size() + "): " + unice);
                    break;
                case "MONTHLY_REPORT":
                    Map<String, double[]> monthly = new TreeMap<>();
                    for (Tranzactie t : tranzactii) {
                        String month = t.getData().substring(0, 7);
                        monthly.putIfAbsent(month, new double[2]);
                        if (t.getTip() == TipTranzactie.CREDIT)
                            monthly.get(month)[0] += t.getSuma();
                        else
                            monthly.get(month)[1] += t.getSuma();
                    }
                    for(Map.Entry<String, double[]> entry : monthly.entrySet()){
                        System.out.println(String.format(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON",
                                entry.getKey(), entry.getValue()[0], entry.getValue()[1]));
                    }
                    break;
                case "TOP":
                    int k = Integer.parseInt(parts[1]);
                    List<Tranzactie> copy = new ArrayList<>(tranzactii);
                    Collections.sort(copy, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    int count = Math.min(k, copy.size());
                    System.out.println("Top " + k + ":");
                    for(int i=0;i<count;i++)
                        System.out.println(copy.get(i));
                    break;
                case "SORT_ASC":
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    for(Tranzactie t : tranzactii)
                        System.out.println(t);
                    break;
                case "SORT_DESC":
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    for(Tranzactie t : tranzactii)
                        System.out.println(t);
                    break;
                case "REVERSE":
                    Collections.reverse(tranzactii);
                    for(Tranzactie t : tranzactii)
                        System.out.println(t);
                    break;
                case "MIN_MAX":
                    Tranzactie min = Collections.min(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie max = Collections.max(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    System.out.println("MIN: "+min);
                    System.out.println("MAX: "+max);
                    break;
                case "CME_DEMO":
                    try{
                        for(Tranzactie t : tranzactii)
                            tranzactii.remove(t);
                    } catch (ConcurrentModificationException e){
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
            }
        }
    }
}
