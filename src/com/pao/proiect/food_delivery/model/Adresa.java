package com.pao.proiect.food_delivery.model;

public class Adresa {
    private String strada;
    private String oras;
    private String codPostal;
    private String tara;

    public Adresa(String strada, String oras, String codPostal, String tara) {
        this.strada    = strada;
        this.oras      = oras;
        this.codPostal = codPostal;
        this.tara      = tara;
    }

    public String getStrada()    { return strada; }
    public String getOras()      { return oras; }
    public String getCodPostal() { return codPostal; }
    public String getTara()      { return tara; }

    public void setStrada(String strada)       { this.strada    = strada; }
    public void setOras(String oras)           { this.oras      = oras; }
    public void setCodPostal(String codPostal) { this.codPostal = codPostal; }
    public void setTara(String tara)           { this.tara      = tara; }

    @Override
    public String toString() {
        return strada + ", " + oras + " " + codPostal + ", " + tara;
    }
}
