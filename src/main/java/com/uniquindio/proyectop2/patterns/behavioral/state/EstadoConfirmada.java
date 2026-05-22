package com.uniquindio.proyectop2.patterns.behavioral.state;

import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Model.Compra;

public class EstadoConfirmada implements EstadoCompraState {
    @Override
    public void pagar(Compra compra) throws Exception {
        throw new Exception("La compra ya está confirmada y pagada.");
    }

    @Override
    public void cancelar(Compra compra) throws Exception {
        // Si se permite cancelar después de confirmada, debería aplicarse una política de reembolso.
        // Por simplicidad, se puede lanzar excepción o delegar en otra estrategia.
        throw new Exception("No se puede cancelar una compra confirmada sin pasar por reembolso.");
    }

    @Override
    public void confirmar(Compra compra) throws Exception {
        throw new Exception("La compra ya está confirmada.");
    }

    @Override
    public void reembolsar(Compra compra) throws Exception {
        compra.setEstado(EstadoCompra.REEMBOLSADA);
        compra.setEstadoCompraState(new EstadoReembolsada());
    }

}
