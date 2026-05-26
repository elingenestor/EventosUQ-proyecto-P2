package com.uniquindio.proyectop2.controladores.compras;

import com.uniquindio.proyectop2.Model.Asiento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.MetodoPago;
import com.uniquindio.proyectop2.Model.ServicioAdicional;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.Model.Zona;
import com.uniquindio.proyectop2.dao.impl.AsientoDAOImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeleccionAsientosControlador {

    @FXML private Label lblEvento;
    @FXML private Label lblCiudad;
    @FXML private Label lblFecha;
    @FXML private Label lblRecinto;
    @FXML private ComboBox<Zona> cmbZona;

    @FXML private TableView<Asiento> tblAsientosDisponibles;
    @FXML private TableColumn<Asiento, String> colFilaDisponible;
    @FXML private TableColumn<Asiento, Integer> colNumeroDisponible;
    @FXML private TableColumn<Asiento, String> colEstadoDisponible;

    @FXML private TableView<Asiento> tblAsientosSeleccionados;
    @FXML private TableColumn<Asiento, String> colFilaSeleccionado;
    @FXML private TableColumn<Asiento, Integer> colNumeroSeleccionado;
    @FXML private TableColumn<Asiento, String> colZonaSeleccionado;

    @FXML private Label lblTotalEstimado;

    private final ObservableList<Asiento> asientosDisponibles = FXCollections.observableArrayList();
    private final ObservableList<Asiento> asientosSeleccionados = FXCollections.observableArrayList();
    private AsientoDAOImpl asientoDAO;

    private Usuario usuario;
    private Evento evento;
    private boolean modoEdicion = false;
    private String idCompraEdicion;
    private MetodoPago metodoPagoActual;
    private final List<ServicioAdicional> serviciosIniciales = new ArrayList<>();

    @FXML
    private void initialize() {
        colFilaDisponible.setCellValueFactory(new PropertyValueFactory<>("fila"));
        colNumeroDisponible.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colEstadoDisponible.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue() != null && cell.getValue().getEstado() != null
                                ? cell.getValue().getEstado().name()
                                : ""
                )
        );
        tblAsientosDisponibles.setItems(asientosDisponibles);

        colFilaSeleccionado.setCellValueFactory(new PropertyValueFactory<>("fila"));
        colNumeroSeleccionado.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colZonaSeleccionado.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue() != null && cell.getValue().getZona() != null
                                ? cell.getValue().getZona().getNombre()
                                : ""
                )
        );
        tblAsientosSeleccionados.setItems(asientosSeleccionados);

        cmbZona.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Zona item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre() + " ($" + item.getPrecioBase() + ")");
            }
        });

        cmbZona.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Zona item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre() + " ($" + item.getPrecioBase() + ")");
            }
        });

        cmbZona.valueProperty().addListener((obs, anterior, zona) -> cargarAsientosZona(zona));
        asientosSeleccionados.addListener((ListChangeListener<Asiento>) c -> actualizarTotal());

        actualizarTotal();
        tblAsientosSeleccionados.setStyle("-fx-background-color: #0d2a1a; -fx-control-inner-background: #0d2a1a;");
    }

    public void inicializar(Usuario usuario, Evento evento) {
        this.usuario = usuario;
        this.evento = evento;
        this.modoEdicion = false;
        this.idCompraEdicion = null;
        this.metodoPagoActual = null;
        this.serviciosIniciales.clear();
        cargarDatosEvento();
    }

    public void inicializarEdicion(Usuario usuario, Evento evento, List<Asiento> asientosActuales,
                                   List<ServicioAdicional> serviciosActuales, MetodoPago metodoPagoActual,
                                   String idCompra) {
        this.usuario = usuario;
        this.evento = evento;
        this.modoEdicion = true;
        this.idCompraEdicion = idCompra;
        this.metodoPagoActual = metodoPagoActual;

        this.serviciosIniciales.clear();
        if (serviciosActuales != null) {
            this.serviciosIniciales.addAll(serviciosActuales);
        }

        asientosSeleccionados.clear();
        if (asientosActuales != null) {
            asientosSeleccionados.addAll(asientosActuales);
        }

        cargarDatosEvento();
        if (cmbZona.getItems() != null && !cmbZona.getItems().isEmpty()) {
            cmbZona.getSelectionModel().selectFirst();
        }
    }

    private void cargarDatosEvento() {
        if (evento == null) {
            lblEvento.setText("-");
            lblCiudad.setText("-");
            lblFecha.setText("-");
            lblRecinto.setText("-");
            cmbZona.getItems().clear();
            actualizarTotal();
            return;
        }

        lblEvento.setText(evento.getNombre() != null ? evento.getNombre() : "-");
        lblCiudad.setText(evento.getCiudad() != null ? evento.getCiudad() : "-");
        lblFecha.setText(evento.getFechaHora() != null ? evento.getFechaHora().toString() : "-");
        lblRecinto.setText(evento.getRecinto() != null ? evento.getRecinto().getNombre() : "-");

        if (evento.getZonas() != null) {
            cmbZona.setItems(FXCollections.observableArrayList(evento.getZonas()));
            if (!evento.getZonas().isEmpty()) {
                cmbZona.getSelectionModel().selectFirst();
            }
        } else {
            cmbZona.getItems().clear();
        }

        actualizarTotal();
    }

    @FXML
    private void agregarAsiento() {
        Asiento seleccionado = tblAsientosDisponibles.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecciona un asiento", "Debes elegir un asiento disponible.");
            return;
        }

        if (!estaSeleccionado(seleccionado)) {
            asientosSeleccionados.add(seleccionado);
            asientosDisponibles.remove(seleccionado);
            actualizarTotal();
        }
    }

    @FXML
    private void quitarAsiento() {
        Asiento seleccionado = tblAsientosSeleccionados.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecciona un asiento", "Debes elegir un asiento de la selección.");
            return;
        }

        asientosSeleccionados.remove(seleccionado);
        cargarAsientosZona(cmbZona.getValue());
        actualizarTotal();
    }

    @FXML
    private void continuarCompra() {
        if (usuario == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Usuario no disponible", "Debes iniciar sesión nuevamente.");
            return;
        }

        if (evento == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Evento no disponible", "No hay un evento cargado.");
            return;
        }

        if (asientosSeleccionados.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin asientos", "Debes seleccionar al menos un asiento.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/uniquindio/proyectop2/vistas/compras/confirmar_compra.fxml")
            );
            Parent root = loader.load();
            ConfirmarCompraControlador controlador = loader.getController();

            if (modoEdicion) {
                controlador.inicializarEdicion(
                        usuario,
                        evento,
                        new ArrayList<>(asientosSeleccionados),
                        new ArrayList<>(serviciosIniciales),
                        metodoPagoActual,
                        idCompraEdicion
                );
            } else {
                controlador.inicializar(usuario, evento, new ArrayList<>(asientosSeleccionados));
            }

            Stage stage = new Stage();
            stage.setTitle(modoEdicion ? "Modificar compra" : "Confirmar compra");
            stage.setScene(new Scene(root, 1100, 760));
            stage.showAndWait();

            if (controlador.isOperacionExitosa()) {
                cerrarVentana();
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la confirmación de compra.");
        }
    }

    private void cargarAsientosZona(Zona zona) {
        asientosDisponibles.clear();

        if (zona == null) {
            return;
        }

        if (asientoDAO == null) {
            asientoDAO = new AsientoDAOImpl();
        }

        List<Asiento> asientos = asientoDAO.findByZona(zona.getIdZona());

        for (Asiento asiento : asientos) {
            if (asiento.getEstado() != null && "DISPONIBLE".equals(asiento.getEstado().name())) {
                if (!estaSeleccionado(asiento)) {
                    asientosDisponibles.add(asiento);
                }
            }
        }

        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0.0;

        for (Asiento asiento : asientosSeleccionados) {
            if (asiento != null && asiento.getZona() != null) {
                total += asiento.getZona().getPrecioBase();
            }
        }

        if (lblTotalEstimado != null) {
            lblTotalEstimado.setText("Total estimado: $" + String.format("%,.2f", total));
        }
    }

    private boolean estaSeleccionado(Asiento asiento) {
        if (asiento == null || asiento.getIdAsiento() == null) {
            return false;
        }

        for (Asiento seleccionado : asientosSeleccionados) {
            if (seleccionado != null
                    && seleccionado.getIdAsiento() != null
                    && seleccionado.getIdAsiento().equals(asiento.getIdAsiento())) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) lblEvento.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}