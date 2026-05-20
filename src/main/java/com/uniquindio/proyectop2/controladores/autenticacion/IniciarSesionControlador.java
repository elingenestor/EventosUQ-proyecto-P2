package com.uniquindio.proyectop2.controladores.autenticacion;

import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.impl.UsuarioServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.UsuarioService;
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
            // Buscamos el usuario en el servicio
            Usuario usuario = usuarioService.login(correo, contrasena);

            // Saltamos de ventana pasándole el usuario manualmente (Sin SesionUsuario)
            abrirVentanaPrincipal(usuario);

        } catch (Exception e) {
            // Manejo de errores robusto del segundo código para evitar alertas vacías
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/proyectop2/vistas/autenticacion/registrarse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            stage.setScene(new Scene(root, 980, 620));
            stage.setTitle("Registro - Eventos UQ");
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la pantalla de registro.");
        }
    }

    private void abrirVentanaPrincipal(Usuario usuario) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/proyectop2/vistas/principal/ventana_principal.fxml"));
        Parent root = loader.load();
        com.uniquindio.proyectop2.controladores.principal.VentanaPrincipalControlador controlador = loader.getController();
        controlador.inicializar(usuario);
        Stage stage = (Stage) txtCorreo.getScene().getWindow();
        stage.setScene(new Scene(root, 1100, 700));
        stage.setTitle("Eventos UQ - Panel principal");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
