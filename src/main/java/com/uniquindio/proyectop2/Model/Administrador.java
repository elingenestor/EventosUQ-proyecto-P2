package com.uniquindio.proyectop2.Model;

public class Administrador extends Usuario {

    public Administrador() {
        super();
        setAdmin(true);
    }

    public Administrador(String idUsuario, String nombreCompleto, String email, String password, String telefono) {
        super(idUsuario, nombreCompleto, email, password, telefono, true);
    }
}