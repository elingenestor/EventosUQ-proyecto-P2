package com.uniquindio.proyectop2.service.interfaces;

import java.time.LocalDate;

public interface ReporteService {
    void generarReporteVentasCSV(LocalDate fechaInicio, LocalDate fechaFin, String rutaArchivo) throws Exception;
    void generarReporteOcupacionPDF(String idEvento, String rutaArchivo) throws Exception;
    void generarReporteIngresosServiciosCSV(LocalDate fechaInicio, LocalDate fechaFin, String rutaArchivo) throws Exception;
}
