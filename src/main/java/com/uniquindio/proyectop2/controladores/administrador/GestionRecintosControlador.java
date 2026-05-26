package com.uniquindio.proyectop2.controladores.administrador;

import com.uniquindio.proyectop2.Model.Recinto;
import com.uniquindio.proyectop2.Model.Zona;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.interfaces.AdminService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class GestionRecintosControlador {

    @FXML private TableView<Recinto> tablaRecintos;
    @FXML private TableColumn<Recinto, String> colId;
    @FXML private TableColumn<Recinto, String> colNombre;
    @FXML private TableColumn<Recinto, String> colDireccion;
    @FXML private TableColumn<Recinto, String> colCiudad;

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCiudad;

    @FXML private TextField txtNombreZona;
    @FXML private TextField txtCapacidadZona;
    @FXML private TextField txtPrecioZona;
    @FXML private ListView<String> listaZonasVisuales;

    private final AdminService adminService = DAOFactory.getAdminService();
    private final ObservableList<Recinto> recintos = FXCollections.observableArrayList();

    private final ArrayList<Zona> zonasTemporales = new ArrayList<>();

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

                listaZonasVisuales.getItems().clear();
                zonasTemporales.clear();
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
    private void agregarZonaAListaTemporal() {
        try {
            String nombreZ = txtNombreZona.getText() != null ? txtNombreZona.getText().trim() : "";
            String capacidadStr = txtCapacidadZona.getText() != null ? txtCapacidadZona.getText().trim() : "";
            String precioStr = txtPrecioZona.getText() != null ? txtPrecioZona.getText().trim() : "";

            if (nombreZ.isBlank() || capacidadStr.isBlank() || precioStr.isBlank()) {
                mostrarError("Debes completar el nombre, capacidad y precio base de la zona.");
                return;
            }

            int capacidad = Integer.parseInt(capacidadStr);
            double precioBase = Double.parseDouble(precioStr);

            Zona nuevaZona = new Zona();
            nuevaZona.setNombre(nombreZ);
            nuevaZona.setCapacidad(capacidad);
            nuevaZona.setPrecioBase(precioBase);

            zonasTemporales.add(nuevaZona);

            listaZonasVisuales.getItems().add(nombreZ + " (Capacidad: " + capacidad + " | Precio: $" + precioBase + ")");

            txtNombreZona.clear();
            txtCapacidadZona.clear();
            txtPrecioZona.clear();

        } catch (NumberFormatException e) {
            mostrarError("La capacidad debe ser un número entero y el precio un valor decimal válido.");
        }
    }

    @FXML
    private void guardarRecinto() {
        try {
            String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
            String direccion = txtDireccion.getText() != null ? txtDireccion.getText().trim() : "";
            String ciudad = txtCiudad.getText() != null ? txtCiudad.getText().trim() : "";

            if (nombre.isBlank() || direccion.isBlank() || ciudad.isBlank()) {
                mostrarError("El nombre, dirección y ciudad del recinto son obligatorios.");
                return;
            }

            Recinto recinto = new Recinto();
            recinto.setIdRecinto(txtId.getText());
            recinto.setNombre(nombre);
            recinto.setDireccion(direccion);
            recinto.setCiudad(ciudad);
            recinto.setZonas(zonasTemporales);

            if (recinto.getIdRecinto() == null || recinto.getIdRecinto().trim().isEmpty()) {
                if (zonasTemporales.isEmpty()) {
                    mostrarError("Debes añadir al menos una zona/localidad para poder crear el recinto.");
                    return;
                }
                adminService.crearRecinto(recinto); // Delega la persistencia en cascada al servicio
                mostrarInfo("Recinto creado con éxito. Zonas y asientos generados en la base de datos.");
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
        txtNombreZona.clear();
        txtCapacidadZona.clear();
        txtPrecioZona.clear();
        listaZonasVisuales.getItems().clear();
        zonasTemporales.clear();
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
