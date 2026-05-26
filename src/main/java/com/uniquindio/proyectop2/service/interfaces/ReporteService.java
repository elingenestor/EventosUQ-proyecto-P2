package com.uniquindio.proyectop2.service.interfaces;

import java.time.LocalDate;

public interface ReporteService {
    void generarReporteVentasCSV(LocalDate inicio, LocalDate fin, String rutaArchivo) throws Exception;
    void generarReporteOcupacionPDF(String idEvento, String rutaArchivo) throws Exception;
    void generarReporteIngresosServiciosCSV(LocalDate inicio, LocalDate fin, String rutaArchivo) throws Exception;
}
