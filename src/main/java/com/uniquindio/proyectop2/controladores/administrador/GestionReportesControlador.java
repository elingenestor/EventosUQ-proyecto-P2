package com.uniquindio.proyectop2.controladores.administrador;

import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;
import com.uniquindio.proyectop2.service.interfaces.ReporteService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class GestionReportesControlador {

    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFin;
    @FXML
    private TextField txtIdEvento;
    @FXML
    private TextField txtRutaArchivo;
    @FXML
    private Label lblEstado;

    private final ReporteService reporteService = DAOFactory.getReporteService();

    @FXML
    private void generarVentasCSV() {
        ejecutarReporte(1);
    }

    @FXML
    private void generarOcupacionPDF() {
        ejecutarReporte(2);
    }

    @FXML
    private void generarIngresosServiciosCSV() {
        ejecutarReporte(3);
    }

    private void ejecutarReporte(int tipo) {
        try {
            String ruta = txtRutaArchivo.getText();
            if (ruta == null || ruta.trim().isEmpty()) {
                mostrarError("Debes indicar una ruta de archivo");
                return;
            }

            LocalDate inicio = dpInicio.getValue();
            LocalDate fin = dpFin.getValue();

            if (tipo == 1 || tipo == 3) {
                if (inicio == null || fin == null) {
                    mostrarError("Debes seleccionar fecha inicial y final");
                    return;
                }
            }

            if (tipo == 1) {
                reporteService.generarReporteVentasCSV(inicio, fin, ruta);
                mostrarInfo("Reporte de ventas generado");
            } else if (tipo == 2) {
                String idEvento = txtIdEvento.getText();
                if (idEvento == null || idEvento.trim().isEmpty()) {
                    mostrarError("Debes indicar el ID del evento");
                    return;
                }
                reporteService.generarReporteOcupacionPDF(idEvento, ruta);
                mostrarInfo("Reporte de ocupación generado");
            } else {
                reporteService.generarReporteIngresosServiciosCSV(inicio, fin, ruta);
                mostrarInfo("Reporte de ingresos de servicios generado");
            }
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarInfo(String mensaje) {
        if (lblEstado != null) {
            lblEstado.setText(mensaje);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        if (lblEstado != null) {
            lblEstado.setText(mensaje);
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
