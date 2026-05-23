package com.uniquindio.proyectop2.controladores.administrador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PanelAdministradorControlador {

    @FXML
    private StackPane contenedorPrincipal;
    @FXML private Button btnPerfil;

    @FXML
    private void initialize() {
        cargarVistaUsuarios();
    }

    @FXML
    private void cargarVistaUsuarios() {
        cargarVista("/com/uniquindio/proyectop2/vistas/administrador/gestion_usuarios.fxml");
    }

    @FXML
    private void cargarVistaEventos() {
        cargarVista("/com/uniquindio/proyectop2/vistas/administrador/gestion_eventos.fxml");
    }

    @FXML
    private void cargarVistaRecintos() {
        cargarVista("/com/uniquindio/proyectop2/vistas/administrador/gestion_recintos.fxml");
    }

    @FXML
    private void cargarVistaReportes() {
        cargarVista("/com/uniquindio/proyectop2/vistas/administrador/gestion_reportes.fxml");
    }

    private void cargarVista(String recurso) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(recurso));
            contenedorPrincipal.getChildren().setAll(vista);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar la vista: " + recurso, e);
        }
    }
    @FXML
    private void abrirPerfil() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/com/uniquindio/proyectop2/vistas/administrador/perfil_admin.fxml"
                            )
                    );

            Parent root = loader.load();

            Stage stage =
                    (Stage) btnPerfil
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
