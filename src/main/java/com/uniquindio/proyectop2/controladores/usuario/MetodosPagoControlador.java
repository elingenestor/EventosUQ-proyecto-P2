package com.uniquindio.proyectop2.controladores.usuario;

import com.uniquindio.proyectop2.Enums.MetodosPago;
import com.uniquindio.proyectop2.Model.MetodoPago;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.util.SesionActual;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.impl.UsuarioServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.UsuarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MetodosPagoControlador {

    @FXML private TableView<MetodoPago> tablaMetodosPago;
    @FXML private TableColumn<MetodoPago, String> colId;
    @FXML private TableColumn<MetodoPago, String> colTipo;
    @FXML private TableColumn<MetodoPago, String> colNumero;
    @FXML private TableColumn<MetodoPago, String> colTitular;

    @FXML private ComboBox<MetodosPago> comboTipo;
    @FXML private TextField txtNumero;
    @FXML private TextField txtTitular;

    private final UsuarioService usuarioService = new UsuarioServiceImpl(
            DAOFactory.obtenerUsuarioDAO(),
            DAOFactory.obtenerMetodoPagoDAO()
    );

    private Usuario usuarioActual;

    @FXML
    private void initialize() {
        // 👇 CORREGIDO: Usamos tu nuevo Singleton seguro
        usuarioActual = SesionActual.getInstance().getUsuarioActual();

        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("idMetodoPago"));
        colTipo.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("tipo"));
        colNumero.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("numero"));
        colTitular.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("titular"));

        comboTipo.setItems(FXCollections.observableArrayList(MetodosPago.values()));

        cargarMetodos();
    }

    private void cargarMetodos() {
        if (usuarioActual == null) {
            tablaMetodosPago.setItems(FXCollections.observableArrayList());
            return;
        }

        try {
            List<MetodoPago> metodos = usuarioService.listarMetodosPago(usuarioActual);
            ObservableList<MetodoPago> datos = FXCollections.observableArrayList(metodos);
            tablaMetodosPago.setItems(datos);
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    private void agregarMetodoPago() {
        if (usuarioActual == null) {
            mostrarAlerta("Sesión no encontrada", "No hay usuario autenticado.");
            return;
        }

        if (comboTipo.getValue() == null || txtNumero.getText().isBlank() || txtTitular.getText().isBlank()) {
            mostrarAlerta("Datos incompletos", "Completa tipo, número y titular.");
            return;
        }

        MetodoPago metodo = new MetodoPago();
        metodo.setTipo(comboTipo.getValue());
        metodo.setNumero(txtNumero.getText().trim());
        metodo.setTitular(txtTitular.getText().trim());

        try {
            usuarioService.agregarMetodoPago(usuarioActual, metodo);
            limpiarFormulario();
            cargarMetodos();
            mostrarInformacion("Éxito", "Método de pago agregado correctamente.");
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    private void eliminarSeleccionado() {
        MetodoPago seleccionado = tablaMetodosPago.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un método", "Debes elegir un método de pago de la tabla.");
            return;
        }

        try {
            DAOFactory.obtenerMetodoPagoDAO().delete(seleccionado.getIdMetodoPago());
            cargarMetodos();
            mostrarInformacion("Eliminado", "El método de pago fue eliminado.");
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    private void volver() {
        Stage stage = (Stage) tablaMetodosPago.getScene().getWindow();
        stage.close();
    }

    private void limpiarFormulario() {
        comboTipo.getSelectionModel().clearSelection();
        txtNumero.clear();
        txtTitular.clear();
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

    public static void abrirVentana(Usuario usuario) {
        try {
            // 👇 CORREGIDO: Usamos tu nuevo Singleton seguro para mantener la ventana sincronizada
            SesionActual.getInstance().setUsuarioActual(usuario);

            FXMLLoader loader = new FXMLLoader(
                    MetodosPagoControlador.class.getResource(
                            "/com/uniquindio/proyectop2/vistas/usuarios/metodos_pago.fxml"
                    )
            );
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Métodos de pago");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo abrir la ventana de métodos de pago", e);
        }
    }
}
