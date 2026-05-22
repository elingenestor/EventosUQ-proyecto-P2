package com.uniquindio.proyectop2.patterns.behavioral.state;

import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.Enums.EstadoCompra;

public class EstadoReembolsada implements EstadoCompraState {
    @Override
    public void pagar(Compra compra) throws Exception {
        throw new Exception("No se puede pagar una compra ya reembolsada.");
    }

    @Override
    public void cancelar(Compra compra) throws Exception {
        throw new Exception("La compra ya fue reembolsada, no se puede cancelar adicionalmente.");
    }

    @Override
    public void confirmar(Compra compra) throws Exception {
        throw new Exception("No se puede confirmar una compra reembolsada.");
    }

    @Override
    public void reembolsar(Compra compra) throws Exception {
        throw new Exception("La compra ya está reembolsada. No se puede reembolsar dos veces.");
    }
}
