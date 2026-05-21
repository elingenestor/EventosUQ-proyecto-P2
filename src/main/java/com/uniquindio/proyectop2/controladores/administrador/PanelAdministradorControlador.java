package com.uniquindio.proyectop2.controladores.administrador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class PanelAdministradorControlador {

    @FXML
    private StackPane contenedorPrincipal;

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
}