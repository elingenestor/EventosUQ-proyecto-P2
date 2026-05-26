package com.uniquindio.proyectop2.service.impl;
import com.uniquindio.proyectop2.dao.interfaces.UsuarioDAO;
import com.uniquindio.proyectop2.dao.interfaces.RecintoDAO;
import com.uniquindio.proyectop2.Model.Recinto;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.service.interfaces.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final UsuarioDAO usuarioDAO;
    private final RecintoDAO recintoDAO;

    public AdminServiceImpl(UsuarioDAO usuarioDAO, RecintoDAO recintoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.recintoDAO = recintoDAO;
    }

    @Override
    public List<Usuario> listarUsuarios() throws Exception {
        return usuarioDAO.findAll();
    }

    @Override
    public void crearUsuario(Usuario usuario) throws Exception {
        if (usuarioDAO.existeEmail(usuario.getEmail())) {
            throw new Exception("Email ya existe.");
        }
        usuarioDAO.save(usuario);
    }

    @Override
    public void actualizarUsuario(Usuario usuario) throws Exception {
        usuarioDAO.update(usuario);
    }

    @Override
    public void eliminarUsuario(String idUsuario) throws Exception {
        usuarioDAO.delete(idUsuario);
    }

    @Override
    public List<Recinto> listarRecintos() throws Exception {
        return recintoDAO.findAll();
    }

    @Override
    public void crearRecinto(Recinto recinto) throws Exception {
        recintoDAO.save(recinto);
    }

    @Override
    public void actualizarRecinto(Recinto recinto) throws Exception {
        recintoDAO.update(recinto);
    }

    @Override
    public void eliminarRecinto(String idRecinto) throws Exception {
        recintoDAO.delete(idRecinto);
    }

    @Override
    public void generarReportes() {
    }
}
