package com.uniquindio.proyectop2.controladores.autenticacion;

import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.impl.UsuarioServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.UsuarioService;
import com.uniquindio.proyectop2.util.SesionActual;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class IniciarSesionControlador {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    private final UsuarioService usuarioService = new UsuarioServiceImpl(
            DAOFactory.crearUsuarioDAO(),
            DAOFactory.crearMetodoPagoDAO()
    );

    @FXML
    private void iniciarSesion() {
        String correo = txtCorreo.getText() == null ? "" : txtCorreo.getText().trim();
        String contrasena = txtContrasena.getText() == null ? "" : txtContrasena.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Debes completar el correo y la contraseña.");
            return;
        }

        try {
            // 1. Autenticamos el usuario con tu servicio robusto
            Usuario usuario = usuarioService.login(correo, contrasena);

            // 2. Encendemos tu nuevo Singleton seguro y Thread-Safe
            SesionActual.getInstance().setUsuarioActual(usuario);

            // 3. Ruteo inteligente por Roles usando la nueva variable 'admin'
            if (usuario.isAdmin()) {
                // Si es Admin, lo mandamos a la vista de administración
                abrirVentana("/com/uniquindio/proyectop2/vistas/administrador/panel_administrador.fxml", "Panel Administrador - Eventos UQ");
            } else {
                // Si es Cliente común, lo mandamos a tu ventana principal unificada
                abrirVentana("/com/uniquindio/proyectop2/vistas/principal/ventana_principal.fxml", "Eventos UQ - Panel Principal");
            }

        } catch (Exception e) {
            String mensaje = e.getMessage();
            if (mensaje == null || mensaje.isBlank()) {
                mensaje = e.toString();
            }
            mostrarAlerta(Alert.AlertType.ERROR, "Error al iniciar sesión", mensaje);
        }
    }

    @FXML
    private void irARegistro() {
        try {
            abrirVentana("/com/uniquindio/proyectop2/vistas/autenticacion/registrarse.fxml", "Registro - Eventos UQ");
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la pantalla de registro.");
        }
    }

    /**
     * Método de utilidad interno para cambiar de ventana limpiamente sin repetir código largo
     */
    private void abrirVentana(String rutaFXML, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
        Parent root = loader.load();

        // Si la ventana de destino requiere inicialización manual (como la principal que armamos),
        // le pasamos el usuario. Si es la de admin, el Singleton se encargará de darle los datos allá.
        Object controladorDestino = loader.getController();
        if (controladorDestino instanceof com.uniquindio.proyectop2.controladores.principal.VentanaPrincipalControlador) {
            Usuario usuarioLogueado = SesionActual.getInstance().getUsuarioActual();
            ((com.uniquindio.proyectop2.controladores.principal.VentanaPrincipalControlador) controladorDestino).inicializar(usuarioLogueado);
        }

        Stage stage = (Stage) txtCorreo.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.centerOnScreen(); // Mantiene la aplicación centrada estéticamente en el monitor
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
