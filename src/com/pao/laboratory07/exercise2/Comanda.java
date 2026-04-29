package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.StareComanda;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected StareComanda stare;

    public Comanda(String nume) {
        this.nume = nume;
        this.stare = StareComanda.PLACED;
    }

    public abstract double pretFinal();
    public abstract String descriere();
}
