package com.uniquindio.proyectop2.dao.impl;

import com.uniquindio.proyectop2.Enums.MetodosPago;
import com.uniquindio.proyectop2.Model.MetodoPago;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.dao.interfaces.MetodoPagoDAO;
import com.uniquindio.proyectop2.patterns.Creational.singleton.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoDAOImpl implements MetodoPagoDAO {

    @Override
    public MetodoPago findById(String id) {
        String sql = "SELECT * FROM metodo_pago WHERE id_metodo_pago = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapMetodoPago(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MetodoPago> findByUsuario(String idUsuario) {
        List<MetodoPago> metodos = new ArrayList<MetodoPago>();
        String sql = "SELECT * FROM metodo_pago WHERE id_usuario = ? ORDER BY id_metodo_pago";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idUsuario));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                metodos.add(mapMetodoPago(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metodos;
    }

    @Override
    public void save(MetodoPago metodoPago) {
        String sql = "INSERT INTO metodo_pago (id_usuario, tipo, numero, titular) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, Integer.parseInt(metodoPago.getUsuario().getIdUsuario()));
            ps.setString(2, metodoPago.getTipo() != null ? metodoPago.getTipo().name() : null);
            ps.setString(3, metodoPago.getNumero());
            ps.setString(4, metodoPago.getTitular());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                metodoPago.setIdMetodoPago(String.valueOf(keys.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(MetodoPago metodoPago) {
        String sql = "UPDATE metodo_pago SET tipo = ?, numero = ?, titular = ?, id_usuario = ? WHERE id_metodo_pago = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, metodoPago.getTipo() != null ? metodoPago.getTipo().name() : null);
            ps.setString(2, metodoPago.getNumero());
            ps.setString(3, metodoPago.getTitular());
            ps.setInt(4, Integer.parseInt(metodoPago.getUsuario().getIdUsuario()));
            ps.setInt(5, Integer.parseInt(metodoPago.getIdMetodoPago()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM metodo_pago WHERE id_metodo_pago = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private MetodoPago mapMetodoPago(ResultSet rs) throws SQLException {
        MetodoPago metodo = new MetodoPago();
        metodo.setIdMetodoPago(String.valueOf(rs.getInt("id_metodo_pago")));
        String tipo = rs.getString("tipo");
        if (tipo != null && !tipo.trim().isEmpty()) {
            metodo.setTipo(MetodosPago.valueOf(tipo));
        }
        metodo.setNumero(rs.getString("numero"));
        metodo.setTitular(rs.getString("titular"));
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(String.valueOf(rs.getInt("id_usuario")));
        metodo.setUsuario(usuario);
        return metodo;
    }
}
