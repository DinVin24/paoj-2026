package com.pao.proiect.food_delivery.repository;

import com.pao.proiect.food_delivery.model.Preparat;
import com.pao.proiect.food_delivery.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PreparatRepository implements Repository<Preparat, Integer> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Preparat mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nume = rs.getString("nume");
        String descriere = rs.getString("descriere");
        double pret = rs.getDouble("pret");
        boolean disponibil = rs.getInt("disponibil") == 1;

        Preparat p = new Preparat(id, nume, descriere, pret);
        p.setDisponibil(disponibil);
        return p;
    }

    @Override
    public void save(Preparat entity) throws SQLException {
        throw new UnsupportedOperationException("Foloseste save(Preparat, idRestaurant) pentru a asocia preparatul cu un restaurant.");
    }

    public void save(Preparat preparat, int idRestaurant) throws SQLException {
        String sql = "INSERT INTO preparate (id, nume, descriere, pret, disponibil, id_restaurant) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, preparat.getId());
            ps.setString(2, preparat.getNume());
            ps.setString(3, preparat.getDescriere());
            ps.setDouble(4, preparat.getPret());
            ps.setInt(5, preparat.isDisponibil() ? 1 : 0);
            ps.setInt(6, idRestaurant);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Preparat> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM preparate WHERE id = ?";
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
    public List<Preparat> findAll() throws SQLException {
        String sql = "SELECT * FROM preparate ORDER BY id";
        List<Preparat> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Preparat preparat) throws SQLException {
        String sql = "UPDATE preparate SET nume = ?, descriere = ?, pret = ?, disponibil = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, preparat.getNume());
            ps.setString(2, preparat.getDescriere());
            ps.setDouble(3, preparat.getPret());
            ps.setInt(4, preparat.isDisponibil() ? 1 : 0);
            ps.setInt(5, preparat.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM preparate WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public List<Preparat> findByRestaurant(int idRestaurant) throws SQLException {
        String sql = "SELECT * FROM preparate WHERE id_restaurant = ? ORDER BY pret";
        List<Preparat> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idRestaurant);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }
}
