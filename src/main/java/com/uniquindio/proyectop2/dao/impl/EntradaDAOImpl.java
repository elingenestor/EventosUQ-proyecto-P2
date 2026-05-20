package com.uniquindio.proyectop2.dao.impl;

import com.uniquindio.proyectop2.Enums.EstadoEntrada;
import com.uniquindio.proyectop2.Model.Asiento;
import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.Model.Entrada;
import com.uniquindio.proyectop2.Model.Zona;
import com.uniquindio.proyectop2.dao.interfaces.EntradaDAO;
import com.uniquindio.proyectop2.patterns.Creational.singleton.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntradaDAOImpl implements EntradaDAO {

    private final ZonaDAOImpl zonaDAO = new ZonaDAOImpl();
    private final AsientoDAOImpl asientoDAO = new AsientoDAOImpl();

    @Override
    public Entrada findById(String id) {
        String sql = "SELECT * FROM entrada WHERE id_entrada = ?";
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
    public List<Entrada> findByCompra(String idCompra) {
        List<Entrada> entradas = new ArrayList<>();
        String sql = "SELECT * FROM entrada WHERE id_compra = ? ORDER BY id_entrada";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idCompra);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entradas.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entradas;
    }

    @Override
    public List<Entrada> findByEvento(String idEvento) {
        List<Entrada> entradas = new ArrayList<>();
        String sql = "SELECT e.* FROM entrada e INNER JOIN compra c ON e.id_compra = c.id_compra WHERE c.id_evento = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idEvento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entradas.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entradas;
    }

    @Override
    public void save(Entrada entrada) {
        String sql = "INSERT INTO entrada (id_compra, id_zona, id_asiento, precio_final, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entrada.getCompra() != null ? entrada.getCompra().getIdCompra() : null);
            ps.setString(2, entrada.getZona() != null ? entrada.getZona().getIdZona() : null);
            ps.setString(3, entrada.getAsiento() != null ? entrada.getAsiento().getIdAsiento() : null);
            ps.setDouble(4, entrada.getPrecioFinal());
            ps.setString(5, entrada.getEstadoEntrada() != null ? entrada.getEstadoEntrada().name() : EstadoEntrada.ACTIVA.name());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                entrada.setIdEntrada(String.valueOf(generatedKeys.getObject(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Entrada entrada) {
        String sql = "UPDATE entrada SET id_compra = ?, id_zona = ?, id_asiento = ?, precio_final = ?, estado = ? WHERE id_entrada = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entrada.getCompra() != null ? entrada.getCompra().getIdCompra() : null);
            ps.setString(2, entrada.getZona() != null ? entrada.getZona().getIdZona() : null);
            ps.setString(3, entrada.getAsiento() != null ? entrada.getAsiento().getIdAsiento() : null);
            ps.setDouble(4, entrada.getPrecioFinal());
            ps.setString(5, entrada.getEstadoEntrada() != null ? entrada.getEstadoEntrada().name() : EstadoEntrada.ACTIVA.name());
            ps.setString(6, entrada.getIdEntrada());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM entrada WHERE id_entrada = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cambiarEstado(String id, EstadoEntrada nuevoEstado) {
        String sql = "UPDATE entrada SET estado = ? WHERE id_entrada = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Entrada map(ResultSet rs) throws SQLException {
        Entrada entrada = new Entrada();
        entrada.setIdEntrada(rs.getString("id_entrada"));
        entrada.setPrecioFinal(rs.getDouble("precio_final"));
        String estado = rs.getString("estado");
        if (estado != null) {
            entrada.setEstadoEntrada(EstadoEntrada.valueOf(estado));
        }
        String idCompra = rs.getString("id_compra"), idZona = rs.getString("id_zona"), idAsiento = rs.getString("id_asiento");
        if (idCompra != null) {
            Compra compra = new Compra();
            compra.setIdCompra(idCompra);
            entrada.setCompra(compra);
        }
        if (idZona != null) {
            Zona zona = zonaDAO.findById(idZona);
            entrada.setZona(zona);
        }
        if (idAsiento != null) {
            Asiento asiento = asientoDAO.findById(idAsiento);
            entrada.setAsiento(asiento);
        }
        return entrada;
    }
}
