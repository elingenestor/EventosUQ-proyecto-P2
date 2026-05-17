package com.uniquindio.proyectop2.patterns.Structural.decorator;

public abstract class ServicioAdicionalDecorator implements ComponenteCompra{
    protected ComponenteCompra wrapped;

    public ServicioAdicionalDecorator(ComponenteCompra wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public double getCosto() {
        return wrapped.getCosto();
    }

    @Override
    public String getDescripcion() {
        return wrapped.getDescripcion();
    }
}
