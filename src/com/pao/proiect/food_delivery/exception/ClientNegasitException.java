package com.pao.proiect.food_delivery.exception;

public class ClientNegasitException extends RuntimeException {
    public ClientNegasitException(String mesaj) {
        super(mesaj);
    }
    public ClientNegasitException(int id) {
        super("Clientul cu id=" + id + " nu a fost gasit.");
    }
}
