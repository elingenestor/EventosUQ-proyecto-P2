package com.uniquindio.proyectop2.dao.impl;

import com.uniquindio.proyectop2.Enums.EstadoAsiento;
import com.uniquindio.proyectop2.Model.Asiento;
import com.uniquindio.proyectop2.Model.Zona;
import com.uniquindio.proyectop2.dao.interfaces.AsientoDAO;
import com.uniquindio.proyectop2.patterns.Creational.singleton.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsientoDAOImpl implements AsientoDAO {

    private final ZonaDAOImpl zonaDAO = new ZonaDAOImpl();

    @Override
    public Asiento findById(String id) {
        String sql = "SELECT * FROM asiento WHERE id_asiento = ?";
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
    public List<Asiento> findByZona(String idZona) {
        List<Asiento> asientos = new ArrayList<>();
        String sql = "SELECT * FROM asiento WHERE id_zona = ? ORDER BY fila, numero";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idZona);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                asientos.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asientos;
    }

    @Override
    public void save(Asiento asiento) {
        String sql = "INSERT INTO asiento (id_zona, fila, numero, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, asiento.getZona() != null ? asiento.getZona().getIdZona() : null);
            ps.setString(2, asiento.getFila());
            ps.setInt(3, asiento.getNumero());
            ps.setString(4, asiento.getEstado() != null ? asiento.getEstado().name() : EstadoAsiento.DISPONIBLE.name());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                asiento.setIdAsiento(String.valueOf(generatedKeys.getObject(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Asiento asiento) {
        String sql = "UPDATE asiento SET id_zona = ?, fila = ?, numero = ?, estado = ? WHERE id_asiento = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, asiento.getZona() != null ? asiento.getZona().getIdZona() : null);
            ps.setString(2, asiento.getFila());
            ps.setInt(3, asiento.getNumero());
            ps.setString(4, asiento.getEstado() != null ? asiento.getEstado().name() : EstadoAsiento.DISPONIBLE.name());
            ps.setString(5, asiento.getIdAsiento());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM asiento WHERE id_asiento = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cambiarEstado(String idAsiento, EstadoAsiento nuevoEstado) {
        String sql = "UPDATE asiento SET estado = ? WHERE id_asiento = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, idAsiento);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bloquearAsientos(List<Integer> idsAsientos) {
        for (Integer id : idsAsientos) {
            cambiarEstado(String.valueOf(id), EstadoAsiento.BLOQUEADO);
        }
    }

    @Override
    public void liberarAsientos(List<Integer> idsAsientos) {
        for (Integer id : idsAsientos) {
            cambiarEstado(String.valueOf(id), EstadoAsiento.DISPONIBLE);
        }
    }

    private Asiento map(ResultSet rs) throws SQLException {
        Asiento asiento = new Asiento();
        asiento.setIdAsiento(rs.getString("id_asiento"));
        asiento.setFila(rs.getString("fila"));
        asiento.setNumero(rs.getInt("numero"));
        String estado = rs.getString("estado");
        if (estado != null) {
            asiento.setEstado(EstadoAsiento.valueOf(estado));
        }
        String idZona = rs.getString("id_zona");
        if (idZona != null) {
            Zona zona = zonaDAO.findById(idZona);
            asiento.setZona(zona);
        }
        return asiento;
    }
}
