package com.uniquindio.proyectop2.controladores.administrador;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PerfilAdminControlador {

    @FXML
    private Label lblIdUsuario;

    @FXML
    private TextField txtNombreCompleto;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefono;

    @FXML
    private PasswordField txtPasswordActual;

    @FXML
    private PasswordField txtNuevaPassword;

    @FXML
    private PasswordField txtConfirmarPassword;

    @FXML
    private Button btnCerrarSesion;

    private final UsuarioService usuarioService =
            new UsuarioServiceImpl(
                    DAOFactory.obtenerUsuarioDAO(),
                    DAOFactory.obtenerMetodoPagoDAO()
            );

    private Usuario adminActual;

    @FXML
    private void initialize() {

        adminActual = SesionActual.getUsuarioActual();

        if(adminActual != null) {

            cargarDatosEnPantalla();
        }
    }

    private void cargarDatosEnPantalla() {

        lblIdUsuario.setText(
                adminActual.getIdUsuario()
        );

        txtNombreCompleto.setText(
                adminActual.getNombreCompleto()
        );

        txtEmail.setText(
                adminActual.getEmail()
        );

        txtTelefono.setText(
                adminActual.getTelefono()
        );
    }

    @FXML
    private void guardarCambios() {

        try {

            if(adminActual == null) {

                mostrarAlerta(
                        "Error",
                        "No hay administrador cargado"
                );

                return;
            }

            if(!txtNuevaPassword.getText()
                    .equals(txtConfirmarPassword.getText())) {

                mostrarAlerta(
                        "Error",
                        "Las contraseñas no coinciden"
                );

                return;
            }

            adminActual.setNombreCompleto(
                    txtNombreCompleto.getText()
            );

            adminActual.setEmail(
                    txtEmail.getText()
            );

            adminActual.setTelefono(
                    txtTelefono.getText()
            );

            usuarioService.actualizarPerfil(
                    adminActual
            );

            // CAMBIAR PASSWORD SOLO SI ESCRIBIÓ UNA NUEVA
            if(!txtNuevaPassword.getText().isBlank()) {

                usuarioService.actualizarPassword(
                        adminActual,
                        txtPasswordActual.getText(),
                        txtNuevaPassword.getText()
                );
            }

            SesionActual.setUsuarioActual(
                    adminActual
            );

            mostrarInformacion(
                    "Perfil actualizado",
                    "Los cambios fueron guardados correctamente."
            );

        } catch (Exception e) {

            mostrarAlerta(
                    "Error",
                    e.getMessage()
            );
        }
    }

    @FXML
    private void volver() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/com/uniquindio/proyectop2/vistas/administrador/panel_administrador.fxml"
                            )
                    );

            Parent root = loader.load();

            Stage stage =
                    (Stage) btnCerrarSesion
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
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
    private void mostrarAlerta(
            String titulo,
            String mensaje
    ) {

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle(titulo);

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    private void mostrarInformacion(
            String titulo,
            String mensaje
    ) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(titulo);

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}
