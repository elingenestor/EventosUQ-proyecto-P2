package com.uniquindio.proyectop2.patterns.Creational.singleton;

import com.uniquindio.proyectop2.Model.Asiento;
import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.ServicioAdicional;
import com.uniquindio.proyectop2.Model.Zona;
import com.uniquindio.proyectop2.dao.interfaces.AsientoDAO;
import com.uniquindio.proyectop2.dao.interfaces.CompraDAO;
import com.uniquindio.proyectop2.dao.interfaces.EventoDAO;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class GestorReportes {
    private static GestorReportes instance;

    // Inyección de los DAOs usando la fábrica del proyecto
    private final CompraDAO compraDAO = DAOFactory.crearCompraDAO();
    private final EventoDAO eventoDAO = DAOFactory.crearEventoDAO();
    private final AsientoDAO asientoDAO = DAOFactory.crearAsientoDAO();

    private GestorReportes (){}

    public static synchronized GestorReportes getInstance() {
        if (instance == null){
            instance = new GestorReportes();
        }
        return instance;
    }

    public void generarReporteVentasCSV(LocalDate inicio, LocalDate fin, String rutaArchivo) throws Exception {
        List<Compra> compras = compraDAO.findAll();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo));
             PrintWriter pw = new PrintWriter(bw)) {

            // Encabezados del archivo CSV
            pw.println("ID Compra;Fecha;Usuario;Total Pagado;Estado");

            for (Compra compra : compras) {
                // CORRECCIÓN SINCRO: Cambiado .getFecha() por .getFechaCreacion()
                if (compra != null && compra.getFechaCreacion() != null) {
                    LocalDate fechaCompra = compra.getFechaCreacion().toLocalDate();

                    if ((fechaCompra.isAfter(inicio) || fechaCompra.isEqual(inicio)) &&
                            (fechaCompra.isBefore(fin) || fechaCompra.isEqual(fin))) {

                        pw.println(compra.getIdCompra() + ";" +
                                fechaCompra + ";" +
                                (compra.getUsuario() != null ? compra.getUsuario().getEmail() : "N/A") + ";" +
                                compra.getTotal() + ";" +
                                (compra.getEstado() != null ? compra.getEstado().name() : "PROCESADA"));
                    }
                }
            }
        }
    }

    public void generarReporteOcupacionPDF(String idEvento, String rutaArchivo) throws Exception {
        Evento evento = eventoDAO.findById(idEvento);
        if (evento == null) {
            throw new IllegalArgumentException("El evento con el ID especificado no existe.");
        }

        int totales = 0;
        int ocupados = 0;

        if (evento.getZonas() != null) {
            for (Zona zona : evento.getZonas()) {
                if (zona != null) {
                    List<Asiento> asientosZona = asientoDAO.findByZona(zona.getIdZona());
                    if (asientosZona != null) {
                        totales += asientosZona.size();
                        for (Asiento asiento : asientosZona) {
                            if (asiento != null && asiento.getEstado() != null && asiento.getEstado().name().equals("OCUPADO")) {
                                ocupados++;
                            }
                        }
                    }
                }
            }
        }

        double porcentajeOcupacion = totales > 0 ? ((double) ocupados / totales) * 100 : 0.0;

        // CORRECCIÓN: Se escribe directamente en la ruta del archivo PDF seleccionada por el usuario
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            bw.write("==================================================\n");
            bw.write("         REPORTE OPERATIVO DE OCUPACIÓN           \n");
            bw.write("==================================================\n");
            bw.write("Evento ID:       " + idEvento + "\n");
            bw.write("Nombre Evento:   " + evento.getNombre() + "\n");
            bw.write("Recinto Lugar:   " + (evento.getRecinto() != null ? evento.getRecinto().getNombre() : "N/A") + "\n");
            bw.write("Fecha / Hora:    " + evento.getFechaHora() + "\n");
            bw.write("--------------------------------------------------\n");
            bw.write("Aforos Totales Habilitados:  " + totales + " asientos.\n");
            bw.write("Boletas Vendidas (Ocupados): " + ocupados + " asientos.\n");
            bw.write("Disponibilidad de Aforo:     " + (totales - ocupados) + " asientos.\n");
            bw.write("--------------------------------------------------\n");
            bw.write("PORCENTAJE DE OCUPACIÓN TOTAL: " + String.format("%.2f", porcentajeOcupacion) + "%\n");
            bw.write("==================================================\n");
        }
    }


    public void generarReporteIngresosServiciosCSV(LocalDate inicio, LocalDate fin, String rutaArchivo) throws Exception {
        List<Compra> compras = compraDAO.findAll();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo));
             PrintWriter pw = new PrintWriter(bw)) {

            pw.println("ID Compra;Fecha Compra;Nombre Servicio;Precio Servicio");

            for (Compra compra : compras) {
                // CORRECCIÓN SINCRO: Cambiado .getFecha() por .getFechaCreacion()
                if (compra != null && compra.getFechaCreacion() != null) {
                    LocalDate fechaCompra = compra.getFechaCreacion().toLocalDate();

                    if ((fechaCompra.isAfter(inicio) || fechaCompra.isEqual(inicio)) &&
                            (fechaCompra.isBefore(fin) || fechaCompra.isEqual(fin))) {

                        if (compra.getServiciosAdicionales() != null) {
                            for (ServicioAdicional servicio : compra.getServiciosAdicionales()) {
                                pw.println(compra.getIdCompra() + ";" +
                                        fechaCompra + ";" +
                                        servicio.getNombre() + ";" +
                                        servicio.getPrecio());
                            }
                        }
                    }
                }
            }
        }
    }
}
