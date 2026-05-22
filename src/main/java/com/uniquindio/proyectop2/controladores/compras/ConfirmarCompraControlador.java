package com.uniquindio.proyectop2.controladores.compras;

import com.uniquindio.proyectop2.Model.*;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.patterns.Structural.facade.ProcesadorCompraFacade;
import com.uniquindio.proyectop2.service.impl.CompraServiceImpl;
import com.uniquindio.proyectop2.service.impl.UsuarioServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.CompraService;
import com.uniquindio.proyectop2.service.interfaces.UsuarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConfirmarCompraControlador {

    @FXML private Label lblUsuario;
    @FXML private Label lblEvento;
    @FXML private Label lblFecha;
    @FXML private Label lblTotalBase;
    @FXML private TableView<Asiento> tblAsientos;
    @FXML private TableColumn<Asiento, String> colFila;
    @FXML private TableColumn<Asiento, Integer> colNumero;
    @FXML private TableColumn<Asiento, String> colZona;
    @FXML private ListView<ServicioAdicional> lstServicios;
    @FXML private ComboBox<MetodoPago> cmbMetodosPago;
    @FXML private Label lblTotalFinal;

    private final ObservableList<Asiento> asientosSeleccionados = FXCollections.observableArrayList();
    private final ObservableList<ServicioAdicional> serviciosDisponibles = FXCollections.observableArrayList();
    private final ObservableList<MetodoPago> metodosPago = FXCollections.observableArrayList();
    private final ProcesadorCompraFacade procesadorCompraFacade = new ProcesadorCompraFacade();
    private final CompraService compraService = new CompraServiceImpl();
    private final UsuarioService usuarioService = new UsuarioServiceImpl(
            DAOFactory.crearUsuarioDAO(),
            DAOFactory.crearMetodoPagoDAO()
    );

    private Usuario usuario;
    private Evento evento;
    private boolean modoEdicion = false;
    private boolean operacionExitosa = false;
    private String idCompraEdicion;

    @FXML
    private void initialize() {
        colFila.setCellValueFactory(new PropertyValueFactory<>("fila"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colZona.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getZona() != null ? cell.getValue().getZona().getNombre() : ""
        ));
        tblAsientos.setItems(asientosSeleccionados);

        lstServicios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstServicios.setCellFactory(listView -> new ListCell<ServicioAdicional>() {
            @Override
            protected void updateItem(ServicioAdicional item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre() + " ($" + item.getPrecio() + ")");
            }
        });

        cmbMetodosPago.setCellFactory(listView -> new ListCell<MetodoPago>() {
            @Override
            protected void updateItem(MetodoPago item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTipo() + " - " + item.getNumero());
            }
        });
        cmbMetodosPago.setButtonCell(new ListCell<MetodoPago>() {
            @Override
            protected void updateItem(MetodoPago item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTipo() + " - " + item.getNumero());
            }
        });

        lstServicios.getSelectionModel().getSelectedItems().addListener((javafx.collections.ListChangeListener<ServicioAdicional>) c -> actualizarTotal());
    }

    public void inicializar(Usuario usuario, Evento evento, List<Asiento> asientos) {
        this.usuario = usuario;
        this.evento = evento;
        this.modoEdicion = false;
        this.idCompraEdicion = null;
        this.operacionExitosa = false;
        asientosSeleccionados.setAll(asientos != null ? asientos : new ArrayList<>());

        lblUsuario.setText(usuario != null ? usuario.getNombreCompleto() : "-");
        lblEvento.setText(evento != null ? evento.getNombre() : "-");
        lblFecha.setText(evento != null && evento.getFechaHora() != null
                ? evento.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "-");

        serviciosDisponibles.setAll(DAOFactory.crearServicioAdicionalDAO().findAll());
        lstServicios.setItems(serviciosDisponibles);

        try {
            metodosPago.setAll(usuario != null ? usuarioService.listarMetodosPago(usuario) : new ArrayList<>());
            cmbMetodosPago.setItems(metodosPago);
            if (!metodosPago.isEmpty()) {
                cmbMetodosPago.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }

        cmbMetodosPago.setDisable(false);
        actualizarTotal();
    }

    public void inicializarEdicion(Usuario usuario, Evento evento, List<Asiento> asientos, List<ServicioAdicional> serviciosActuales, MetodoPago metodoPagoActual, String idCompra) {
        inicializar(usuario, evento, asientos);
        this.modoEdicion = true;
        this.idCompraEdicion = idCompra;

        if (serviciosActuales != null && !serviciosActuales.isEmpty()) {
            for (ServicioAdicional servicioActual : serviciosActuales) {
                for (int i = 0; i < serviciosDisponibles.size(); i++) {
                    ServicioAdicional disponible = serviciosDisponibles.get(i);
                    if (disponible != null && servicioActual != null && disponible.getIdServicio() != null && disponible.getIdServicio().equals(servicioActual.getIdServicio())) {
                        lstServicios.getSelectionModel().select(i);
                    }
                }
            }
        }

        if (metodoPagoActual != null) {
            for (int i = 0; i < metodosPago.size(); i++) {
                MetodoPago metodo = metodosPago.get(i);
                if (metodo != null && metodo.getIdMetodoPago() != null && metodo.getIdMetodoPago().equals(metodoPagoActual.getIdMetodoPago())) {
                    cmbMetodosPago.getSelectionModel().select(i);
                    break;
                }
            }
        }

        cmbMetodosPago.setDisable(true);
    }

    public boolean isOperacionExitosa() {
        return operacionExitosa;
    }

    @FXML
    private void confirmarCompra() {
        List<ServicioAdicional> serviciosSeleccionados = new ArrayList<>(lstServicios.getSelectionModel().getSelectedItems());

        try {
            if (modoEdicion) {
                if (idCompraEdicion == null) {
                    throw new Exception("No se encontró la compra que vas a modificar.");
                }
                compraService.modificarCompra(idCompraEdicion, new ArrayList<>(asientosSeleccionados), serviciosSeleccionados);
                operacionExitosa = true;
                mostrarAlerta(Alert.AlertType.INFORMATION, "Compra modificada", "La compra fue actualizada correctamente.");
                cerrarVentana();
                return;
            }

            MetodoPago metodo = cmbMetodosPago.getValue();
            if (metodo == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Selecciona un método de pago", "Debes elegir un método de pago.");
                return;
            }

            Compra compra = procesadorCompraFacade.procesarCompra(usuario, evento, new ArrayList<>(asientosSeleccionados), serviciosSeleccionados, metodo);
            operacionExitosa = true;
            mostrarAlerta(Alert.AlertType.INFORMATION, "Compra exitosa", "La compra " + compra.getIdCompra() + " fue procesada correctamente.");
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al confirmar", e.getMessage());
        }
    }

    @FXML
    private void recalcularTotal() {
        actualizarTotal();
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) lblUsuario.getScene().getWindow();
        stage.close();
    }

    private void actualizarTotal() {
        double total = 0.0;
        for (Asiento asiento : asientosSeleccionados) {
            if (asiento.getZona() != null) {
                total += asiento.getZona().getPrecioBase();
            }
        }
        for (ServicioAdicional servicio : lstServicios.getSelectionModel().getSelectedItems()) {
            total += servicio.getPrecio();
        }
        lblTotalBase.setText("Subtotal: $" + String.format("%,.2f", total));
        lblTotalFinal.setText("Total estimado: $" + String.format("%,.2f", total));
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
