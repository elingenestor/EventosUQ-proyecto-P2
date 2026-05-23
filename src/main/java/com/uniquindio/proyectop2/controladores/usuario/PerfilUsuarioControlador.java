package com.uniquindio.proyectop2.controladores.usuario;

import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.impl.UsuarioServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.UsuarioService;
import com.uniquindio.proyectop2.util.SesionActual;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class PerfilUsuarioControlador {

    @FXML private Label lblIdUsuario;
    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private PasswordField txtPasswordActual;
    @FXML private PasswordField txtNuevaPassword;
    @FXML private PasswordField txtConfirmarPassword;
    @FXML private Button btnCerrarSesion;

    private final UsuarioService usuarioService = new UsuarioServiceImpl(DAOFactory.obtenerUsuarioDAO(), DAOFactory.obtenerMetodoPagoDAO()
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
            if (txtPasswordActual != null) txtPasswordActual.setText(usuarioActual.getPassword());
        }
    }

    @FXML
    private void guardarCambios() {
        if (usuarioActual == null) {
            mostrarAlerta("Error", "No hay usuario cargado.");
            return;
        }
        if (!txtNuevaPassword.getText().equals(txtConfirmarPassword.getText())) {

            mostrarAlerta("Error", "Las contraseñas no coinciden");
            return;
        }

        usuarioActual.setNombreCompleto(txtNombreCompleto.getText());
        usuarioActual.setEmail(txtEmail.getText());
        usuarioActual.setTelefono(txtTelefono.getText());
        usuarioActual.setPassword(txtPasswordActual.getText());

        try {

            usuarioService.actualizarPassword(
                    usuarioActual,
                    txtPasswordActual.getText(),
                    txtNuevaPassword.getText()
            );

            mostrarInformacion("Contraseña actualizada","Contraseña actualizada");

        } catch (Exception e) {

            mostrarAlerta("Error al actualizar", "No se ha podido actualizar la contraseña");
        }
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
    @FXML
    private void cerrarSesion() {

        try {

            // LIMPIAR SESIÓN
            SesionActual.cerrarSesion();

            // ABRIR LOGIN
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/uniquindio/proyectop2/vistas/autenticacion/iniciar_sesion.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // CERRAR VENTANA ACTUAL
            Stage actual = (Stage) btnCerrarSesion
                    .getScene()
                    .getWindow();

            actual.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
