package com.pao.proiect.food_delivery.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InregistrareComanda {
    private final int idComanda;
    private final int idClient;
    private final String numeRestaurant;
    private final double totalPlata;
    private final LocalDateTime timestamp;
    private final List<String> rezumatArticole;

    public InregistrareComanda(int idComanda, int idClient, String numeRestaurant,
            double totalPlata, LocalDateTime timestamp,
            List<String> rezumatArticole) {
        this.idComanda = idComanda;
        this.idClient = idClient;
        this.numeRestaurant = numeRestaurant;
        this.totalPlata = totalPlata;
        this.timestamp = timestamp;
        this.rezumatArticole = Collections.unmodifiableList(new ArrayList<>(rezumatArticole));
    }

    public int getIdComanda() {
        return idComanda;
    }

    public int getIdClient() {
        return idClient;
    }

    public String getNumeRestaurant() {
        return numeRestaurant;
    }

    public double getTotalPlata() {
        return totalPlata;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getRezumatArticole() {
        return rezumatArticole;
    }

    @Override
    public String toString() {
        return String.format(
                "InregistrareComanda{id=%d, client=%d, restaurant='%s', total=%.2f RON, la=%s, articole=%s}",
                idComanda, idClient, numeRestaurant, totalPlata, timestamp, rezumatArticole);
    }
}
