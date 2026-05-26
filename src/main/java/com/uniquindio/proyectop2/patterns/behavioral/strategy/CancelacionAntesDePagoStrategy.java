package com.uniquindio.proyectop2.patterns.behavioral.strategy;

import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.Model.Entrada;
import com.uniquindio.proyectop2.Enums.EstadoAsiento;
import com.uniquindio.proyectop2.Enums.EstadoEntrada;
import com.uniquindio.proyectop2.dao.interfaces.EntradaDAO;
import com.uniquindio.proyectop2.dao.interfaces.AsientoDAO;
import com.uniquindio.proyectop2.dao.interfaces.CompraDAO;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;

public class CancelacionAntesDePagoStrategy implements PoliticaCancelacion {

    private final CompraDAO compraDAO;
    private final EntradaDAO entradaDAO;
    private final AsientoDAO asientoDAO;

    public CancelacionAntesDePagoStrategy() {
        this.compraDAO = DAOFactory.crearCompraDAO();
        this.entradaDAO = DAOFactory.crearEntradaDAO();
        this.asientoDAO = DAOFactory.crearAsientoDAO();
    }

    @Override
    public void aplicarCancelacion(Compra compra) throws Exception {
        if (compra.getEstado() != EstadoCompra.CREADA) {
            throw new Exception("Esta política solo aplica para compras no pagadas.");
        }

        for (Entrada entrada : compra.getEntradas()) {
            if (entrada.getAsiento() != null) {
                this.asientoDAO.cambiarEstado(entrada.getAsiento().getIdAsiento(), EstadoAsiento.DISPONIBLE);
            }

            entrada.setEstadoEntrada(EstadoEntrada.ANULADA);

            this.entradaDAO.update(entrada);
        }

        compra.setEstado(EstadoCompra.CANCELADA);

        this.compraDAO.update(compra);
    }
}
