package com.uniquindio.proyectop2.controladores.compras;

import com.uniquindio.proyectop2.Model.*;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.patterns.Structural.facade.ProcesadorCompraFacade;
import com.uniquindio.proyectop2.service.impl.UsuarioServiceImpl;
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
    private final UsuarioService usuarioService = new UsuarioServiceImpl(
            DAOFactory.crearUsuarioDAO(),
            DAOFactory.crearMetodoPagoDAO()
    );

    private Usuario usuario;
    private Evento evento;

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

        actualizarTotal();
    }

    @FXML
    private void confirmarCompra() {
        MetodoPago metodo = cmbMetodosPago.getValue();
        if (metodo == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecciona un método de pago", "Debes elegir un método de pago.");
            return;
        }

        List<ServicioAdicional> serviciosSeleccionados = new ArrayList<>(lstServicios.getSelectionModel().getSelectedItems());

        try {
            Compra compra = procesadorCompraFacade.procesarCompra(usuario, evento, new ArrayList<>(asientosSeleccionados), serviciosSeleccionados, metodo);
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
