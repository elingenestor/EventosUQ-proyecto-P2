package com.uniquindio.proyectop2.dao.impl;

import com.uniquindio.proyectop2.Model.Recinto;
import com.uniquindio.proyectop2.dao.interfaces.RecintoDAO;
import com.uniquindio.proyectop2.patterns.Creational.singleton.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecintoDAOImpl implements RecintoDAO {

    @Override
    public Recinto finById(String id) {
        String sql = "SELECT * FROM recinto WHERE id_recinto = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRecinto(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Recinto> findAll() {
        List<Recinto> recintos = new ArrayList<Recinto>();
        String sql = "SELECT * FROM recinto ORDER BY nombre";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recintos.add(mapRecinto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recintos;
    }

    @Override
    public List<Recinto> finByCiudad(String ciudad) {
        List<Recinto> recintos = new ArrayList<Recinto>();
        String sql = "SELECT * FROM recinto WHERE ciudad = ? ORDER BY nombre";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ciudad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                recintos.add(mapRecinto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recintos;
    }

    @Override
    public void save(Recinto recinto) {
        String sql = "INSERT INTO recinto (nombre, direccion, ciudad) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, recinto.getNombre());
            ps.setString(2, recinto.getDireccion());
            ps.setString(3, recinto.getCiudad());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                recinto.setIdRecinto(String.valueOf(keys.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Recinto recinto) {
        String sql = "UPDATE recinto SET nombre = ?, direccion = ?, ciudad = ? WHERE id_recinto = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recinto.getNombre());
            ps.setString(2, recinto.getDireccion());
            ps.setString(3, recinto.getCiudad());
            ps.setInt(4, Integer.parseInt(recinto.getIdRecinto()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM recinto WHERE id_recinto = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Recinto mapRecinto(ResultSet rs) throws SQLException {
        Recinto recinto = new Recinto();
        recinto.setIdRecinto(String.valueOf(rs.getInt("id_recinto")));
        recinto.setNombre(rs.getString("nombre"));
        recinto.setDireccion(rs.getString("direccion"));
        recinto.setCiudad(rs.getString("ciudad"));
        return recinto;
    }
}
