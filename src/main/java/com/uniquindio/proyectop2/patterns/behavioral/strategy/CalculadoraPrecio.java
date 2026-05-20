package com.uniquindio.proyectop2.patterns.behavioral.strategy;

import com.uniquindio.proyectop2.Model.Entrada;

public interface CalculadoraPrecio {
    double calcularPrecio(Entrada entrada, Object contexto);
}
