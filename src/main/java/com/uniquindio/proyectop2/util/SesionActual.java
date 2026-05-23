package com.uniquindio.proyectop2.util;

import com.uniquindio.proyectop2.Model.Usuario;

public final class SesionActual {

    private static SesionActual instance;
    private static Usuario usuarioActual;

    private SesionActual() {
    }

    public static synchronized SesionActual getInstance() {
        if (instance == null) {
            instance = new SesionActual();
        }
        return instance;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuarioActual) {
        usuarioActual = usuarioActual;
    }

    public boolean haySesionActiva() {
        return usuarioActual != null;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}