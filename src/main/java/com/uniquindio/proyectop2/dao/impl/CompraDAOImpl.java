package com.uniquindio.proyectop2.dao.impl;

import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.MetodoPago;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.dao.interfaces.CompraDAO;
import com.uniquindio.proyectop2.patterns.Creational.singleton.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAOImpl implements CompraDAO {

    @Override
    public Compra findById(String id) {
        String sql = "SELECT * FROM compra WHERE id_compra = ?";
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
    public List<Compra> findByUsuario(String idUsuario) {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT * FROM compra WHERE id_usuario = ? ORDER BY fecha_creacion DESC";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                compras.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compras;
    }

    @Override
    public String save(Compra compra) {
        String sql = "INSERT INTO compra (id_usuario, id_evento, total, estado, id_metodo_pago) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, compra.getUsuario() != null ? compra.getUsuario().getIdUsuario() : null);
            ps.setString(2, compra.getEvento() != null ? compra.getEvento().getIdEvento() : null);
            ps.setDouble(3, compra.getTotal());
            ps.setString(4, compra.getEstado() != null ? compra.getEstado().name() : EstadoCompra.CREADA.name());
            ps.setString(5, compra.getMetodoPagoUsado() != null ? compra.getMetodoPagoUsado().getIdMetodoPago() : null);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                String id = String.valueOf(generatedKeys.getObject(1));
                compra.setIdCompra(id);
                return compra.getIdCompra();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compra.getIdCompra();
    }

    @Override
    public void update(Compra compra) {
        String sql = "UPDATE compra SET id_usuario = ?, id_evento = ?, total = ?, estado = ?, id_metodo_pago = ? WHERE id_compra = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, compra.getUsuario() != null ? compra.getUsuario().getIdUsuario() : null);
            ps.setString(2, compra.getEvento() != null ? compra.getEvento().getIdEvento() : null);
            ps.setDouble(3, compra.getTotal());
            ps.setString(4, compra.getEstado() != null ? compra.getEstado().name() : EstadoCompra.CREADA.name());
            ps.setString(5, compra.getMetodoPagoUsado() != null ? compra.getMetodoPagoUsado().getIdMetodoPago() : null);
            ps.setString(6, compra.getIdCompra());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM compra WHERE id_compra = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Compra map(ResultSet rs) throws SQLException {
        Compra compra = new Compra();
        compra.setIdCompra(rs.getString("id_compra"));
        Timestamp fecha = rs.getTimestamp("fecha_creacion");
        if (fecha != null) {
            compra.setFechaCreacion(fecha.toLocalDateTime());
        }
        compra.setTotal(rs.getDouble("total"));
        String estado = rs.getString("estado");
        if (estado != null) {
            compra.setEstado(EstadoCompra.valueOf(estado));
        }
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getString("id_usuario"));
        compra.setUsuario(usuario);
        Evento evento = new Evento();
        evento.setIdEvento(rs.getString("id_evento"));
        compra.setEvento(evento);
        String idMetodoPago = rs.getString("id_metodo_pago");
        if (idMetodoPago != null) {
            MetodoPago metodo = new MetodoPago();
            metodo.setIdMetodoPago(idMetodoPago);
            compra.SetMetodoPagoUsado(metodo);
        }
        return compra;
    }
}
