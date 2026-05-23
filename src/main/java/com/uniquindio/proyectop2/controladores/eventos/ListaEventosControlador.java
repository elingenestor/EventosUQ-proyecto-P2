package com.uniquindio.proyectop2.controladores.eventos;

import com.uniquindio.proyectop2.Enums.CategoriaEvento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.impl.EventoServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.EventoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ListaEventosControlador {

    @FXML private TextField txtCiudad;
    @FXML private ComboBox<CategoriaEvento> cmbCategoria;
    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;

    @FXML private GridPane gridEventos; // Reemplaza formalmente tu TableView

    private final EventoService eventoService = new EventoServiceImpl(
            DAOFactory.crearEventoDAO(),
            DAOFactory.crearAsientoDAO()
    );

    private Usuario usuario;

    @FXML
    public void initialize() {
        cmbCategoria.setItems(FXCollections.observableArrayList(CategoriaEvento.values()));
        cmbCategoria.setPromptText("Todas");

        // Escuchadores reactivos para filtrar al escribir/seleccionar
        txtCiudad.textProperty().addListener((observable, oldValue, newValue) -> refrescarEventos());
        cmbCategoria.valueProperty().addListener((observable, oldValue, newValue) -> refrescarEventos());
        dpDesde.valueProperty().addListener((observable, oldValue, newValue) -> refrescarEventos());
        dpHasta.valueProperty().addListener((observable, oldValue, newValue) -> refrescarEventos());

        refrescarEventos();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        refrescarEventos();
    }

    @FXML
    private void buscarEventos() {
        refrescarEventos();
    }

    @FXML
    private void limpiarFiltros() {
        txtCiudad.clear();
        cmbCategoria.setValue(null);
        dpDesde.setValue(null);
        dpHasta.setValue(null);
        refrescarEventos();
    }

    @FXML
    private void refrescarEventos() {
        try {
            if (gridEventos == null) return;
            gridEventos.getChildren().clear();

            String ciudad = (txtCiudad.getText() != null && !txtCiudad.getText().isBlank()) ? txtCiudad.getText().trim() : null;
            CategoriaEvento categoria = cmbCategoria.getValue();
            LocalDate desde = dpDesde.getValue();
            LocalDate hasta = dpHasta.getValue();

            List<Evento> disponibles = eventoService.listarEventosDisponibles(ciudad, categoria, desde, hasta);

            if (disponibles == null || disponibles.isEmpty()) {
                return;
            }

            int columna = 0;
            int fila = 0;

            for (Evento ev : disponibles) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/proyectop2/vistas/fragmentos/tarjeta_evento.fxml"));
                VBox tarjeta = loader.load();

                TarjetaEventoControlador tarjetaCtrl = loader.getController();
                if (tarjetaCtrl != null) {
                    tarjetaCtrl.configurarTarjeta(ev);
                    tarjetaCtrl.setUsuario(usuario);
                    // Dejamos que la tarjeta use su propio botón interno de comprar instalado por ella
                }

                if (columna == 3) {
                    columna = 0;
                    fila++;
                }

                gridEventos.add(tarjeta, columna, fila);
                GridPane.setMargin(tarjeta, new Insets(12));
                columna++;
            }
        } catch (IOException fxmlEx) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error FXML", "No se pudo cargar tarjeta_evento.fxml: " + fxmlEx.getMessage());
            fxmlEx.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Problema al renderizar las tarjetas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
