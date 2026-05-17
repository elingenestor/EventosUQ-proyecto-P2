package com.uniquindio.proyectop2.patterns.Structural.decorator;

import com.uniquindio.proyectop2.Model.Compra;

public class CompraBase implements ComponenteCompra{
    private Compra compra;

    public CompraBase(Compra compra) {
        this.compra = compra;
    }

    @Override
    public double getCosto() {
        return compra.getCosto();
    }

    @Override
    public String getDescripcion() {
        return compra.getDescripcion();
    }
}
