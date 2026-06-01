package com.pao.proiect.food_delivery.service;

import com.pao.proiect.food_delivery.exception.RestaurantNegasitException;
import com.pao.proiect.food_delivery.model.Preparat;
import com.pao.proiect.food_delivery.model.Restaurant;
import com.pao.proiect.food_delivery.repository.PreparatRepository;
import com.pao.proiect.food_delivery.repository.RestaurantRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ServiciuRestaurant {

    private static ServiciuRestaurant instanta;
    private final RestaurantRepository restaurantRepository;
    private final PreparatRepository preparatRepository;

    private ServiciuRestaurant() throws IOException, SQLException {
        this.restaurantRepository = new RestaurantRepository();
        this.preparatRepository = new PreparatRepository();
    }

    public static synchronized ServiciuRestaurant getInstance() throws IOException, SQLException {
        if (instanta == null)
            instanta = new ServiciuRestaurant();
        return instanta;
    }

    public void adauga(Restaurant restaurant) throws SQLException {
        if (restaurant == null)
            throw new IllegalArgumentException("Restaurantul nu poate fi null");
        restaurantRepository.save(restaurant);
    }

    public void elimina(int id) throws SQLException {
        if (!restaurantRepository.findById(id).isPresent())
            throw new RestaurantNegasitException(id);
        restaurantRepository.delete(id);
    }

    public Restaurant gasesteDupaId(int id) throws SQLException {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNegasitException(id));
    }

    public Optional<Restaurant> gasesteDupaNume(String nume) throws SQLException {
        if (nume == null)
            return Optional.empty();
        return restaurantRepository.findAll().stream()
                .filter(r -> r.getNume().equalsIgnoreCase(nume))
                .findFirst();
    }

    public List<Restaurant> listeazaTot() throws SQLException {
        return restaurantRepository.findAll().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Restaurant> cautaDupaBucatarie(String tipBucatarie) throws SQLException {
        if (tipBucatarie == null)
            return Collections.emptyList();
        return restaurantRepository.findAll().stream()
                .filter(r -> r.getTipBucatarie().equalsIgnoreCase(tipBucatarie))
                .sorted()
                .collect(Collectors.toList());
    }

    public void adaugaPreparat(int idRestaurant, Preparat preparat) throws SQLException {
        if (preparat == null)
            throw new IllegalArgumentException("Preparatul nu poate fi null");
        
        // Asiguram existenta restaurantului
        Restaurant r = gasesteDupaId(idRestaurant);
        preparatRepository.save(preparat, idRestaurant);
        r.adaugaPreparat(preparat);
    }

    public boolean eliminaPreparat(int idRestaurant, int idPreparat) throws SQLException {
        Restaurant r = gasesteDupaId(idRestaurant);
        boolean eliminat = r.eliminaPreparat(idPreparat);
        if (eliminat) {
            preparatRepository.delete(idPreparat);
        }
        return eliminat;
    }

    public TreeSet<Preparat> getMeniu(int idRestaurant) throws SQLException {
        List<Preparat> list = preparatRepository.findByRestaurant(idRestaurant);
        return new TreeSet<>(list);
    }
}
