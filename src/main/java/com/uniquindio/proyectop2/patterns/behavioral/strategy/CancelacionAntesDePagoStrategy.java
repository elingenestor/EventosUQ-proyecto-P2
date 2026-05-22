package com.uniquindio.proyectop2.patterns.behavioral.strategy;

import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.Model.Entrada;
import com.uniquindio.proyectop2.Enums.EstadoAsiento;
import com.uniquindio.proyectop2.Enums.EstadoEntrada;
import com.uniquindio.proyectop2.dao.interfaces.EntradaDAO;
import com.uniquindio.proyectop2.dao.interfaces.AsientoDAO;
import com.uniquindio.proyectop2.dao.interfaces.CompraDAO;


public class CancelacionAntesDePagoStrategy implements PoliticaCancelacion{

    @Override
    public void aplicarCancelacion(Compra compra) throws Exception {
        if (compra.getEstado() != EstadoCompra.CREADA) {
            throw new Exception("Esta política solo aplica para compras no pagadas.");
        }


        for (Entrada entrada : compra.getEntradas()) {
            if (entrada.getAsiento() != null) {
                AsientoDAO.cambiarEstado(entrada.getAsiento().getIdAsiento(), EstadoAsiento.DISPONIBLE);
            }

            entrada.setEstadoEntrada(EstadoEntrada.ANULADA);

            EntradaDAO.update(entrada);
        }

        // 3. Cambiar estado de la compra
        compra.setEstado(EstadoCompra.CANCELADA);
        CompraDAO.update(compra);
    }
}
