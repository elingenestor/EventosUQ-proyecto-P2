package com.uniquindio.proyectop2.Model;

public class Administrador extends Usuario {
    private String rol;

    public Administrador() {
        super();
    }

    public Administrador(String idUsuario, String nombreCompleto, String email, String password, String telefono) {
        super(idUsuario, nombreCompleto, email, password, telefono);
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
