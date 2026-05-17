package com.uniquindio.proyectop2.service.impl;

import com.uniquindio.proyectop2.dao.interfaces.MetodoPagoDAO;
import com.uniquindio.proyectop2.dao.interfaces.UsuarioDAO;
import com.uniquindio.proyectop2.Model.MetodoPago;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.service.interfaces.UsuarioService;

import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioDAO usuarioDAO;
    private final MetodoPagoDAO metodoPagoDAO;

    // Inyección por constructor (DIP)
    public UsuarioServiceImpl(UsuarioDAO usuarioDAO, MetodoPagoDAO metodoPagoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.metodoPagoDAO = metodoPagoDAO;
    }

    @Override
    public void registrar(Usuario usuario) throws Exception {
        if (usuarioDAO.existeEmail(usuario.getEmail())) {
            throw new Exception("El email ya está registrado.");
        }
        // En un caso real, encriptar password
        usuarioDAO.save(usuario);
    }

    @Override
    public Usuario login(String email, String password) throws Exception {
        Usuario usuario = usuarioDAO.findByEmail(email);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            throw new Exception("Email o contraseña incorrectos.");
        }
        return usuario;
    }

    @Override
    public void actualizarPerfil(Usuario usuario) throws Exception {
        usuarioDAO.update(usuario);
    }

    @Override
    public void agregarMetodoPago(Usuario usuario, MetodoPago metodo) throws Exception {
        metodo.setUsuario(usuario);
        metodoPagoDAO.save(metodo);
    }

    @Override
    public List<MetodoPago> listarMetodosPago(Usuario usuario) throws Exception {
        return metodoPagoDAO.findByUsuario(usuario.getIdUsuario());
    }
}
