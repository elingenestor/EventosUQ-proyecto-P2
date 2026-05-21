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
        String sqlRecinto = "INSERT INTO recinto (nombre, direccion, ciudad) VALUES (?, ?, ?)";
        String sqlZona = "INSERT INTO zona (nombre, capacidad, precio_base, id_recinto) VALUES (?, ?, ?, ?)";
        String sqlAsiento = "INSERT INTO asiento (fila, numero, estado, id_zona) VALUES (?, ?, 'DISPONIBLE', ?)";

        Connection conn = null;
        try {
            conn = ConexionBD.getInstance().getConnection();
            conn.setAutoCommit(false); // 🌟 Iniciamos una transacción segura

            // 1. GUARDAMOS EL RECINTO
            try (PreparedStatement psRecinto = conn.prepareStatement(sqlRecinto, Statement.RETURN_GENERATED_KEYS)) {
                psRecinto.setString(1, recinto.getNombre());
                psRecinto.setString(2, recinto.getDireccion());
                psRecinto.setString(3, recinto.getCiudad());
                psRecinto.executeUpdate();

                ResultSet keys = psRecinto.getGeneratedKeys();
                if (keys.next()) {
                    recinto.setIdRecinto(String.valueOf(keys.getInt(1)));
                }
            }

            // 2. RECORREMOS Y GUARDAMOS LAS ZONAS DE ESTE RECINTO
            for (com.uniquindio.proyectop2.Model.Zona zona : recinto.getZonas()) {
                int idZonaGenerada = 0;
                try (PreparedStatement psZona = conn.prepareStatement(sqlZona, Statement.RETURN_GENERATED_KEYS)) {
                    psZona.setString(1, zona.getNombre());
                    psZona.setInt(2, zona.getCapacidad());
                    psZona.setDouble(3, zona.getPrecioBase());
                    psZona.setInt(4, Integer.parseInt(recinto.getIdRecinto()));
                    psZona.executeUpdate();

                    ResultSet keysZona = psZona.getGeneratedKeys();
                    if (keysZona.next()) {
                        idZonaGenerada = keysZona.getInt(1);
                    }
                }

                // 3. 🚀 GENERACIÓN MATEMÁTICA CORREGIDA DE SILLAS
                try (PreparedStatement psAsiento = conn.prepareStatement(sqlAsiento)) {
                    int asientosPorFila = 10;

                    for (int i = 1; i <= zona.getCapacidad(); i++) {
                        char letraFila = (char) ('A' + ((i - 1) / asientosPorFila));
                        String fila = String.valueOf(letraFila);
                        int numeroSilla = ((i - 1) % asientosPorFila) + 1;

                        psAsiento.setString(1, fila);        // Parámetro 1
                        psAsiento.setInt(2, numeroSilla);    // Parámetro 2
                        psAsiento.setInt(3, idZonaGenerada); // Parámetro 3
                        psAsiento.addBatch();
                    }
                    psAsiento.executeBatch();
                }

            }

            conn.commit(); // Si todo salió perfecto, guarda los cambios reales en MySQL
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
            }
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
