package com.pao.proiect.food_delivery.model;

import java.time.LocalDateTime;

public class Recenzie {
    private final int id;
    private final Client     client;
    private final Restaurant restaurant;
    private int    rating;       // 1-5
    private String comentariu;
    private final LocalDateTime data;

    public Recenzie(int id, Client client, Restaurant restaurant, int rating, String comentariu) {
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("Rating-ul trebuie sa fie intre 1 si 5");
        this.id          = id;
        this.client      = client;
        this.restaurant  = restaurant;
        this.rating      = rating;
        this.comentariu  = comentariu;
        this.data        = LocalDateTime.now();
    }

    public int        getId()         { return id; }
    public Client     getClient()     { return client; }
    public Restaurant getRestaurant() { return restaurant; }
    public int        getRating()     { return rating; }
    public String     getComentariu() { return comentariu; }
    public LocalDateTime getData()    { return data; }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("Rating-ul trebuie sa fie intre 1 si 5");
        this.rating = rating;
    }
    public void setComentariu(String comentariu) { this.comentariu = comentariu; }

    @Override
    public String toString() {
        return String.format("Recenzie{restaurant='%s', autor='%s', rating=%d/5, comentariu='%s'}",
                restaurant.getNume(), client.getNume(), rating, comentariu);
    }
}
