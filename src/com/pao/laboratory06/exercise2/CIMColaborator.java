package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean hasBonus;

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        if (hasBonus) {
            net *= 1.1;
        }
        return net;
    }

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        this.venitBrutLunar = in.nextDouble();
        if (in.hasNext()) {
            String bonusStr = in.next();
            this.hasBonus = "DA".equalsIgnoreCase(bonusStr);
        } else {
            this.hasBonus = false;
        }
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return hasBonus;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.CIM;
    }
}
