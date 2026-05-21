package com.uniquindio.proyectop2.controladores.principal;

import com.uniquindio.proyectop2.Model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InicioControlador {

    @FXML
    private Label lblMensaje;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (lblMensaje != null && usuario != null) {
            lblMensaje.setText("Explora los eventos disponibles desde el panel principal, " + usuario.getNombreCompleto() + ".");
        }
    }

    @FXML
    private void initialize() {
        if (lblMensaje != null) {
            lblMensaje.setText("Explora los eventos disponibles desde el panel principal.");
        }
    }
}
