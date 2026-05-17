package com.uniquindio.proyectop2.Model;

import com.uniquindio.proyectop2.Enums.TipoServicio;

public class ServicioAdicional {
    private String idServicio;
    private String nombre;
    private String descripcion;
    private double precio;
    private TipoServicio tipoServicio;

    public ServicioAdicional(){
    }


    public ServicioAdicional(String idServicio, String nombre, String descripcion, double precio, TipoServicio tipoServicio){
        this.idServicio = idServicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipoServicio = tipoServicio;
    }

    //getter y setter

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
