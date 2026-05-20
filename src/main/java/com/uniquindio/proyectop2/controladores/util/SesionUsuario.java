package com.uniquindio.proyectop2.controladores.util;

import com.uniquindio.proyectop2.Model.Usuario;

/**
 * Sesión simple para mantener el usuario autenticado en la interfaz.
 */
public final class SesionUsuario {

    private static Usuario usuarioActual;

    private SesionUsuario() {
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuarioActual) {
        SesionUsuario.usuarioActual = usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
