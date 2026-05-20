package com.uniquindio.proyectop2.patterns.behavioral.state;

import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.patterns.behavioral.state.EstadoCompraI;

public class EstadoCreada implements EstadoCompraI{
    @Override
    public void pagar(Compra compra) throws Exception {
        compra.setEstado(EstadoCompra.PAGADA);
        compra.setEstadoCompraState(new EstadoPagada());
    }

    @Override
    public void cancelar(Compra compra) throws Exception {
        compra.setEstado(EstadoCompra.CANCELADA);
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
