package com.uniquindio.proyectop2.Model;

import java.util.List;
import java.util.ArrayList;

public class Recinto {
    private String idRecinto;
    private String nombre;
    private String direccion;
    private String ciudad;
    private List<Zona> zonas;

    public Recinto (){
        this.zonas = new ArrayList<>();
    }

    public Recinto (String idRecinto, String nombre, String direccion, String ciudad) {
        this();
        this.idRecinto = idRecinto;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public String getIdRecinto() {
        return idRecinto;
    }

    public void setIdRecinto(String idRecinto) {
        this.idRecinto = idRecinto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public void setZonas(List<Zona> zonas) {
        this.zonas = zonas;
    }

    @Override
    public String toString() {
        return this.nombre != null ? this.nombre : "Sin nombre";
    }
}
