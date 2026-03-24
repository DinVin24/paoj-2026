package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];

    private AngajatService(){}

    private static class Holder{
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance(){
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a){
        Angajat[] aux = new Angajat[angajati.length + 1];
        System.arraycopy(this.angajati, 0, aux, 0, this.angajati.length);
        aux[this.angajati.length] = a;
        this.angajati = aux;
        System.out.println("Angajat adaugat: " + a.getNume());
    }

    public void printAll(){
        System.out.println("--- Lista completa angajati ---");
        for (Angajat a : angajati)
            System.out.println(a);
    }

    public void listBySalary(){
        Angajat[] copy = this.angajati.clone();
        Arrays.sort(copy);
        System.out.println("--- Sortati dupa salariu ---");
        for(Angajat a : copy)
            System.out.println(a);
    }

    public void findByDepartament(String nume){
        System.out.println("--- Cautare in departamenutl: " + nume + " ---");
        boolean found = false;
        for (Angajat a : angajati)
            if (a.getDepartament().nume().equalsIgnoreCase(nume)){
                System.out.println(a);
                found = true;
            }
        if (!found)
            System.out.println("Niciun angajat in departamenutl: " + nume);
    }

}
