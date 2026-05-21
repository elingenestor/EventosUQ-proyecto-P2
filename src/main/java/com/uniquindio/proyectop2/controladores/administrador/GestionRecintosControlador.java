package com.uniquindio.proyectop2.controladores.administrador;

import com.uniquindio.proyectop2.Model.Recinto;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.interfaces.AdminService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionRecintosControlador {

    @FXML
    private TableView<Recinto> tablaRecintos;
    @FXML
    private TableColumn<Recinto, String> colId;
    @FXML
    private TableColumn<Recinto, String> colNombre;
    @FXML
    private TableColumn<Recinto, String> colDireccion;
    @FXML
    private TableColumn<Recinto, String> colCiudad;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtCiudad;

    private final AdminService adminService = DAOFactory.getAdminService();
    private final ObservableList<Recinto> recintos = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<Recinto, String>("idRecinto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Recinto, String>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Recinto, String>("direccion"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<Recinto, String>("ciudad"));

        tablaRecintos.setItems(recintos);
        tablaRecintos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                txtId.setText(seleccionado.getIdRecinto());
                txtNombre.setText(seleccionado.getNombre());
                txtDireccion.setText(seleccionado.getDireccion());
                txtCiudad.setText(seleccionado.getCiudad());
            }
        });
        cargarRecintos();
    }

    @FXML
    private void cargarRecintos() {
        try {
            recintos.setAll(adminService.listarRecintos());
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void guardarRecinto() {
        try {
            Recinto recinto = new Recinto();
            recinto.setIdRecinto(txtId.getText());
            recinto.setNombre(txtNombre.getText());
            recinto.setDireccion(txtDireccion.getText());
            recinto.setCiudad(txtCiudad.getText());

            if (recinto.getIdRecinto() == null || recinto.getIdRecinto().trim().isEmpty()) {
                adminService.crearRecinto(recinto);
                mostrarInfo("Recinto creado correctamente");
            } else {
                adminService.actualizarRecinto(recinto);
                mostrarInfo("Recinto actualizado correctamente");
            }
            limpiarFormulario();
            cargarRecintos();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void eliminarRecinto() {
        try {
            String id = txtId.getText();
            if (id == null || id.trim().isEmpty()) {
                mostrarError("Selecciona un recinto para eliminar");
                return;
            }
            adminService.eliminarRecinto(id);
            mostrarInfo("Recinto eliminado correctamente");
            limpiarFormulario();
            cargarRecintos();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        txtDireccion.clear();
        txtCiudad.clear();
        tablaRecintos.getSelectionModel().clearSelection();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
