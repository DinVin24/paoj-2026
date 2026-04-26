package com.pao.proiect.food_delivery.model;

public class Sofer extends Persoana {
    private String tipVehicul;
    private String numarInmatriculare;
    private boolean disponibil;

    public Sofer(int id, String nume, String email, String telefon,
                 Adresa adresa, String tipVehicul, String numarInmatriculare) {
        super(id, nume, email, telefon, adresa);
        this.tipVehicul   = tipVehicul;
        this.numarInmatriculare  = numarInmatriculare;
        this.disponibil   = true;
    }

    @Override
    public String getRol() { return "SOFER"; }

    public String  getTipVehicul()   { return tipVehicul; }
    public String  getNumarInmatriculare()  { return numarInmatriculare; }
    public boolean isDisponibil()    { return disponibil; }
    public void    setDisponibil(boolean disponibil) { this.disponibil = disponibil; }
    public void    setTipVehicul(String tipVehicul)  { this.tipVehicul = tipVehicul; }

    @Override
    public String toString() {
        return "[SOFER] " + getNume() + " (" + tipVehicul + " / " + numarInmatriculare
                + ") disponibil=" + disponibil;
    }
}
