package com.pao.proiect.food_delivery.model;

public class ArticolComanda {
    private final Preparat preparat;
    private int cantitate;

    public ArticolComanda(Preparat preparat, int cantitate) {
        this.preparat  = preparat;
        this.cantitate = cantitate;
    }

    public Preparat getPreparat()  { return preparat; }
    public int      getCantitate() { return cantitate; }
    public void     setCantitate(int cantitate) { this.cantitate = cantitate; }

    public double getSubtotal() { return preparat.getPret() * cantitate; }

    @Override
    public String toString() {
        return cantitate + "x " + preparat.getNume()
                + " @ " + String.format("%.2f", preparat.getPret())
                + " RON = " + String.format("%.2f", getSubtotal()) + " RON";
    }
}
