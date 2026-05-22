package com.uniquindio.proyectop2.dao.impl;

import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Enums.TipoServicio;
import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.MetodoPago;
import com.uniquindio.proyectop2.Model.ServicioAdicional;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.dao.interfaces.CompraDAO;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.patterns.Creational.singleton.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CompraDAOImpl implements CompraDAO {

    @Override
    public Compra findById(String id) {
        String sql = "SELECT * FROM compra WHERE id_compra = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
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
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    compras.add(map(rs));
                }
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

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    String id = String.valueOf(generatedKeys.getObject(1));
                    compra.setIdCompra(id);
                    guardarServiciosCompra(conn, compra);
                    return id;
                }
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

            try (PreparedStatement delete = conn.prepareStatement("DELETE FROM compra_servicio WHERE id_compra = ?")) {
                delete.setString(1, compra.getIdCompra());
                delete.executeUpdate();
            }
            guardarServiciosCompra(conn, compra);
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

    @Override
    public void saveServicioCompra(String idCompra, String idServicio) {
        String sql = "INSERT INTO compra_servicio (id_compra, id_servicio) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idCompra);
            ps.setString(2, idServicio);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ServicioAdicional> findServiciosByCompra(String idCompra) {
        List<ServicioAdicional> servicios = new ArrayList<>();
        String sql = "SELECT sa.* FROM servicio_adicional sa INNER JOIN compra_servicio cs ON sa.id_servicio = cs.id_servicio WHERE cs.id_compra = ? ORDER BY sa.nombre";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idCompra);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    servicios.add(mapServicio(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicios;
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
        if (estado != null && !estado.isBlank()) {
            compra.setEstado(EstadoCompra.valueOf(estado));
        }

        String idUsuario = rs.getString("id_usuario");
        if (idUsuario != null) {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);
            compra.setUsuario(usuario);
        }

        String idEvento = rs.getString("id_evento");
        if (idEvento != null) {
            Evento evento = DAOFactory.obtenerEventoDAO().findById(idEvento);
            if (evento == null) {
                evento = new Evento();
                evento.setIdEvento(idEvento);
            }
            compra.setEvento(evento);
        }

        String idMetodoPago = rs.getString("id_metodo_pago");
        if (idMetodoPago != null) {
            MetodoPago metodo = DAOFactory.obtenerMetodoPagoDAO().findById(idMetodoPago);
            if (metodo == null) {
                metodo = new MetodoPago();
                metodo.setIdMetodoPago(idMetodoPago);
            }
            compra.SetMetodoPagoUsado(metodo);
        }

        compra.setServiciosAdicionales(findServiciosByCompra(compra.getIdCompra()));
        return compra;
    }

    private void guardarServiciosCompra(Connection conn, Compra compra) throws SQLException {
        if (compra == null || compra.getIdCompra() == null || compra.getServiciosAdicionales() == null || compra.getServiciosAdicionales().isEmpty()) {
            return;
        }

        String sql = "INSERT INTO compra_servicio (id_compra, id_servicio) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (ServicioAdicional servicio : compra.getServiciosAdicionales()) {
                if (servicio != null && servicio.getIdServicio() != null) {
                    ps.setString(1, compra.getIdCompra());
                    ps.setString(2, servicio.getIdServicio());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private ServicioAdicional mapServicio(ResultSet rs) throws SQLException {
        ServicioAdicional servicio = new ServicioAdicional();
        servicio.setIdServicio(rs.getString("id_servicio"));
        servicio.setNombre(rs.getString("nombre"));
        servicio.setDescripcion(rs.getString("descripcion"));
        servicio.setPrecio(rs.getDouble("precio"));
        String tipo = rs.getString("tipo");
        if (tipo != null && !tipo.isBlank()) {
            try {
                servicio.setTipoServicio(TipoServicio.valueOf(tipo));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return servicio;
    }
}
