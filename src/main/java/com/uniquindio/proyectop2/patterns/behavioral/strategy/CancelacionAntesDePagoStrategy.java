package com.uniquindio.proyectop2.patterns.behavioral.strategy;

import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Model.Compra;

public class CancelacionAntesDePagoStrategy implements PoliticaCancelacion{
    @Override
    public void aplicarCancelacion(Compra compra) throws Exception {
        if (compra.getEstado() != EstadoCompra.CREADA) {
            throw new Exception("Esta política solo aplica para compras no pagadas.");
        }
        compra.setEstado(EstadoCompra.CANCELADA);
        // Liberar asientos, etc. (lógica delegada)
    }
}
