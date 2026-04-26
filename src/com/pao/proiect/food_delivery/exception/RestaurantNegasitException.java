package com.pao.proiect.food_delivery.exception;

public class RestaurantNegasitException extends RuntimeException {
    public RestaurantNegasitException(String mesaj) {
        super(mesaj);
    }
    public RestaurantNegasitException(int id) {
        super("Restaurantul cu id=" + id + " nu a fost gasit.");
    }
}
