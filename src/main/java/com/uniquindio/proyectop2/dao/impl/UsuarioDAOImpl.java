package com.uniquindio.proyectop2.dao.impl;

import com.uniquindio.proyectop2.dao.interfaces.UsuarioDAO;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.patterns.Creational.singleton.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    @Override
    public Usuario findById(String id) {
        String sql = "select * from usuario where id_usuario = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return mapResultSetToUsuario(rs);
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            return null;
        }
    }

}
