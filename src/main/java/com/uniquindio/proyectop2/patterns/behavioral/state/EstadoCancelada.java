package com.uniquindio.proyectop2.patterns.behavioral.state;
import com .uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Model.Compra;

public class EstadoCancelada implements EstadoCompraState {
    @Override
    public void pagar(Compra compra) throws Exception {
        throw new Exception("No se puede pagar una compra cancelada.");
    }

    @Override
    public void cancelar(Compra compra) throws Exception {
        throw new Exception("La compra ya está cancelada.");
    }

    @Override
    public void confirmar(Compra compra) throws Exception {
        throw new Exception("No se puede confirmar una compra cancelada.");
    }

    @Override
    public void reembolsar(Compra compra) throws Exception {
        throw new Exception("No se puede reembolsar una compra cancelada (ya fue cancelada sin pago).");
    }
}
