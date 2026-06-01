package com.pao.proiect.food_delivery.service;

import com.pao.proiect.food_delivery.exception.ComandaNuPoateAnulataException;
import com.pao.proiect.food_delivery.model.*;
import com.pao.proiect.food_delivery.repository.ClientRepository;
import com.pao.proiect.food_delivery.repository.ComandaRepository;
import com.pao.proiect.food_delivery.repository.SoferRepository;
import com.pao.proiect.food_delivery.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ServiciuComanda {

    private static ServiciuComanda instanta;
    private final ComandaRepository comandaRepository;
    private final ClientRepository clientRepository;
    private final SoferRepository soferRepository;
    private int urmatoareId = 1;

    private ServiciuComanda() throws IOException, SQLException {
        this.comandaRepository = new ComandaRepository();
        this.clientRepository = new ClientRepository();
        this.soferRepository = new SoferRepository();

        List<Comanda> existente = comandaRepository.findAll();
        if (!existente.isEmpty()) {
            this.urmatoareId = existente.stream().mapToInt(Comanda::getId).max().getAsInt() + 1;
        }
    }

    public static synchronized ServiciuComanda getInstance() throws IOException, SQLException {
        if (instanta == null)
            instanta = new ServiciuComanda();
        return instanta;
    }

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    // baerm rand 5 - tranzactie explicita
    public Comanda plaseazaComanda(Client client, Restaurant restaurant, List<ArticolComanda> articole) throws SQLException, IOException {
        if (client == null)
            throw new IllegalArgumentException("Clientul nu poate fi null");
        if (restaurant == null)
            throw new IllegalArgumentException("Restaurantul nu poate fi null");
        if (articole == null || articole.isEmpty())
            throw new IllegalArgumentException("Comanda trebuie sa aiba cel putin un articol");

        Connection conn = getConn();
        conn.setAutoCommit(false); // Dezactivam commits

        try {
            // Asiguram existenta clientului si restaurantului in DB inainte de plasarea comenzii
            if (!clientRepository.findById(client.getId()).isPresent()) {
                clientRepository.save(client);
            }

            Comanda comanda = new Comanda(urmatoareId++, client, restaurant, articole);

            // Pasul 1: Salvam comanda principala
            comandaRepository.save(comanda);

            // Pasul 2: Salvam articolele comenzii in tabela de legatura (articole_comanda)
            String sqlArticol = "INSERT INTO articole_comanda (id_comanda, id_preparat, cantitate) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlArticol)) {
                for (ArticolComanda art : articole) {
                    ps.setInt(1, comanda.getId());
                    ps.setInt(2, art.getPreparat().getId());
                    ps.setInt(3, art.getCantitate());
                    ps.executeUpdate();
                }
            }

            conn.commit(); // Toate scrierile au reusit -> salvam definitiv
            System.out.println("[TX] plaseazaComanda comisa cu succes in SQLite. Comanda ID=" + comanda.getId());
            return comanda;

        } catch (SQLException e) {
            conn.rollback(); // Ceva a esuat -> anulam toate modificarile partiale
            System.out.println("[TX] plaseazaComanda rollback din cauza: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Re-activam comportamentul implicit
        }
    }

    public void asigneazaSofer(int idComanda, Sofer sofer) throws SQLException, IOException {
        if (sofer == null)
            throw new IllegalArgumentException("Soferul nu poate fi null");

        Comanda comanda = comandaRepository.findById(idComanda)
                .orElseThrow(() -> new NoSuchElementException("Comanda cu id=" + idComanda + " nu a fost gasita"));

        if (comanda.getStatus() != StatusComanda.IN_ASTEPTARE)
            throw new IllegalStateException("Doar comenzile IN_ASTEPTARE pot primi un sofer");

        // Asiguram existenta soferului in DB
        if (!soferRepository.findById(sofer.getId()).isPresent()) {
            soferRepository.save(sofer);
        }

        comanda.setSofer(sofer);
        comanda.setStatus(StatusComanda.ASIGNAT);
        sofer.setDisponibil(false);

        // Actualizam entitatile in DB
        comandaRepository.update(comanda);
        soferRepository.update(sofer);

        System.out.println("  [ServiciuComanda] Soferul '" + sofer.getNume() + "' asignat la comanda #" + idComanda);
    }

    public List<Comanda> listeazaDupaSofer(Sofer sofer) throws SQLException {
        if (sofer == null)
            return Collections.emptyList();

        return comandaRepository.findAll().stream()
                .filter(c -> sofer.equals(c.getSofer())
                        && c.getStatus() != StatusComanda.LIVRAT
                        && c.getStatus() != StatusComanda.ANULAT)
                .collect(Collectors.toList());
    }

    public List<InregistrareComanda> getIstoricClient(int idClient) throws SQLException {
        // Reconstruim istoricul clientului pe baza comenzilor sale livrate din DB
        return comandaRepository.findAll().stream()
                .filter(c -> c.getClient().getId() == idClient && c.getStatus() == StatusComanda.LIVRAT)
                .map(Comanda::laInregistrare)
                .collect(Collectors.toList());
    }

    public void anuleaza(int idComanda) throws SQLException, IOException {
        Comanda comanda = comandaRepository.findById(idComanda)
                .orElseThrow(() -> new NoSuchElementException("Comanda cu id=" + idComanda + " nu a fost gasita"));

        if (comanda.getStatus() == StatusComanda.IN_LIVRARE || comanda.getStatus() == StatusComanda.LIVRAT) {
            throw new ComandaNuPoateAnulataException(idComanda, comanda.getStatus().name());
        }
        if (comanda.getStatus() == StatusComanda.ANULAT) {
            System.out.println("  [ServiciuComanda] Comanda #" + idComanda + " este deja anulata.");
            return;
        }

        Sofer sofer = comanda.getSofer();
        if (sofer != null) {
            sofer.setDisponibil(true);
            soferRepository.update(sofer);
        }

        comanda.setStatus(StatusComanda.ANULAT);
        comandaRepository.update(comanda);

        System.out.println("  [ServiciuComanda] Comanda #" + idComanda + " anulata.");
    }

    public InregistrareComanda livreaza(int idComanda) throws SQLException, IOException {
        Comanda comanda = comandaRepository.findById(idComanda)
                .orElseThrow(() -> new NoSuchElementException("Comanda cu id=" + idComanda + " nu a fost gasita"));

        comanda.setStatus(StatusComanda.LIVRAT);

        Sofer sofer = comanda.getSofer();
        if (sofer != null) {
            sofer.setDisponibil(true);
            soferRepository.update(sofer);
        }

        comandaRepository.update(comanda);

        System.out.println("  [ServiciuComanda] Comanda #" + idComanda + " livrata.");
        return comanda.laInregistrare();
    }

    public Comanda gasesteDupaId(int idComanda) throws SQLException {
        return comandaRepository.findById(idComanda)
                .orElseThrow(() -> new NoSuchElementException("Comanda cu id=" + idComanda + " nu a fost gasita"));
    }

    public List<Comanda> listeazaTot() throws SQLException {
        return comandaRepository.findAll();
    }

    public Queue<Comanda> getCoadaAsteptare() throws SQLException {
        List<Comanda> inAsteptare = comandaRepository.findAll().stream()
                .filter(c -> c.getStatus() == StatusComanda.IN_ASTEPTARE)
                .collect(Collectors.toList());
        return new LinkedList<>(inAsteptare);
    }

   // rand 6 barem 3 interogari sql join
    /**
     * JOIN #1: Listeaza comenzile active (care nu sunt LIVRATE/ANULATE)
     * cu detaliile complete ale clientului si ale soferului asignat.
     */
    public List<String> getComenziActiveCuDetalii() throws SQLException, IOException {
        String sql = """
                SELECT c.id, c.status, c.total,
                       cl.nume AS client_nume, cl.telefon AS client_tel,
                       s.nume  AS sofer_nume, s.vehicul AS sofer_vehicul
                FROM comenzi c
                JOIN clienti cl ON c.id_client = cl.id
                LEFT JOIN soferi s ON c.id_sofer = s.id
                WHERE c.status != 'LIVRAT' AND c.status != 'ANULAT'
                ORDER BY c.id
                """;
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String numeSofer = rs.getString("sofer_nume");
                String detaliiSofer = (numeSofer != null) ?
                        numeSofer + " (" + rs.getString("sofer_vehicul") + ")" : "Neasignat";
                rezultate.add(String.format("Comanda#%d | Client: %s (%s) | Livrator: %s | Total: %.2f RON | Status: %s",
                        rs.getInt("id"),
                        rs.getString("client_nume"),
                        rs.getString("client_tel"),
                        detaliiSofer,
                        rs.getDouble("total"),
                        rs.getString("status")));
            }
        }
        return rezultate;
    }

    /**
     * JOIN #2: Top 5 preparate cele mai comandate de pe platforma, cu numele restaurantului.
     */
    public List<String> getTopPreparateComandate() throws SQLException, IOException {
        String sql = """
                SELECT p.nume      AS prep_nume,
                       r.nume      AS rest_nume,
                       SUM(ac.cantitate) AS total_comandat
                FROM articole_comanda ac
                JOIN preparate p ON ac.id_preparat = p.id
                JOIN restaurante r ON p.id_restaurant = r.id
                GROUP BY p.id, p.nume, r.nume
                ORDER BY total_comandat DESC
                LIMIT 5
                """;
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultate.add(String.format("Preparat: %-20s | Restaurant: %-15s | Total Comandat: %d buc",
                        rs.getString("prep_nume"),
                        rs.getString("rest_nume"),
                        rs.getInt("total_comandat")));
            }
        }
        return rezultate;
    }

    /**
     * JOIN #3: Istoricul detaliat al unui client (toate preparatele comandate si livratorul lor).
     */
    public List<String> getIstoricCompletClientRaport(int idClient) throws SQLException, IOException {
        String sql = """
                SELECT c.id AS comanda_id, c.data_crearii, c.status, c.total,
                       s.nume AS sofer_nume, p.nume AS prep_nume, ac.cantitate
                FROM comenzi c
                JOIN articole_comanda ac ON c.id = ac.id_comanda
                JOIN preparate p ON ac.id_preparat = p.id
                LEFT JOIN soferi s ON c.id_sofer = s.id
                WHERE c.id_client = ?
                ORDER BY c.data_crearii DESC
                """;
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String sofer = rs.getString("sofer_nume");
                    String detaliiSofer = (sofer != null) ? sofer : "Fără șofer";
                    rezultate.add(String.format("Comanda#%d (%s) | Status: %s | Preparat: %s x %d | Livrat de: %s",
                            rs.getInt("comanda_id"),
                            rs.getString("data_crearii"),
                            rs.getString("status"),
                            rs.getString("prep_nume"),
                            rs.getInt("cantitate"),
                            detaliiSofer));
                }
            }
        }
        return rezultate;
    }
}
