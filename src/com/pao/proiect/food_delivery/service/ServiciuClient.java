package com.pao.proiect.food_delivery.service;

import com.pao.proiect.food_delivery.exception.ClientNegasitException;
import com.pao.proiect.food_delivery.model.Client;
import com.pao.proiect.food_delivery.repository.ClientRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServiciuClient {

    private static ServiciuClient instanta;
    private final ClientRepository clientRepository;

    private ServiciuClient() throws IOException, SQLException {
        this.clientRepository = new ClientRepository();
    }

    public static synchronized ServiciuClient getInstance() throws IOException, SQLException {
        if (instanta == null)
            instanta = new ServiciuClient();
        return instanta;
    }

    public void adauga(Client client) throws SQLException {
        if (client == null)
            throw new IllegalArgumentException("Clientul nu poate fi null");
        if (gasesteDupaEmail(client.getEmail()).isPresent())
            throw new IllegalArgumentException("Exista deja un client cu email-ul: " + client.getEmail());
        clientRepository.save(client);
    }

    public void elimina(int id) throws SQLException {
        clientRepository.delete(id);
    }

    public Client gasesteDupaId(int id) throws SQLException {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNegasitException(id));
    }

    public Optional<Client> gasesteDupaEmail(String email) throws SQLException {
        if (email == null)
            return Optional.empty();
        return clientRepository.findAll().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<Client> listeazaTot() throws SQLException {
        return clientRepository.findAll().stream()
                .sorted(Comparator.comparing(Client::getNume))
                .collect(Collectors.toList());
    }
}
