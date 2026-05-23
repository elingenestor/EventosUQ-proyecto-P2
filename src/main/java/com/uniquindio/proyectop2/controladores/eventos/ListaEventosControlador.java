package com.uniquindio.proyectop2.controladores.eventos;

import com.uniquindio.proyectop2.Enums.CategoriaEvento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.Usuario; // Corregido con M mayúscula
import com.uniquindio.proyectop2.service.interfaces.EventoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ListaEventosControlador {

    @FXML private TextField txtCiudad;
    @FXML private ComboBox<CategoriaEvento> cmbCategoria;
    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;

    @FXML private GridPane gridEventos;

    private EventoService eventoService;
    private Usuario usuario;

    @FXML
    public void initialize() {
        // 1. Cargamos con elegancia las categorías en el ComboBox al iniciar
        if (cmbCategoria != null) {
            cmbCategoria.getItems().setAll(CategoriaEvento.values());
        }

        // 2. ¡Superpoder Reactivo! Escuchamos los cambios en tiempo real sin usar botones
        txtCiudad.textProperty().addListener((observable, oldValue, newValue) -> {
            refrescarEventos();
        });

        cmbCategoria.valueProperty().addListener((observable, oldValue, newValue) -> {
            refrescarEventos();
        });

        dpDesde.valueProperty().addListener((observable, oldValue, newValue) -> {
            refrescarEventos();
        });

        dpHasta.valueProperty().addListener((observable, oldValue, newValue) -> {
            refrescarEventos();
        });
    }

    // Método blindado con el tipo de dato exacto de tu proyecto
    public void setUsuario(Object usuarioLogueado) {
        if (usuarioLogueado instanceof com.uniquindio.proyectop2.Model.Usuario) {
            this.usuario = (com.uniquindio.proyectop2.Model.Usuario) usuarioLogueado;
            System.out.println("Usuario inyectado correctamente en el catálogo: " + this.usuario.getNombreCompleto());
        } else {
            System.out.println("Se recibió un objeto de usuario desconocido en el palacio.");
        }

        try {
            this.eventoService = new com.uniquindio.proyectop2.service.impl.EventoServiceImpl(
                    new com.uniquindio.proyectop2.dao.impl.EventoDAOImpl(),
                    new com.uniquindio.proyectop2.dao.impl.AsientoDAOImpl()
            );

            // Pintamos inmediatamente las tarjetas en pantalla al cargar
            refrescarEventos();

        } catch (Exception e) {
            System.out.println("Error al levantar el servicio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarEventos() {
        refrescarEventos();
    }

    @FXML
    private void limpiarFiltros() {
        txtCiudad.clear();
        if (cmbCategoria != null) cmbCategoria.setValue(null);
        dpDesde.setValue(null);
        dpHasta.setValue(null);
        refrescarEventos();
    }

    @FXML
    private void refrescarEventos() {
        try {
            if (gridEventos == null) return;
            gridEventos.getChildren().clear();

            if (eventoService == null) return;

            String ciudad = (txtCiudad.getText() != null && !txtCiudad.getText().isBlank()) ? txtCiudad.getText() : null;
            CategoriaEvento category = cmbCategoria != null ? cmbCategoria.getValue() : null;
            LocalDate desde = dpDesde != null ? dpDesde.getValue() : null;
            LocalDate hasta = dpHasta != null ? dpHasta.getValue() : null;

            // Traemos los eventos reales filtrados de la base de datos
            List<Evento> disponibles = eventoService.listarEventosDisponibles(ciudad, category, desde, hasta);

            if (disponibles == null || disponibles.isEmpty()) {
                System.out.println("No hay eventos disponibles que coincidan con los criterios.");
                return;
            }

            int columna = 0;
            int fila = 0;

            for (Evento ev : disponibles) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/proyectop2/vistas/fragmentos/tarjeta_evento.fxml"));
                VBox tarjeta = loader.load();

                // 2. Pasamos el evento y el usuario logueado a su respectivo controlador de tarjeta
                TarjetaEventoControlador tarjetaCtrl = loader.getController();
                if (tarjetaCtrl != null) {
                    tarjetaCtrl.configurarTarjeta(ev);
                    tarjetaCtrl.setUsuario(usuario);
                }

                // 3. Organizamos en la cuadrícula de 3 en 3
                if (columna == 3) {
                    columna = 0;
                    fila++;
                }

                gridEventos.add(tarjeta, columna, fila);
                GridPane.setMargin(tarjeta, new Insets(12));
                columna++;
            }
        } catch (IOException fxmlEx) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error de Ruta FXML");
            alert.setHeaderText("No se pudo cargar tarjeta_evento.fxml");
            alert.setContentText("Revisa si el archivo está en vistas/fragmentos/\nDetalle: " + fxmlEx.getMessage());
            alert.showAndWait();
            fxmlEx.printStackTrace();
        } catch (Exception e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error inesperado en el catálogo");
            alert.setHeaderText("Ocurrió un problema al dibujar las tarjetas");
            alert.setContentText("Mensaje del error: " + e.toString());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
