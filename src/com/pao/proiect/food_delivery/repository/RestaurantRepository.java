package com.pao.proiect.food_delivery.repository;

import com.pao.proiect.food_delivery.model.Adresa;
import com.pao.proiect.food_delivery.model.Preparat;
import com.pao.proiect.food_delivery.model.Restaurant;
import com.pao.proiect.food_delivery.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantRepository implements Repository<Restaurant, Integer> {

    private final PreparatRepository preparatRepository;

    public RestaurantRepository() throws IOException, SQLException {
        this.preparatRepository = new PreparatRepository();
    }

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Restaurant mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nume = rs.getString("nume");
        String tipBucatarie = rs.getString("tip_bucatarie");
        double rating = rs.getDouble("rating");
        String strada = rs.getString("strada");
        String oras = rs.getString("oras");
        String codPostal = rs.getString("cod_postal");
        String tara = rs.getString("tara");

        Adresa adresa = null;
        if (strada != null) {
            adresa = new Adresa(strada, oras, codPostal, tara);
        }

        Restaurant r = new Restaurant(id, nume, tipBucatarie, adresa);
        r.setRating(rating);
        return r;
    }

    @Override
    public void save(Restaurant r) throws SQLException {
        String sql = "INSERT INTO restaurante (id, nume, tip_bucatarie, rating, strada, oras, cod_postal, tara) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, r.getId());
            ps.setString(2, r.getNume());
            ps.setString(3, r.getTipBucatarie());
            ps.setDouble(4, r.getRating());

            if (r.getAdresa() != null) {
                ps.setString(5, r.getAdresa().getStrada());
                ps.setString(6, r.getAdresa().getOras());
                ps.setString(7, r.getAdresa().getCodPostal());
                ps.setString(8, r.getAdresa().getTara());
            } else {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
            }
            ps.executeUpdate();

            // Salvam preparatele din meniu daca exista deja
            for (Preparat p : r.getMeniu()) {
                preparatRepository.save(p, r.getId());
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Restaurant> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM restaurante WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Restaurant r = mapRow(rs);
                    incarcaMeniu(r);
                    return Optional.of(r);
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Restaurant> findAll() throws SQLException {
        String sql = "SELECT * FROM restaurante ORDER BY nume";
        List<Restaurant> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Restaurant r = mapRow(rs);
                incarcaMeniu(r);
                list.add(r);
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Restaurant r) throws SQLException {
        String sql = "UPDATE restaurante SET nume = ?, tip_bucatarie = ?, rating = ?, strada = ?, oras = ?, cod_postal = ?, tara = ? "
                +
                "WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, r.getNume());
            ps.setString(2, r.getTipBucatarie());
            ps.setDouble(3, r.getRating());

            if (r.getAdresa() != null) {
                ps.setString(4, r.getAdresa().getStrada());
                ps.setString(5, r.getAdresa().getOras());
                ps.setString(6, r.getAdresa().getCodPostal());
                ps.setString(7, r.getAdresa().getTara());
            } else {
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            }
            ps.setInt(8, r.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM restaurante WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    private void incarcaMeniu(Restaurant r) throws SQLException {
        List<Preparat> list = preparatRepository.findByRestaurant(r.getId());
        for (Preparat p : list) {
            r.adaugaPreparat(p);
        }
    }
}
