package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.StareComanda;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected String client;
    protected StareComanda stare;

    public Comanda(String nume, String client) {
        this.nume = nume;
        this.client = client;
        this.stare = StareComanda.PLACED;
    }

    public abstract double pretFinal();
    public abstract String descriere();

    public String getClient() {
        return client;
    }

    public String getNume() {
        return nume;
    }
}
