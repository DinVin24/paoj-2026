package com.pao.proiect.food_delivery.exception;

public class ComandaNuPoateAnulataException extends RuntimeException {
    public ComandaNuPoateAnulataException(int idComanda, String statusCurent) {
        super("Comanda cu id=" + idComanda
                + " nu poate fi anulata deoarece are statusul: " + statusCurent);
    }
}
