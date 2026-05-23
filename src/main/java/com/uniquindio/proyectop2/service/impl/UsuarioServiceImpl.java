package com.uniquindio.proyectop2.service.impl;

import com.uniquindio.proyectop2.Model.MetodoPago;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.dao.interfaces.MetodoPagoDAO;
import com.uniquindio.proyectop2.dao.interfaces.UsuarioDAO;
import com.uniquindio.proyectop2.service.interfaces.UsuarioService;
import com.uniquindio.proyectop2.util.PasswordUtil;

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

    private boolean emailValido(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }



    @Override
    public void registrar(Usuario usuario) throws Exception {
        validarUsuario(usuario);

        if (usuarioDAO.existeEmail(usuario.getEmail())) {
            throw new Exception("El email ya está registrado.");
        }
        usuario.setPassword(PasswordUtil.hashPassword(usuario.getPassword()));

        usuarioDAO.save(usuario);
    }

    @Override
    public Usuario login(String email, String password) throws Exception {

        String passwordHash = PasswordUtil.hashPassword(password);

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new Exception("Debes ingresar email y contraseña.");
        }

        Usuario usuario = usuarioDAO.findByEmail(email);

        if (usuario == null) {
            throw new Exception("Usuario no encontrado.");
        }

        if (usuario.getPassword().equals(passwordHash)) {
            throw new Exception("Contraseña incorrecta.");
        }

        return usuario;
    }

    @Override
    public void actualizarPerfil(Usuario usuario) throws Exception {

        if(usuario.getNombreCompleto().isBlank() ||
                usuario.getEmail().isBlank()) {

            throw new Exception("Campos obligatorios");
        }

        // VALIDAR FORMATO EMAIL
        if(!emailValido(usuario.getEmail())) {
            throw new Exception("Correo inválido");
        }

        // VALIDAR EMAIL DUPLICADO
        Usuario existente = usuarioDAO.findByEmail(usuario.getEmail());

        if(existente != null &&
                existente.getIdUsuario() != usuario.getIdUsuario()) {

            throw new Exception(
                    "Ya existe un usuario con ese correo"
            );
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

    @Override
    public void actualizarPassword(Usuario usuario, String passwordActual, String nuevaPassword) throws Exception {

        Usuario usuarioBD = usuarioDAO.findById(usuario.getIdUsuario());



        if(usuarioBD == null) {
            throw new Exception("Usuario no encontrado");
        }

        String passwordActualHash = PasswordUtil.hashPassword(passwordActual);

        // VALIDAR PASSWORD ACTUAL
        if(!usuarioBD.getPassword().equals(passwordActualHash)) {
            throw new Exception(
                    "La contraseña actual es incorrecta"
            );
        }

        // VALIDAR NUEVA PASSWORD
        if(nuevaPassword.length() < 8) {
            throw new Exception(
                    "La nueva contraseña debe tener mínimo 8 caracteres"
            );
        }

        String nuevaPasswordHash =
                PasswordUtil.hashPassword(nuevaPassword);


        usuarioDAO.updatePassword(usuario.getIdUsuario(), nuevaPasswordHash
        );
    }
}