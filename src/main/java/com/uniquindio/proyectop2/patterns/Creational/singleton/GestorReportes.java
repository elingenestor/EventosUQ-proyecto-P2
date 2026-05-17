package com.uniquindio.proyectop2.patterns.Creational.singleton;

import java.time.LocalDate;

public class GestorReportes {
    private static GestorReportes instance;

    private GestorReportes (){}

    private static synchronized GestorReportes getInstance() {
        if (instance == null){
            instance = new GestorReportes();
        }
        return instance;
    }

    public void generarReporteVentasCSV(LocalDate inicio, LocalDate fin, String rutaArchivo){
        //IMplementacion con apache POI / OpenCSV
        System.out.println("Generando reporte de ventas CSV ...");
    }

    public void GenerarReporteOcupacionPDF(int idEvento, String rutaArchivo){
        //implementacion con PDFBox
        System.out.println("Generando reporte ocupacion PDF...");
    }

    public void GenerarReporteIngresosServiciosCSV(LocalDate inicio, LocalDate fin, String rutaArchivo){
        System.out.println("Generando reporte de ingresos de servicios CSV...");
    }
}
