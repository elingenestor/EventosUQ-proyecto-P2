package com.uniquindio.proyectop2.dao.impl;

import com.uniquindio.proyectop2.Model.Recinto;
import com.uniquindio.proyectop2.Model.Zona;
import com.uniquindio.proyectop2.dao.interfaces.ZonaDAO;
import com.uniquindio.proyectop2.patterns.Creational.singleton.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZonaDAOImpl implements ZonaDAO {

    private final RecintoDAOImpl recintoDAO = new RecintoDAOImpl();

    @Override
    public Zona findById(String id) {
        String sql = "SELECT * FROM zona WHERE id_zona = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Zona> findByRecinto(String idRecinto) {
        List<Zona> zonas = new ArrayList<>();
        String sql = "SELECT * FROM zona WHERE id_recinto = ? ORDER BY nombre";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idRecinto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                zonas.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zonas;
    }

    @Override
    public void save(Zona zona) {
        String sql = "INSERT INTO zona (id_recinto, nombre, capacidad, precio_base) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, zona.getRecinto() != null ? zona.getRecinto().getIdRecinto() : null);
            ps.setString(2, zona.getNombre());
            ps.setInt(3, zona.getCapacidad());
            ps.setDouble(4, zona.getPrecioBase());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                zona.setIdZona(String.valueOf(generatedKeys.getObject(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Zona zona) {
        String sql = "UPDATE zona SET id_recinto = ?, nombre = ?, capacidad = ?, precio_base = ? WHERE id_zona = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, zona.getRecinto() != null ? zona.getRecinto().getIdRecinto() : null);
            ps.setString(2, zona.getNombre());
            ps.setInt(3, zona.getCapacidad());
            ps.setDouble(4, zona.getPrecioBase());
            ps.setString(5, zona.getIdZona());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM zona WHERE id_zona = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Zona map(ResultSet rs) throws SQLException {
        Zona zona = new Zona();
        zona.setIdZona(rs.getString("id_zona"));
        zona.setNombre(rs.getString("nombre"));
        zona.setCapacidad(rs.getInt("capacidad"));
        zona.setPrecioBase(rs.getDouble("precio_base"));
        String idRecinto = rs.getString("id_recinto");
        if (idRecinto != null) {
            Recinto recinto = recintoDAO.finById(idRecinto);
            zona.setRecinto(recinto);
        }
        return zona;
    }
}
