package com.uniquindio.proyectop2.controladores.administrador;

import com.uniquindio.proyectop2.Enums.CategoriaEvento;
import com.uniquindio.proyectop2.Enums.EstadoEvento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.impl.EventoServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.EventoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GestionEventosControlador {

    @FXML private TableView<Evento> tablaEventos;
    @FXML private TableColumn<Evento, String> colId;
    @FXML private TableColumn<Evento, String> colNombre;
    @FXML private TableColumn<Evento, String> colCategoria;
    @FXML private TableColumn<Evento, String> colCiudad;
    @FXML private TableColumn<Evento, String> colFechaHora;
    @FXML private TableColumn<Evento, String> colEstado;

    @FXML private ComboBox<com.uniquindio.proyectop2.Model.Recinto> cbRecinto;
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<CategoriaEvento> cbCategoria;
    @FXML private TextField txtCiudad;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private ComboBox<EstadoEvento> cbEstado;
    @FXML private TextArea txtDescripcion;
    @FXML private TextArea txtPoliticas;

    // CONSTRUCTOR ADAPTADO CON LOS DOS DAOS EXIGIDOS
    private final EventoService eventoService =
            new EventoServiceImpl(DAOFactory.crearEventoDAO(), DAOFactory.crearAsientoDAO());

    private final ObservableList<Evento> eventos = FXCollections.observableArrayList();
    private final DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEvento"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategoria() != null ? cell.getValue().getCategoria().name() : ""));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colFechaHora.setCellValueFactory(cell -> {
            LocalDateTime fecha = cell.getValue().getFechaHora();
            return new SimpleStringProperty(fecha != null ? fecha.format(formateador) : "");
        });
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstado() != null ? cell.getValue().getEstado().name() : ""));

        cbCategoria.setItems(FXCollections.observableArrayList(CategoriaEvento.values()));
        cbEstado.setItems(FXCollections.observableArrayList(EstadoEvento.values()));

        com.uniquindio.proyectop2.dao.impl.RecintoDAOImpl recintoDAO = new com.uniquindio.proyectop2.dao.impl.RecintoDAOImpl();
        cbRecinto.setItems(javafx.collections.FXCollections.observableArrayList(recintoDAO.findAll()));

        tablaEventos.setItems(eventos);
        tablaEventos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) mostrarEventoSeleccionado(seleccionado);
        });

        cargarEventos();
        nuevoEvento();
    }

    @FXML
    private void cargarEventos() {
        try {
            eventos.setAll(eventoService.listarEventosDisponibles(null, null, null, null));
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void nuevoEvento() {
        limpiarFormulario();
        cbEstado.setValue(EstadoEvento.BORRADOR);
    }

    @FXML
    private void guardarEvento() {
        try {
            Evento evento = construirEventoDesdeFormulario();
            if (txtId.getText() == null || txtId.getText().isBlank()) {
                eventoService.crearEvento(evento);
                mostrarInfo("Evento creado correctamente.");
            } else {
                eventoService.actualizarEvento(evento);
                mostrarInfo("Evento actualizado correctamente.");
            }
            cargarEventos();
            nuevoEvento();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    // ACCIÓN CAMBIADA A CANCELAR EVENTO CONTROLADO
    @FXML
    private void cancelarEvento() {
        try {
            String id = txtId.getText();
            if (id == null || id.isBlank()) {
                mostrarError("Selecciona un evento para cancelar.");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Cancelación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Seguro que deseas cancelar este evento? Esta acción notificará a los usuarios.");

            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                eventoService.cancelarEvento(id);
                cargarEventos();
                nuevoEvento();
                mostrarInfo("Evento cancelado con éxito.");
            }
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        cbCategoria.getSelectionModel().clearSelection();
        txtCiudad.clear();
        dpFecha.setValue(null);
        txtHora.clear();
        cbEstado.getSelectionModel().clearSelection();
        cbRecinto.getSelectionModel().clearSelection();
        txtDescripcion.clear();
        txtPoliticas.clear();
        tablaEventos.getSelectionModel().clearSelection();
    }

    private Evento construirEventoDesdeFormulario() throws Exception {
        String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        String ciudad = txtCiudad.getText() != null ? txtCiudad.getText().trim() : "";
        String horaTexto = txtHora.getText() != null ? txtHora.getText().trim() : "";

        if (nombre.isBlank() || ciudad.isBlank() || horaTexto.isBlank() || dpFecha.getValue() == null || cbRecinto.getValue() == null) {
            throw new Exception("Todos los campos principales son obligatorios, incluyendo el recinto.");
        }

        LocalTime hora = LocalTime.parse(horaTexto);
        LocalDate fecha = dpFecha.getValue();

        Evento evento = new Evento();
        evento.setIdEvento(txtId.getText() != null ? txtId.getText().trim() : null);
        evento.setNombre(nombre);
        evento.setCategoria(cbCategoria.getValue());
        evento.setCiudad(ciudad);
        evento.setFechaHora(LocalDateTime.of(fecha, hora));
        evento.setEstado(cbEstado.getValue());
        evento.setDescripcion(txtDescripcion.getText() != null ? txtDescripcion.getText().trim() : "");
        evento.setPoliticasCancelacion(txtPoliticas.getText() != null ? txtPoliticas.getText().trim() : "");

        evento.setRecinto(cbRecinto.getValue());

        return evento;
    }


    private void mostrarEventoSeleccionado(Evento evento) {
        txtId.setText(evento.getIdEvento());
        txtNombre.setText(evento.getNombre());
        cbCategoria.setValue(evento.getCategoria());
        txtCiudad.setText(evento.getCiudad());

        if (evento.getFechaHora() != null) {
            dpFecha.setValue(evento.getFechaHora().toLocalDate());
            txtHora.setText(evento.getFechaHora().toLocalTime().toString());
        } else {
            dpFecha.setValue(null);
            txtHora.clear();
        }

        cbEstado.setValue(evento.getEstado());
        txtDescripcion.setText(evento.getDescripcion());
        txtPoliticas.setText(evento.getPoliticasCancelacion());

        if (evento.getRecinto() != null) {
            cbRecinto.setValue(evento.getRecinto());
        } else {
            cbRecinto.getSelectionModel().clearSelection();
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alert.showAndWait();
    }
}
