package com.uniquindio.proyectop2.Model;

import com.uniquindio.proyectop2.Enums.EstadoEntrada;
public class Entrada {
    private String idEntrada;
    private double precioFinal;
    private EstadoEntrada estadoEntrada;
    private Zona zona;
    private Asiento asiento;
    private Compra compra;


    public Entrada(){
    }

    public Entrada(String idEntrada, double precioFInal, EstadoEntrada estadoEntrada, Zona zona, Asiento asiento, Compra compra){
        this.idEntrada = idEntrada;
        this.precioFinal = precioFInal;
        this.estadoEntrada = estadoEntrada;
        this.zona = zona;
        this.asiento = asiento;
        this.compra = compra;
    }

    public String getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(String idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public EstadoEntrada getEstadoEntrada() {
        return estadoEntrada;
    }

    public void setEstadoEntrada(EstadoEntrada estadoEntrada) {
        this.estadoEntrada = estadoEntrada;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }
}
