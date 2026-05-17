package com.uniquindio.proyectop2.service.impl;

import com.uniquindio.proyectop2.patterns.Creational.singleton.GestorReportes;
import com.uniquindio.proyectop2.service.interfaces.ReporteService;

import java.time.LocalDate;

public class ReporteServiceImpl implements ReporteService {
    private final GestorReportes gestorReportes = GestorReportes.getInstance();

    @Override
    public void generarReporteVentasCSV(LocalDate inicio, LocalDate fin, String rutaArchivo) throws Exception {
        gestorReportes.generarReporteVentasCSV(inicio, fin, rutaArchivo);
    }

    @Override
    public void generarReporteOcupacionPDF(String idEvento, String rutaArchivo) throws Exception {
        gestorReportes.generarReporteOcupacionPDF(idEvento, rutaArchivo);
    }

    @Override
    public void generarReporteIngresosServiciosCSV(LocalDate inicio, LocalDate fin, String rutaArchivo) throws Exception {
        gestorReportes.generarReporteIngresosServiciosCSV(inicio, fin, rutaArchivo);
    }
}
