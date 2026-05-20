package com.uniquindio.proyectop2.patterns.behavioral.state;

import com.uniquindio.proyectop2.Model.Compra;

public interface EstadoCompraI {
    void pagar(Compra compra) throws Exception;
    void cancelar(Compra compra) throws Exception;
    void confirmar(Compra compra) throws Exception;
    void reembolsar(Compra compra) throws Exception;
}
