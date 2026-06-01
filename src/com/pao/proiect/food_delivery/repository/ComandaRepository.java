package com.pao.proiect.food_delivery.repository;

import com.pao.proiect.food_delivery.model.*;
import com.pao.proiect.food_delivery.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComandaRepository implements Repository<Comanda, Integer> {

    private final ClientRepository clientRepository;
    private final RestaurantRepository restaurantRepository;
    private final SoferRepository soferRepository;
    private final PreparatRepository preparatRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public ComandaRepository() throws IOException, SQLException {
        this.clientRepository = new ClientRepository();
        this.restaurantRepository = new RestaurantRepository();
        this.soferRepository = new SoferRepository();
        this.preparatRepository = new PreparatRepository();
    }

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Comanda mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int idClient = rs.getInt("id_client");
        int idRestaurant = rs.getInt("id_restaurant");
        
        // Citim id_sofer si tratam valoarea NULL din baza de date
        int idSofer = rs.getInt("id_sofer");
        boolean hasSofer = !rs.wasNull();

        StatusComanda status = StatusComanda.valueOf(rs.getString("status"));
        String dataStr = rs.getString("data_crearii");
        LocalDateTime dataCrearii = LocalDateTime.parse(dataStr, formatter);

        try {
            Client client = clientRepository.findById(idClient)
                    .orElseThrow(() -> new SQLException("Clientul cu id=" + idClient + " nu exista."));
            Restaurant restaurant = restaurantRepository.findById(idRestaurant)
                    .orElseThrow(() -> new SQLException("Restaurantul cu id=" + idRestaurant + " nu exista."));

            Sofer sofer = null;
            if (hasSofer) {
                sofer = soferRepository.findById(idSofer).orElse(null);
            }

            // Incarcam articolele acestei comenzi
            List<ArticolComanda> articole = incarcaArticole(id);

            Comanda comanda = new Comanda(id, client, restaurant, articole);
            comanda.setStatus(status);
            comanda.setSofer(sofer);
            
            return comanda;
        } catch (SQLException e) {
            throw e;
        }
    }

    private List<ArticolComanda> incarcaArticole(int idComanda) throws SQLException {
        List<ArticolComanda> articole = new ArrayList<>();
        String sql = "SELECT * FROM articole_comanda WHERE id_comanda = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idComanda);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPrep = rs.getInt("id_preparat");
                    int cantitate = rs.getInt("cantitate");

                    Preparat p = preparatRepository.findById(idPrep)
                            .orElseThrow(() -> new SQLException("Preparatul cu id=" + idPrep + " nu exista."));
                    articole.add(new ArticolComanda(p, cantitate));
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return articole;
    }

    @Override
    public void save(Comanda comanda) throws SQLException {
        // Salvare simpla a comenzii (in mod tranzactional o vom face in ServiciuComanda)
        String sql = "INSERT INTO comenzi (id, id_client, id_restaurant, id_sofer, status, total, data_crearii) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, comanda.getId());
            ps.setInt(2, comanda.getClient().getId());
            ps.setInt(3, comanda.getRestaurant().getId());
            if (comanda.getSofer() != null) {
                ps.setInt(4, comanda.getSofer().getId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, comanda.getStatus().name());
            ps.setDouble(6, comanda.calculeazaTotal());
            ps.setString(7, comanda.getDataCrearii().format(formatter));
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Comanda> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM comenzi WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Comanda> findAll() throws SQLException {
        String sql = "SELECT * FROM comenzi ORDER BY id";
        List<Comanda> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Comanda comanda) throws SQLException {
        String sql = "UPDATE comenzi SET id_client = ?, id_restaurant = ?, id_sofer = ?, status = ?, total = ? " +
                     "WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, comanda.getClient().getId());
            ps.setInt(2, comanda.getRestaurant().getId());
            if (comanda.getSofer() != null) {
                ps.setInt(3, comanda.getSofer().getId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, comanda.getStatus().name());
            ps.setDouble(5, comanda.calculeazaTotal());
            ps.setInt(6, comanda.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM comenzi WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
