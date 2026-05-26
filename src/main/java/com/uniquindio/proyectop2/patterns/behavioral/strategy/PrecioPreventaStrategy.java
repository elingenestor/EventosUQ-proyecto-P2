package com.uniquindio.proyectop2.patterns.behavioral.strategy;

import com.uniquindio.proyectop2.Model.Entrada;

import java.time.LocalDateTime;

public class PrecioPreventaStrategy implements CalculadoraPrecio{
    private LocalDateTime fechaLimite;
    private double descuento;

    public PrecioPreventaStrategy(LocalDateTime fechaLimite, double descuento) {
        this.fechaLimite = fechaLimite;
        this.descuento = descuento;
    }

    @Override
    public double calcularPrecio(Entrada entrada, Object contexto) {
        double precioBase = entrada.getZona().getPrecioBase();
        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isBefore(fechaLimite)) {
            return precioBase * (1 - descuento);
        }
        return precioBase;
    }
}
