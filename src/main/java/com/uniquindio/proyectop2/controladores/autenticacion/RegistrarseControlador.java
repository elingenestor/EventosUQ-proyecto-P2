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

public class RegistrarseControlador {

    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private PasswordField txtContrasena;
    @FXML private PasswordField txtConfirmarContrasena;

    private final UsuarioService usuarioService = new UsuarioServiceImpl(
            DAOFactory.crearUsuarioDAO(),
            DAOFactory.crearMetodoPagoDAO()
    );

    @FXML
    private void registrarUsuario() {
        String nombre = txtNombreCompleto.getText() == null ? "" : txtNombreCompleto.getText().trim();
        String correo = txtCorreo.getText() == null ? "" : txtCorreo.getText().trim();
        String telefono = txtTelefono.getText() == null ? "" : txtTelefono.getText().trim();
        String contrasena = txtContrasena.getText() == null ? "" : txtContrasena.getText();
        String confirmar = txtConfirmarContrasena.getText() == null ? "" : txtConfirmarContrasena.getText();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Debes completar los campos obligatorios.");
            return;
        }

        if (!contrasena.equals(confirmar)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Contraseñas no coinciden", "Verifica la contraseña y su confirmación.");
            return;
        }

        try {
            Usuario usuario = new Usuario();
            usuario.setNombreCompleto(nombre);
            usuario.setEmail(correo);
            usuario.setTelefono(telefono);
            usuario.setPassword(contrasena);

            usuarioService.registrar(usuario);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso", "El usuario fue creado correctamente.");
            volverALogin();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al registrar", e.getMessage());
        }
    }

    @FXML
    private void volverALogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/proyectop2/vistas/autenticacion/iniciar_sesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtNombreCompleto.getScene().getWindow();
            stage.setScene(new Scene(root, 980, 620));
            stage.setTitle("Eventos UQ - Iniciar sesión");
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo volver al inicio de sesión.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
