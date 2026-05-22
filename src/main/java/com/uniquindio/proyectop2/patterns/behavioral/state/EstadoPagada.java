package com.uniquindio.proyectop2.patterns.behavioral.state;

import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Model.Compra;

public class EstadoPagada implements EstadoCompraState{
    @Override
    public void pagar(Compra compra) throws Exception {
        throw new Exception("La compra ya está pagada.");
    }

    @Override
    public void cancelar(Compra compra) throws Exception {
        compra.setEstado(EstadoCompra.CANCELADA);
        compra.setEstadoCompraState(new EstadoCancelada());
    }

    @Override
    public void confirmar(Compra compra) throws Exception {
        compra.setEstado(EstadoCompra.CONFIRMADA);
        compra.setEstadoCompraState((EstadoCompraState) new EstadoConfirmada());
    }

    @Override
    public void reembolsar(Compra compra) throws Exception {
        compra.setEstado(EstadoCompra.REEMBOLSADA);
        compra.setEstadoCompraState(new EstadoReembolsada());
    }
}
