package com.pao.proiect.food_delivery.repository;

import com.pao.proiect.food_delivery.model.Adresa;
import com.pao.proiect.food_delivery.model.Client;
import com.pao.proiect.food_delivery.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, Integer> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nume = rs.getString("nume");
        String email = rs.getString("email");
        String telefon = rs.getString("telefon");
        String strada = rs.getString("strada");
        String oras = rs.getString("oras");
        String codPostal = rs.getString("cod_postal");
        String tara = rs.getString("tara");
        int puncte = rs.getInt("puncte_fidelitate");

        Adresa adresa = null;
        if (strada != null) {
            adresa = new Adresa(strada, oras, codPostal, tara);
        }

        Client client = new Client(id, nume, email, telefon, adresa);
        client.adaugaPuncteFidelitate(puncte);
        return client;
    }

    @Override
    public void save(Client client) throws SQLException {
        String sql = "INSERT INTO clienti (id, nume, email, telefon, strada, oras, cod_postal, tara, puncte_fidelitate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, client.getId());
            ps.setString(2, client.getNume());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getTelefon());
            if (client.getAdresa() != null) {
                ps.setString(5, client.getAdresa().getStrada());
                ps.setString(6, client.getAdresa().getOras());
                ps.setString(7, client.getAdresa().getCodPostal());
                ps.setString(8, client.getAdresa().getTara());
            } else {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
            }
            ps.setInt(9, client.getPuncteFidelitate());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Client> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM clienti WHERE id = ?";
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
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT * FROM clienti ORDER BY id";
        List<Client> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Client client) throws SQLException {
        String sql = "UPDATE clienti SET nume = ?, email = ?, telefon = ?, strada = ?, oras = ?, cod_postal = ?, tara = ?, puncte_fidelitate = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, client.getNume());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getTelefon());
            if (client.getAdresa() != null) {
                ps.setString(4, client.getAdresa().getStrada());
                ps.setString(5, client.getAdresa().getOras());
                ps.setString(6, client.getAdresa().getCodPostal());
                ps.setString(7, client.getAdresa().getTara());
            } else {
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            }
            ps.setInt(8, client.getPuncteFidelitate());
            ps.setInt(9, client.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM clienti WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
