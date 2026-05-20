package com.uniquindio.proyectop2.patterns.behavioral.strategy;

import com.uniquindio.proyectop2.Model.Entrada;

public class PrecioBaseStrategy implements CalculadoraPrecio{
    @Override
    public double calcularPrecio(Entrada entrada, Object contexto) {
        return entrada.getZona().getPrecioBase();
    }
}
