package com.pao.proiect.food_delivery.repository;

import com.pao.proiect.food_delivery.model.Adresa;
import com.pao.proiect.food_delivery.model.Sofer;
import com.pao.proiect.food_delivery.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoferRepository implements Repository<Sofer, Integer> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Sofer mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nume = rs.getString("nume");
        String email = rs.getString("email");
        String telefon = rs.getString("telefon");
        String strada = rs.getString("strada");
        String oras = rs.getString("oras");
        String codPostal = rs.getString("cod_postal");
        String tara = rs.getString("tara");
        String vehicul = rs.getString("vehicul");
        String numarInmatriculare = rs.getString("numar_inmatriculare");
        boolean disponibil = rs.getInt("disponibil") == 1;

        Adresa adresa = null;
        if (strada != null) {
            adresa = new Adresa(strada, oras, codPostal, tara);
        }

        Sofer sofer = new Sofer(id, nume, email, telefon, adresa, vehicul, numarInmatriculare);
        sofer.setDisponibil(disponibil);
        return sofer;
    }

    @Override
    public void save(Sofer sofer) throws SQLException {
        String sql = "INSERT INTO soferi (id, nume, email, telefon, strada, oras, cod_postal, tara, vehicul, numar_inmatriculare, disponibil) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, sofer.getId());
            ps.setString(2, sofer.getNume());
            ps.setString(3, sofer.getEmail());
            ps.setString(4, sofer.getTelefon());
            
            if (sofer.getAdresa() != null) {
                ps.setString(5, sofer.getAdresa().getStrada());
                ps.setString(6, sofer.getAdresa().getOras());
                ps.setString(7, sofer.getAdresa().getCodPostal());
                ps.setString(8, sofer.getAdresa().getTara());
            } else {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
            }
            ps.setString(9, sofer.getTipVehicul());
            ps.setString(10, sofer.getNumarInmatriculare());
            ps.setInt(11, sofer.isDisponibil() ? 1 : 0);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Sofer> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM soferi WHERE id = ?";
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
    public List<Sofer> findAll() throws SQLException {
        String sql = "SELECT * FROM soferi ORDER BY id";
        List<Sofer> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Sofer sofer) throws SQLException {
        String sql = "UPDATE soferi SET nume = ?, email = ?, telefon = ?, strada = ?, oras = ?, cod_postal = ?, tara = ?, vehicul = ?, numar_inmatriculare = ?, disponibil = ? " +
                     "WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, sofer.getNume());
            ps.setString(2, sofer.getEmail());
            ps.setString(3, sofer.getTelefon());
            
            if (sofer.getAdresa() != null) {
                ps.setString(4, sofer.getAdresa().getStrada());
                ps.setString(5, sofer.getAdresa().getOras());
                ps.setString(6, sofer.getAdresa().getCodPostal());
                ps.setString(7, sofer.getAdresa().getTara());
            } else {
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            }
            ps.setString(8, sofer.getTipVehicul());
            ps.setString(9, sofer.getNumarInmatriculare());
            ps.setInt(10, sofer.isDisponibil() ? 1 : 0);
            ps.setInt(11, sofer.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM soferi WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
