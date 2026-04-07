package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica {
    private double venitLunar;
    private double cheltuieliLunare;

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetBaza = (venitLunar - cheltuieliLunare) * 12;
        double impozitVal = 0.1 * venitNetBaza;
        
        final double SALARIU_MINIM_ANUAL = 48600.0;
        
        double cass;
        if (venitNetBaza < 6 * SALARIU_MINIM_ANUAL) {
            cass = 0.1 * (6 * SALARIU_MINIM_ANUAL);
        } else if (venitNetBaza <= 72 * SALARIU_MINIM_ANUAL) {
            cass = 0.1 * venitNetBaza;
        } else {
            cass = 0.1 * (72 * SALARIU_MINIM_ANUAL);
        }
        
        double cas;
        if (venitNetBaza < 12 * SALARIU_MINIM_ANUAL) {
            cas = 0;
        } else if (venitNetBaza <= 24 * SALARIU_MINIM_ANUAL) {
            cas = 0.25 * (12 * SALARIU_MINIM_ANUAL);
        } else {
            cas = 0.25 * (24 * SALARIU_MINIM_ANUAL);
        }
        
        return venitNetBaza - impozitVal - cass - cas;
    }

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        this.venitLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public String tipContract() {
        return "PFA";
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.PFA;
    }
}
