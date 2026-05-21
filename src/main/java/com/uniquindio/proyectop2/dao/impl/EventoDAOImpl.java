package com.uniquindio.proyectop2.dao.impl;

import com.uniquindio.proyectop2.Enums.CategoriaEvento;
import com.uniquindio.proyectop2.Enums.EstadoEvento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.Recinto;
import com.uniquindio.proyectop2.Model.Zona;
import com.uniquindio.proyectop2.dao.interfaces.EventoDAO;
import com.uniquindio.proyectop2.patterns.Creational.singleton.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDAOImpl implements EventoDAO {

    private final RecintoDAOImpl recintoDAO = new RecintoDAOImpl();
    private final ZonaDAOImpl zonaDAO = new ZonaDAOImpl();

    @Override
    public Evento findById(String id) {
        String sql = "SELECT * FROM evento WHERE id_evento = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Evento> findAll() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM evento ORDER BY fecha_hora DESC";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                eventos.add(map(rs, true));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }

    @Override
    public List<Evento> findByFiltros(String ciudad, CategoriaEvento categoriaEvento, LocalDate inicio, LocalDate fin) {
        List<Evento> eventos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM evento WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (ciudad != null && !ciudad.isBlank()) {
            sql.append(" AND ciudad LIKE ?");
            params.add("%" + ciudad + "%");
        }
        if (categoriaEvento != null) {
            sql.append(" AND categoria = ?");
            params.add(categoriaEvento.name());
        }
        if (inicio != null) {
            sql.append(" AND fecha_hora >= ?");
            params.add(Timestamp.valueOf(inicio.atStartOfDay()));
        }
        if (fin != null) {
            sql.append(" AND fecha_hora <= ?");
            params.add(Timestamp.valueOf(fin.plusDays(1).atStartOfDay().minusNanos(1)));
        }

        sql.append(" ORDER BY fecha_hora DESC");

        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventos.add(map(rs, true));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }

    @Override
    public void save(Evento evento) {
        String sql = "INSERT INTO evento (nombre, categoria, descripcion, ciudad, fecha_hora, estado, politicas_cancelacion, id_recinto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getCategoria() != null ? evento.getCategoria().name() : null);
            ps.setString(3, evento.getDescripcion());
            ps.setString(4, evento.getCiudad());
            ps.setTimestamp(5, evento.getFechaHora() != null ? Timestamp.valueOf(evento.getFechaHora()) : null);
            ps.setString(6, evento.getEstado() != null ? evento.getEstado().name() : EstadoEvento.BORRADOR.name());
            ps.setString(7, evento.getPoliticasCancelacion());
            ps.setString(8, evento.getRecinto() != null ? evento.getRecinto().getIdRecinto() : null);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                evento.setIdEvento(String.valueOf(generatedKeys.getObject(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Evento evento) {
        String sql = "UPDATE evento SET nombre = ?, categoria = ?, descripcion = ?, ciudad = ?, fecha_hora = ?, estado = ?, politicas_cancelacion = ?, id_recinto = ? WHERE id_evento = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getCategoria() != null ? evento.getCategoria().name() : null);
            ps.setString(3, evento.getDescripcion());
            ps.setString(4, evento.getCiudad());
            ps.setTimestamp(5, evento.getFechaHora() != null ? Timestamp.valueOf(evento.getFechaHora()) : null);
            ps.setString(6, evento.getEstado() != null ? evento.getEstado().name() : EstadoEvento.BORRADOR.name());
            ps.setString(7, evento.getPoliticasCancelacion());
            ps.setString(8, evento.getRecinto() != null ? evento.getRecinto().getIdRecinto() : null);
            ps.setString(9, evento.getIdEvento());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM evento WHERE id_evento = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cambiarEstado(String id, EstadoEvento nuevoEstado) {
        String sql = "UPDATE evento SET estado = ? WHERE id_evento = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Evento map(ResultSet rs, boolean cargarDetalle) throws SQLException {
        Evento evento = new Evento();
        evento.setIdEvento(rs.getString("id_evento"));
        evento.setNombre(rs.getString("nombre"));
        String categoria = rs.getString("categoria");
        if (categoria != null) {
            evento.setCategoria(CategoriaEvento.valueOf(categoria));
        }
        evento.setDescripcion(rs.getString("descripcion"));
        evento.setCiudad(rs.getString("ciudad"));
        Timestamp fecha = rs.getTimestamp("fecha_hora");
        if (fecha != null) {
            evento.setFechaHora(fecha.toLocalDateTime());
        }
        String estado = rs.getString("estado");
        if (estado != null) {
            evento.setEstado(EstadoEvento.valueOf(estado));
        }
        evento.setPoliticasCancelacion(rs.getString("politicas_cancelacion"));
        String idRecinto = rs.getString("id_recinto");
        if (idRecinto != null) {
            Recinto recinto = recintoDAO.finById(idRecinto);
            if (recinto != null && cargarDetalle) {
                recinto.setZonas(zonaDAO.findByRecinto(recinto.getIdRecinto()));
                evento.setZonas(recinto.getZonas());
            }
            evento.setRecinto(recinto);
        }
        return evento;
    }
}
