package com.pao.proiect.food_delivery.model;

import java.util.Objects;

public class Client extends Persoana {
    private int puncteFidelitate;

    public Client(int id, String nume, String email, String telefon, Adresa adresa) {
        super(id, nume, email, telefon, adresa);
        this.puncteFidelitate = 0;
    }

    @Override
    public String getRol() { return "CLIENT"; }

    public int getPuncteFidelitate() { return puncteFidelitate; }
    public void adaugaPuncteFidelitate(int puncte) { this.puncteFidelitate += puncte; }

    @Override
    public String toString() {
        return "[CLIENT] " + getNume() + " <" + getEmail() + "> - puncte fidelitate: " + puncteFidelitate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client c = (Client) o;
        return Objects.equals(getEmail(), c.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
