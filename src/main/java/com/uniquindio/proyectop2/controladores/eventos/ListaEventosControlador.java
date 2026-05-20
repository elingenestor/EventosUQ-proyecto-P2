package com.uniquindio.proyectop2.controladores.eventos;

import com.uniquindio.proyectop2.Enums.CategoriaEvento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.impl.EventoServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.EventoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListaEventosControlador {

    @FXML private TextField txtCiudad;
    @FXML private ComboBox<CategoriaEvento> cmbCategoria;
    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;

    @FXML private TableView<Evento> tblEventos;
    @FXML private TableColumn<Evento, String> colNombre;
    @FXML private TableColumn<Evento, String> colCategoria;
    @FXML private TableColumn<Evento, String> colCiudad;
    @FXML private TableColumn<Evento, LocalDate> colFecha;
    @FXML private TableColumn<Evento, String> colEstado;

    @FXML private Label lblTituloDetalle;
    @FXML private Label lblDescripcionDetalle;
    @FXML private Label lblRecintoDetalle;
    @FXML private Label lblFechaDetalle;
    @FXML private Label lblEstadoDetalle;

    private final EventoService eventoService = new EventoServiceImpl(
            DAOFactory.crearEventoDAO(),
            DAOFactory.crearAsientoDAO()
    );

    private final ObservableList<Evento> eventos = FXCollections.observableArrayList();
    private Usuario usuario;

    @FXML
    private void initialize() {
        cmbCategoria.setItems(FXCollections.observableArrayList(CategoriaEvento.values()));
        cmbCategoria.setPromptText("Todas");

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getCategoria() != null ? cell.getValue().getCategoria().name() : ""
        ));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colFecha.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getFechaHora() != null
                ? cell.getValue().getFechaHora().toLocalDate()
                : null));
        colEstado.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getEstado() != null ? cell.getValue().getEstado().name() : ""
        ));

        tblEventos.setItems(eventos);
        tblEventos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> mostrarDetalle(seleccionado));

        cargarEventos();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private void buscarEventos() {
        cargarEventos();
    }

    @FXML
    private void limpiarFiltros() {
        txtCiudad.clear();
        cmbCategoria.setValue(null);
        dpDesde.setValue(null);
        dpHasta.setValue(null);
        cargarEventos();
    }

    @FXML
    private void refrescarEventos() {
        cargarEventos();
    }

    @FXML
    private void abrirDetalleSeleccionado() {
        Evento seleccionado = tblEventos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecciona un evento", "Debes elegir un evento de la tabla.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/proyectop2/vistas/eventos/detalle_evento.fxml"));
            Parent root = loader.load();
            DetalleEventoControlador controlador = loader.getController();
            controlador.setUsuario(usuario);
            controlador.inicializar(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Detalle del evento");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 980, 700));
            stage.showAndWait();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir el detalle del evento.");
        }
    }

    private void cargarEventos() {
        try {
            String ciudad = txtCiudad.getText() == null ? null : txtCiudad.getText().trim();
            CategoriaEvento categoria = cmbCategoria.getValue();
            LocalDate desde = dpDesde.getValue();
            LocalDate hasta = dpHasta.getValue();

            List<Evento> resultado = eventoService.listarEventosDisponibles(ciudad, categoria, desde, hasta);
            eventos.setAll(resultado);

            if (!eventos.isEmpty()) {
                tblEventos.getSelectionModel().selectFirst();
            } else {
                mostrarDetalle(null);
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar eventos", e.getMessage());
        }
    }

    private void mostrarDetalle(Evento evento) {
        if (evento == null) {
            lblTituloDetalle.setText("Selecciona un evento");
            lblDescripcionDetalle.setText("Aquí aparecerá la información del evento.");
            lblRecintoDetalle.setText("-");
            lblFechaDetalle.setText("-");
            lblEstadoDetalle.setText("-");
            return;
        }

        lblTituloDetalle.setText(evento.getNombre());
        lblDescripcionDetalle.setText(evento.getDescripcion() != null ? evento.getDescripcion() : "");
        lblRecintoDetalle.setText(evento.getRecinto() != null ? evento.getRecinto().getNombre() : "Sin recinto");
        lblFechaDetalle.setText(evento.getFechaHora() != null
                ? evento.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "-");
        lblEstadoDetalle.setText(evento.getEstado() != null ? evento.getEstado().name() : "-");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
