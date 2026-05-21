package com.uniquindio.proyectop2.service.impl;

import com.uniquindio.proyectop2.Model.MetodoPago;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.dao.interfaces.MetodoPagoDAO;
import com.uniquindio.proyectop2.dao.interfaces.UsuarioDAO;
import com.uniquindio.proyectop2.service.interfaces.UsuarioService;

import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final MetodoPagoDAO metodoPagoDAO;

    public UsuarioServiceImpl(UsuarioDAO usuarioDAO) {
        this(usuarioDAO, null);
    }

    public UsuarioServiceImpl(UsuarioDAO usuarioDAO, MetodoPagoDAO metodoPagoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.metodoPagoDAO = metodoPagoDAO;
    }

    @Override
    public void registrar(Usuario usuario) throws Exception {
        validarUsuario(usuario);

        if (usuarioDAO.existeEmail(usuario.getEmail())) {
            throw new Exception("El email ya está registrado.");
        }

        usuarioDAO.save(usuario);
    }

    @Override
    public Usuario login(String email, String password) throws Exception {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new Exception("Debes ingresar email y contraseña.");
        }

        Usuario usuario = usuarioDAO.findByEmail(email);

        if (usuario == null) {
            throw new Exception("Usuario no encontrado.");
        }

        if (!usuario.getPassword().equals(password)) {
            throw new Exception("Contraseña incorrecta.");
        }

        return usuario;
    }

    @Override
    public void actualizarPerfil(Usuario usuario) throws Exception {
        validarUsuario(usuario);

        if (usuario.getIdUsuario() == null || usuario.getIdUsuario().isBlank()) {
            throw new Exception("El usuario no tiene un ID válido.");
        }

        usuarioDAO.update(usuario);
    }

    @Override
    public void agregarMetodoPago(Usuario usuario, MetodoPago metodo) throws Exception {
        if (metodoPagoDAO == null) {
            throw new Exception("El DAO de métodos de pago todavía no está conectado.");
        }

        if (usuario == null || usuario.getIdUsuario() == null || usuario.getIdUsuario().isBlank()) {
            throw new Exception("Usuario inválido.");
        }

        if (metodo == null) {
            throw new Exception("Método de pago inválido.");
        }

        metodo.setUsuario(usuario);
        metodoPagoDAO.save(metodo);
    }

    @Override
    public List<MetodoPago> listarMetodosPago(Usuario usuario) throws Exception {
        if (metodoPagoDAO == null) {
            throw new Exception("El DAO de métodos de pago todavía no está conectado.");
        }

        if (usuario == null || usuario.getIdUsuario() == null || usuario.getIdUsuario().isBlank()) {
            throw new Exception("Usuario inválido.");
        }

        return metodoPagoDAO.findByUsuario(usuario.getIdUsuario());
    }

    private void validarUsuario(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("El usuario no puede ser nulo.");
        }

        if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().isBlank()) {
            throw new Exception("El nombre completo es obligatorio.");
        }

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new Exception("El email es obligatorio.");
        }

        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new Exception("La contraseña es obligatoria.");
        }

        if (usuario.getTelefono() == null || usuario.getTelefono().isBlank()) {
            throw new Exception("El teléfono es obligatorio.");
        }
    }
}