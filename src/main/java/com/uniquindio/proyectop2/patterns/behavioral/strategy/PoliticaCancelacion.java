package com.uniquindio.proyectop2.patterns.behavioral.strategy;

import com.uniquindio.proyectop2.Model.Compra;

public interface PoliticaCancelacion {
    void aplicarCancelacion(Compra compra) throws Exception;

}
