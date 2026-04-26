package com.pao.proiect.food_delivery.model;

public abstract class Persoana {
    private final int id;
    private String nume;
    private String email;
    private String telefon;
    private Adresa adresa;

    public Persoana(int id, String nume, String email, String telefon, Adresa adresa) {
        this.id      = id;
        this.nume    = nume;
        this.email   = email;
        this.telefon = telefon;
        this.adresa  = adresa;
    }

    public abstract String getRol();

    public int    getId()      { return id; }
    public String getNume()    { return nume; }
    public String getEmail()   { return email; }
    public String getTelefon() { return telefon; }
    public Adresa getAdresa()  { return adresa; }

    public void setNume(String nume)       { this.nume    = nume; }
    public void setEmail(String email)     { this.email   = email; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public void setAdresa(Adresa adresa)   { this.adresa  = adresa; }

    @Override
    public String toString() {
        return "[" + getRol() + "] " + nume + " <" + email + ">";
    }
}
