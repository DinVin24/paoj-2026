package com.pao.proiect.food_delivery.model;

import java.util.Objects;

public class Preparat implements Comparable<Preparat> {
    private final int id;
    private String nume;
    private String descriere;
    private double pret;
    private boolean disponibil;

    public Preparat(int id, String nume, String descriere, double pret) {
        this.id = id;
        this.nume = nume;
        this.descriere = descriere;
        this.pret = pret;
        this.disponibil = true;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getDescriere() {
        return descriere;
    }

    public double getPret() {
        return pret;
    }

    public boolean isDisponibil() {
        return disponibil;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public void setDisponibil(boolean disponibil) {
        this.disponibil = disponibil;
    }

    @Override
    public int compareTo(Preparat altul) {
        int cmp = Double.compare(this.pret, altul.pret);
        return cmp != 0 ? cmp : Integer.compare(this.id, altul.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Preparat))
            return false;
        return this.id == ((Preparat) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%-25s %6.2f RON  [%s]", nume, pret,
                disponibil ? "disponibil" : "indisponibil");
    }
}
