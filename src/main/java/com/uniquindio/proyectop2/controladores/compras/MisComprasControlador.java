package com.uniquindio.proyectop2.controladores.compras;

import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.Model.Entrada;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.controladores.util.SesionUsuario;
import com.uniquindio.proyectop2.dao.interfaces.CompraDAO;
import com.uniquindio.proyectop2.dao.interfaces.EntradaDAO;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.impl.CompraServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.CompraService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MisComprasControlador {

    @FXML private Label lblUsuario;
    @FXML private TableView<Compra> tblCompras;
    @FXML private TableColumn<Compra, String> colIdCompra;
    @FXML private TableColumn<Compra, String> colEvento;
    @FXML private TableColumn<Compra, String> colFecha;
    @FXML private TableColumn<Compra, Number> colTotal;
    @FXML private TableColumn<Compra, String> colEstado;
    @FXML private TableColumn<Compra, String> colMetodoPago;
    @FXML private Label lblIdCompra;
    @FXML private Label lblEvento;
    @FXML private Label lblFecha;
    @FXML private Label lblTotal;
    @FXML private Label lblEstado;
    @FXML private Label lblMetodoPago;
    @FXML private Label lblCantidadEntradas;
    @FXML private ListView<Entrada> lstEntradas;

    private final ObservableList<Compra> compras = FXCollections.observableArrayList();
    private final ObservableList<Entrada> entradasDetalle = FXCollections.observableArrayList();
    private final CompraDAO compraDAO = DAOFactory.obtenerCompraDAO();
    private final EntradaDAO entradaDAO = DAOFactory.obtenerEntradaDAO();
    private final CompraService compraService = new CompraServiceImpl();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private Usuario usuario; // Almacena el usuario inyectado manualmente

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

        // Si el usuario llegó bien, pintamos su nombre y listamos sus compras de la base de datos
        if (usuario != null) {
            if (lblUsuario != null) {
                lblUsuario.setText(usuario.getNombreCompleto());
            }
            cargarCompras(); // Ahora sí llamamos a cargar de forma segura
        }
    }

    @FXML
    private void initialize() {
        configurarTabla();
        configurarListaEntradas();
        // cargarCompras();
    }

    private void configurarTabla() {
        colIdCompra.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        colEvento.setCellValueFactory(data -> {
            String nombre = data.getValue().getEvento() != null ? data.getValue().getEvento().getNombre() : "-";
            return new javafx.beans.property.SimpleStringProperty(nombre);
        });
        colFecha.setCellValueFactory(data -> {
            if (data.getValue().getFechaCreacion() == null) {
                return new javafx.beans.property.SimpleStringProperty("-");
            }
            return new javafx.beans.property.SimpleStringProperty(data.getValue().getFechaCreacion().format(formatter));
        });
        colTotal.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotal()));
        colEstado.setCellValueFactory(data -> {
            String estado = data.getValue().getEstado() != null ? data.getValue().getEstado().name() : "-";
            return new javafx.beans.property.SimpleStringProperty(estado);
        });
        colMetodoPago.setCellValueFactory(data -> {
            String metodo = data.getValue().getMetodoPagoUsado() != null && data.getValue().getMetodoPagoUsado().getTipo() != null
                    ? data.getValue().getMetodoPagoUsado().getTipo().name()
                    : "-";
            return new javafx.beans.property.SimpleStringProperty(metodo);
        });

        tblCompras.setItems(compras);
        tblCompras.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> mostrarDetalleCompra(newValue));
    }

    private void configurarListaEntradas() {
        lstEntradas.setCellFactory(listView -> new ListCell<Entrada>() {
            @Override
            protected void updateItem(Entrada item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                String fila = item.getAsiento() != null ? item.getAsiento().getFila() : "-";
                String numero = item.getAsiento() != null ? String.valueOf(item.getAsiento().getNumero()) : "-";
                String zona = item.getZona() != null ? item.getZona().getNombre() : "-";
                setText("Fila " + fila + " | Asiento " + numero + " | Zona " + zona);
            }
        });
        lstEntradas.setItems(entradasDetalle);
    }

    private void cargarCompras() {

        if (this.usuario == null) {
            lblUsuario.setText("No hay usuario autenticado");
            compras.clear();
            limpiarDetalle();
            mostrarAlerta("Sesión no encontrada", "No hay un usuario autenticado para mostrar sus compras.");
            return;
        }

        lblUsuario.setText(usuario.getNombreCompleto());
        List<Compra> lista = compraDAO.findByUsuario(usuario.getIdUsuario());
        compras.setAll(lista);

        if (!compras.isEmpty()) {
            tblCompras.getSelectionModel().selectFirst();
        } else {
            limpiarDetalle();
        }
    }

    private void mostrarDetalleCompra(Compra compra) {
        if (compra == null) {
            limpiarDetalle();
            return;
        }

        lblIdCompra.setText(valorOGuion(compra.getIdCompra()));
        lblEvento.setText(compra.getEvento() != null ? valorOGuion(compra.getEvento().getNombre()) : "-");
        lblFecha.setText(compra.getFechaCreacion() != null ? compra.getFechaCreacion().format(formatter) : "-");
        lblTotal.setText(String.format("$%.2f", compra.getTotal()));
        lblEstado.setText(compra.getEstado() != null ? compra.getEstado().name() : "-");
        if (compra.getMetodoPagoUsado() != null && compra.getMetodoPagoUsado().getTipo() != null) {
            lblMetodoPago.setText(compra.getMetodoPagoUsado().getTipo().name() + " - " + valorOGuion(compra.getMetodoPagoUsado().getNumero()));
        } else {
            lblMetodoPago.setText("-");
        }

        entradasDetalle.setAll(entradaDAO.findByCompra(compra.getIdCompra()));
        lblCantidadEntradas.setText(String.valueOf(entradasDetalle.size()));
    }

    @FXML
    private void actualizarListado() {
        cargarCompras();
    }

    @FXML
    private void cancelarCompra() {
        Compra seleccionada = tblCompras.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una compra", "Debes elegir una compra de la tabla.");
            return;
        }

        if (seleccionada.getEstado() == null) {
            mostrarAlerta("Estado inválido", "La compra seleccionada no tiene un estado válido.");
            return;
        }

        if (seleccionada.getEstado().name().equals("CANCELADA") || seleccionada.getEstado().name().equals("REEMBOLSADA")) {
            mostrarAlerta("Compra no cancelable", "La compra ya está cancelada o reembolsada.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cancelar compra");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Deseas cancelar la compra " + seleccionada.getIdCompra() + "?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                try {
                    compraService.cancelarCompra(seleccionada.getIdCompra());
                    cargarCompras();
                    mostrarInformacion("Compra cancelada", "La compra fue cancelada correctamente.");
                } catch (Exception e) {
                    mostrarAlerta("Error al cancelar", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void volver() {
        try {
            BorderPane principal = (BorderPane) tblCompras.getScene().getRoot();
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/uniquindio/proyectop2/vistas/principal/inicio.fxml")
            );
            javafx.scene.Node root = loader.load();
            principal.setCenter(root);

            com.uniquindio.proyectop2.controladores.principal.InicioControlador controlador = loader.getController();
            if (controlador != null) {
                controlador.setUsuario(SesionUsuario.getUsuarioActual());
            }
        } catch (Exception e) {
            mostrarAlerta("Error", mensajeError(e));
        }
    }

    private void limpiarDetalle() {
        lblIdCompra.setText("-");
        lblEvento.setText("-");
        lblFecha.setText("-");
        lblTotal.setText("$0.00");
        lblEstado.setText("-");
        lblMetodoPago.setText("-");
        lblCantidadEntradas.setText("0");
        entradasDetalle.clear();
    }

    private String valorOGuion(String valor) {
        return (valor == null || valor.isBlank()) ? "-" : valor;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private String mensajeError(Exception e) {
        if (e == null) {
            return "Error desconocido.";
        }
        String mensaje = e.getMessage();
        if (mensaje == null || mensaje.isBlank()) {
            mensaje = e.toString();
        }
        return mensaje;
    }
}
