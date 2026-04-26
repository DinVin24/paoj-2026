package com.pao.proiect.food_delivery.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Comanda {
    private final int id;
    private final Client client;
    private final Restaurant restaurant;
    private final List<ArticolComanda> articole;
    private StatusComanda status;
    private Sofer sofer;
    private final LocalDateTime dataCrearii;

    public Comanda(int id, Client client, Restaurant restaurant, List<ArticolComanda> articole) {
        this.id = id;
        this.client = client;
        this.restaurant = restaurant;
        this.articole = new ArrayList<>(articole);
        this.status = StatusComanda.IN_ASTEPTARE;
        this.dataCrearii = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<ArticolComanda> getArticole() {
        return articole;
    }

    public StatusComanda getStatus() {
        return status;
    }

    public Sofer getSofer() {
        return sofer;
    }

    public LocalDateTime getDataCrearii() {
        return dataCrearii;
    }

    public void setStatus(StatusComanda status) {
        this.status = status;
    }

    public void setSofer(Sofer sofer) {
        this.sofer = sofer;
    }

    public double calculeazaTotal() {
        return articole.stream().mapToDouble(ArticolComanda::getSubtotal).sum();
    }

    public InregistrareComanda laInregistrare() {
        List<String> rezumat = articole.stream()
                .map(ArticolComanda::toString)
                .collect(Collectors.toList());
        return new InregistrareComanda(id, client.getId(), restaurant.getNume(),
                calculeazaTotal(), dataCrearii, rezumat);
    }

    @Override
    public String toString() {
        return String.format("Comanda{id=%d, client='%s', restaurant='%s', status=%s, total=%.2f RON}",
                id, client.getNume(), restaurant.getNume(), status, calculeazaTotal());
    }
}
