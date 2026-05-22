package com.uniquindio.proyectop2.patterns.behavioral.state;

import com.uniquindio.proyectop2.Model.Compra;

public class EstadoCreada implements EstadoCompraState {
    @Override
    public void pagar(Compra compra) throws Exception {
        compra.setEstado(com.uniquindio.proyectop2.Enums.EstadoCompra.PAGADA);
        compra.setEstadoCompraState(new EstadoPagada());
    }

    @Override
    public void cancelar(Compra compra) throws Exception {
        compra.setEstado(com.uniquindio.proyectop2.Enums.EstadoCompra.CANCELADA);
        compra.setEstadoCompraState(new EstadoCancelada());
    }

    @Override
    public void confirmar(Compra compra) throws Exception {
        throw new Exception("No se puede confirmar una compra no pagada.");
    }

    @Override
    public void reembolsar(Compra compra) throws Exception {
        throw new Exception("No se puede reembolsar una compra no pagada.");
    }
}
