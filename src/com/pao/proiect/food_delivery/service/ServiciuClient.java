package com.pao.proiect.food_delivery.service;

import com.pao.proiect.food_delivery.exception.ClientNegasitException;
import com.pao.proiect.food_delivery.model.Client;

import java.util.*;
import java.util.stream.Collectors;

public class ServiciuClient {

    private static ServiciuClient instanta;

    private final Map<Integer, Client> dupaId = new HashMap<>();
    private final Map<String, Client> dupaEmail = new HashMap<>();

    private ServiciuClient() {
    }

    public static ServiciuClient getInstance() {
        if (instanta == null)
            instanta = new ServiciuClient();
        return instanta;
    }

    public void adauga(Client client) {
        if (client == null)
            throw new IllegalArgumentException("Clientul nu poate fi null");
        if (dupaEmail.containsKey(client.getEmail()))
            throw new IllegalArgumentException("Exista deja un client cu email-ul: " + client.getEmail());
        dupaId.put(client.getId(), client);
        dupaEmail.put(client.getEmail(), client);
    }

    public void elimina(int id) {
        Client c = gasesteDupaId(id);
        dupaId.remove(id);
        dupaEmail.remove(c.getEmail());
    }

    public Client gasesteDupaId(int id) {
        Client c = dupaId.get(id);
        if (c == null)
            throw new ClientNegasitException(id);
        return c;
    }

    public Optional<Client> gasesteDupaEmail(String email) {
        if (email == null)
            return Optional.empty();
        return Optional.ofNullable(dupaEmail.get(email));
    }

    public List<Client> listeazaTot() {
        return dupaId.values().stream()
                .sorted(Comparator.comparing(Client::getNume))
                .collect(Collectors.toList());
    }
}
