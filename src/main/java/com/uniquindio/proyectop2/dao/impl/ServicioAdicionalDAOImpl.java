package com.uniquindio.proyectop2.dao.impl;

import com.uniquindio.proyectop2.Enums.TipoServicio;
import com.uniquindio.proyectop2.Model.ServicioAdicional;
import com.uniquindio.proyectop2.dao.interfaces.ServicioAdicionalDAO;
import com.uniquindio.proyectop2.patterns.Creational.singleton.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioAdicionalDAOImpl implements ServicioAdicionalDAO {

    @Override
    public ServicioAdicional findById(String id) {
        String sql = "SELECT * FROM servicio_adicional WHERE id_servicio = ?";
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
    public List<ServicioAdicional> findAll() {
        List<ServicioAdicional> servicios = new ArrayList<>();
        String sql = "SELECT * FROM servicio_adicional ORDER BY nombre";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                servicios.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicios;
    }

    @Override
    public void save(ServicioAdicional servicioAdicional) {
        String sql = "INSERT INTO servicio_adicional (nombre, descripcion, precio, tipo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, servicioAdicional.getNombre());
            ps.setString(2, servicioAdicional.getDescripcion());
            ps.setDouble(3, servicioAdicional.getPrecio());
            ps.setString(4, servicioAdicional.getTipoServicio() != null ? servicioAdicional.getTipoServicio().name() : null);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                servicioAdicional.setIdServicio(String.valueOf(generatedKeys.getObject(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ServicioAdicional servicioAdicional) {
        String sql = "UPDATE servicio_adicional SET nombre = ?, descripcion = ?, precio = ?, tipo = ? WHERE id_servicio = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, servicioAdicional.getNombre());
            ps.setString(2, servicioAdicional.getDescripcion());
            ps.setDouble(3, servicioAdicional.getPrecio());
            ps.setString(4, servicioAdicional.getTipoServicio() != null ? servicioAdicional.getTipoServicio().name() : null);
            ps.setString(5, servicioAdicional.getIdServicio());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM servicio_adicional WHERE id_servicio = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ServicioAdicional map(ResultSet rs) throws SQLException {
        ServicioAdicional servicio = new ServicioAdicional();
        servicio.setIdServicio(rs.getString("id_servicio"));
        servicio.setNombre(rs.getString("nombre"));
        servicio.setDescripcion(rs.getString("descripcion"));
        servicio.setPrecio(rs.getDouble("precio"));
        String tipo = rs.getString("tipo");
        if (tipo != null) {
            try {
                servicio.setTipoServicio(TipoServicio.valueOf(tipo));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return servicio;
    }
}
