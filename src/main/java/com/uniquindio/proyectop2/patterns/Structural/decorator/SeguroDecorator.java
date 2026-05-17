package com.uniquindio.proyectop2.patterns.Structural.decorator;

public class SeguroDecorator extends ServicioAdicionalDecorator {
    private double costoSerguro;

    public SeguroDecorator(ComponenteCompra wrapped, double costoSerguro) {
        super(wrapped);
        this.costoSerguro = costoSerguro;
    }

    @Override
    public double getCosto() {
        return super.getCosto() + costoSerguro;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + "+ Seguro.";
    }
}
