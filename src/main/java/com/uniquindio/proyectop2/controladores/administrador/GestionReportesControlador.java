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
            // === PASO A: VALIDACIÓN PREVIA DE DATOS OBLIGATORIOS ===
            LocalDate inicio = dpInicio.getValue();
            LocalDate fin = dpFin.getValue();

            // Validar fechas para los reportes de ventas y servicios adicionales
            if (tipo == 1 || tipo == 3) {
                if (inicio == null || fin == null) {
                    mostrarError("Debes seleccionar obligatoriamente la fecha inicial y final antes de exportar.");
                    return;
                }
                if (inicio.isAfter(fin)) {
                    mostrarError("La fecha de inicio no puede ser posterior a la fecha fin.");
                    return;
                }
            }

            // Validar ID del evento para el reporte de ocupación
            String idEvento = "";
            if (tipo == 2) {
                idEvento = txtIdEvento.getText();
                if (idEvento == null || idEvento.trim().isEmpty()) {
                    mostrarError("Debes indicar el ID del evento antes de generar el reporte de ocupación.");
                    return;
                }
            }

            // === PASO B: CONFIGURACIÓN DEL SELECTOR DE ARCHIVOS ===
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Seleccionar ubicación para guardar el reporte");

            if (tipo == 1) {
                fileChooser.setInitialFileName("reporte_ventas.csv");
                fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Archivos CSV (*.csv)", "*.csv"));
            } else if (tipo == 2) {
                fileChooser.setInitialFileName("reporte_ocupacion.pdf");
                fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Documento PDF (*.pdf)", "*.pdf"));
            } else {
                fileChooser.setInitialFileName("reporte_ingresos_servicios.csv");
                fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Archivos CSV (*.csv)", "*.csv"));
            }

            // === PASO C: ABRIR EXPLORADOR DE WINDOWS ===
            javafx.stage.Stage stage = (javafx.stage.Stage) dpInicio.getScene().getWindow();
            java.io.File archivoSeleccionado = fileChooser.showSaveDialog(stage);

            if (archivoSeleccionado == null) {
                return; // El usuario canceló la ventana del explorador
            }

            String ruta = archivoSeleccionado.getAbsolutePath();

            // === PASO D: EJECUCIÓN DEL REPORTE ===
            if (tipo == 1) {
                reporteService.generarReporteVentasCSV(inicio, fin, ruta);
                mostrarInfo("Reporte de ventas generado exitosamente.");
            } else if (tipo == 2) {
                reporteService.generarReporteOcupacionPDF(idEvento, ruta);
                mostrarInfo("Reporte de ocupación generado exitosamente.");
            } else {
                reporteService.generarReporteIngresosServiciosCSV(inicio, fin, ruta);
                mostrarInfo("Reporte de ingresos de servicios generado exitosamente.");
            }
        } catch (Exception e) {
            mostrarError("Ocurrió un error al procesar el reporte: " + e.getMessage());
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
