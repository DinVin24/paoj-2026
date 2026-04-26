package com.pao.proiect.food_delivery.model;

import java.util.Objects;
import java.util.TreeSet;

public class Restaurant implements Comparable<Restaurant> {
    private final int id;
    private String nume;
    private String tipBucatarie;
    private Adresa adresa;
    private double rating;
    private final TreeSet<Preparat> meniu;

    public Restaurant(int id, String nume, String tipBucatarie, Adresa adresa) {
        this.id = id;
        this.nume = nume;
        this.tipBucatarie = tipBucatarie;
        this.adresa = adresa;
        this.rating = 0.0;
        this.meniu = new TreeSet<>();
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getTipBucatarie() {
        return tipBucatarie;
    }

    public Adresa getAdresa() {
        return adresa;
    }

    public double getRating() {
        return rating;
    }

    public TreeSet<Preparat> getMeniu() {
        return meniu;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setTipBucatarie(String tipBucatarie) {
        this.tipBucatarie = tipBucatarie;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void adaugaPreparat(Preparat preparat) {
        if (preparat == null)
            throw new IllegalArgumentException("Preparatul nu poate fi null");
        meniu.add(preparat);
    }

    public boolean eliminaPreparat(int idPreparat) {
        return meniu.removeIf(p -> p.getId() == idPreparat);
    }

    /** Ordine alfabetica dupa nume */
    @Override
    public int compareTo(Restaurant altul) {
        return this.nume.compareToIgnoreCase(altul.nume);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Restaurant))
            return false;
        return this.id == ((Restaurant) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Restaurant{id=%d, nume='%s', bucatarie='%s', rating=%.1f, adresa=%s}",
                id, nume, tipBucatarie, rating, adresa);
    }
}
