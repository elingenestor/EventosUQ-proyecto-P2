package com.uniquindio.proyectop2.controladores.usuario;

import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.impl.UsuarioServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PerfilUsuarioControlador {

    @FXML private Label lblIdUsuario;
    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private PasswordField txtPassword;

    private final UsuarioService usuarioService = new UsuarioServiceImpl(
            DAOFactory.obtenerUsuarioDAO(),
            DAOFactory.obtenerMetodoPagoDAO()
    );

    private Usuario usuarioActual;

    /**
     * PASO 1: El receptor manual del usuario conectado
     */
    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;

        if (usuarioActual != null) {
            cargarDatosEnPantalla();
        }
    }

    /**
     * PASO 2: Deja el initialize limpio para evitar la alerta automática
     */
    @FXML
    private void initialize() {
        // No cargamos nada al arrancar de forma ciega
    }

    /**
     * PASO 3: Método auxiliar para pintar la interfaz
     */
    private void cargarDatosEnPantalla() {
        if (usuarioActual != null) {
            if (lblIdUsuario != null) {
                lblIdUsuario.setText(usuarioActual.getIdUsuario() == null ? "" : usuarioActual.getIdUsuario());
            }
            if (txtNombreCompleto != null) txtNombreCompleto.setText(usuarioActual.getNombreCompleto());
            if (txtEmail != null) txtEmail.setText(usuarioActual.getEmail());
            if (txtTelefono != null) txtTelefono.setText(usuarioActual.getTelefono());
            if (txtPassword != null) txtPassword.setText(usuarioActual.getPassword());
        }
    }

    @FXML
    private void guardarCambios() {
        if (usuarioActual == null) {
            mostrarAlerta("Error", "No hay usuario cargado.");
            return;
        }

        usuarioActual.setNombreCompleto(txtNombreCompleto.getText());
        usuarioActual.setEmail(txtEmail.getText());
        usuarioActual.setTelefono(txtTelefono.getText());
        usuarioActual.setPassword(txtPassword.getText());

        try {
            usuarioService.actualizarPerfil(usuarioActual);
            mostrarInformacion("Perfil actualizado", "Los cambios fueron guardados correctamente.");
        } catch (Exception e) {
            mostrarAlerta("Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void abrirMetodosPago() {
        MetodosPagoControlador.abrirVentana(usuarioActual);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
