package com.uniquindio.proyectop2.patterns.Structural.decorator;

public class VIPDecorator extends ServicioAdicionalDecorator {
    private double costoVIP;

    public VIPDecorator(ComponenteCompra wrapped, double costoVIP) {
        super(wrapped);
        this.costoVIP = costoVIP;
    }

    @Override
    public double  getCosto() {
        return super.getCosto() + costoVIP;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + "+ VIP.";
    }
}
