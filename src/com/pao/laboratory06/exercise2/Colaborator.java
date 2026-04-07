package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;

    public Colaborator() {
    }

    public abstract double calculeazaVenitNetAnual();

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        // The subclasses will read their specific fields after these common ones
    }

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei\n", 
                tipContract(), nume, prenume, calculeazaVenitNetAnual());
    }

    public abstract TipColaborator getTip();

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }
}
