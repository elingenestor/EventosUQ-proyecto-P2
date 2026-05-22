package com.uniquindio.proyectop2.controladores.eventos;

import com.uniquindio.proyectop2.controladores.compras.SeleccionAsientosControlador;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.Model.Zona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DetalleEventoControlador {

    @FXML private Label lblNombre;
    @FXML private Label lblCategoria;
    @FXML private Label lblCiudad;
    @FXML private Label lblFecha;
    @FXML private Label lblEstado;
    @FXML private Label lblRecinto;
    @FXML private Label lblDireccionRecinto;
    @FXML private Label lblPoliticas;
    @FXML private TextArea txtDescripcion;
    @FXML private TableView<Zona> tblZonas;
    @FXML private TableColumn<Zona, String> colZonaNombre;
    @FXML private TableColumn<Zona, Integer> colCapacidad;
    @FXML private TableColumn<Zona, Double> colPrecioBase;

    private final ObservableList<Zona> zonas = FXCollections.observableArrayList();
    private Evento evento;
    private Usuario usuario;

    @FXML
    private void initialize() {
        colZonaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        colPrecioBase.setCellValueFactory(new PropertyValueFactory<>("precioBase"));
        tblZonas.setItems(zonas);
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void inicializar(Evento evento) {
        this.evento = evento;
        if (evento == null) {
            limpiarVista();
            return;
        }

        lblNombre.setText(evento.getNombre() != null ? evento.getNombre() : "Sin nombre");
        lblCategoria.setText("Categoría: " + (evento.getCategoria() != null ? evento.getCategoria().name() : "-"));
        lblCiudad.setText(evento.getCiudad() != null ? evento.getCiudad() : "-");
        lblFecha.setText(evento.getFechaHora() != null
                ? evento.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "-");
        lblEstado.setText(evento.getEstado() != null ? evento.getEstado().name() : "-");
        lblRecinto.setText(evento.getRecinto() != null && evento.getRecinto().getNombre() != null
                ? evento.getRecinto().getNombre()
                : "Sin recinto");
        lblDireccionRecinto.setText(evento.getRecinto() != null && evento.getRecinto().getDireccion() != null
                ? evento.getRecinto().getDireccion()
                : "-");
        lblPoliticas.setText(evento.getPoliticasCancelacion() != null ? evento.getPoliticasCancelacion() : "-");
        txtDescripcion.setText(evento.getDescripcion() != null ? evento.getDescripcion() : "");
        zonas.setAll(evento.getZonas() != null ? evento.getZonas() : java.util.Collections.emptyList());
    }

    @FXML
    private void seleccionarAsientos() {
        if (evento == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin evento", "No hay un evento cargado.");
            return;
        }

        if (this.usuario == null) {
            this.usuario = com.uniquindio.proyectop2.util.SesionActual.getInstance().getUsuarioActual();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/proyectop2/vistas/compras/seleccion_asientos.fxml"));
            Parent root = loader.load();
            SeleccionAsientosControlador controlador = loader.getController();

            controlador.inicializar(usuario, evento);

            Stage stage = new Stage();
            stage.setTitle("Selección de asientos - " + evento.getNombre());
            stage.setScene(new Scene(root, 1050, 720));

            cerrarVentana();

            stage.showAndWait();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la selección de asientos.");
            e.printStackTrace();
        }
    }


    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) lblNombre.getScene().getWindow();
        stage.close();
    }

    private void limpiarVista() {
        lblNombre.setText("-");
        lblCategoria.setText("-");
        lblCiudad.setText("-");
        lblFecha.setText("-");
        lblEstado.setText("-");
        lblRecinto.setText("-");
        lblDireccionRecinto.setText("-");
        lblPoliticas.setText("-");
        txtDescripcion.setText("");
        zonas.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
