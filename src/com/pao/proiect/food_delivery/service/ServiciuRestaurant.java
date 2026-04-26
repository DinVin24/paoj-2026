package com.pao.proiect.food_delivery.service;

import com.pao.proiect.food_delivery.exception.RestaurantNegasitException;
import com.pao.proiect.food_delivery.model.Preparat;
import com.pao.proiect.food_delivery.model.Restaurant;

import java.util.*;
import java.util.stream.Collectors;

public class ServiciuRestaurant {

    private static ServiciuRestaurant instanta;

    private final Map<Integer, Restaurant> dupaId = new HashMap<>();

    private ServiciuRestaurant() {
    }

    public static ServiciuRestaurant getInstance() {
        if (instanta == null)
            instanta = new ServiciuRestaurant();
        return instanta;
    }

    public void adauga(Restaurant restaurant) {
        if (restaurant == null)
            throw new IllegalArgumentException("Restaurantul nu poate fi null");
        dupaId.put(restaurant.getId(), restaurant);
    }

    public void elimina(int id) {
        if (!dupaId.containsKey(id))
            throw new RestaurantNegasitException(id);
        dupaId.remove(id);
    }

    public Restaurant gasesteDupaId(int id) {
        Restaurant r = dupaId.get(id);
        if (r == null)
            throw new RestaurantNegasitException(id);
        return r;
    }

    public Optional<Restaurant> gasesteDupaNume(String nume) {
        if (nume == null)
            return Optional.empty();
        return dupaId.values().stream()
                .filter(r -> r.getNume().equalsIgnoreCase(nume))
                .findFirst();
    }

    public List<Restaurant> listeazaTot() {
        return dupaId.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Restaurant> cautaDupaBucatarie(String tipBucatarie) {
        if (tipBucatarie == null)
            return Collections.emptyList();
        return dupaId.values().stream()
                .filter(r -> r.getTipBucatarie().equalsIgnoreCase(tipBucatarie))
                .sorted()
                .collect(Collectors.toList());
    }

    public void adaugaPreparat(int idRestaurant, Preparat preparat) {
        gasesteDupaId(idRestaurant).adaugaPreparat(preparat);
    }

    public boolean eliminaPreparat(int idRestaurant, int idPreparat) {
        return gasesteDupaId(idRestaurant).eliminaPreparat(idPreparat);
    }

    public TreeSet<Preparat> getMeniu(int idRestaurant) {
        return gasesteDupaId(idRestaurant).getMeniu();
    }
}
