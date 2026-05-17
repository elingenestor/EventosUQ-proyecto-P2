package com.uniquindio.proyectop2.patterns.Creational.builder;

import com.uniquindio.proyectop2.Enums.CategoriaEvento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.Recinto;

import java.time.LocalDateTime;

public class EventoBuilder {
    private String idEvento;
    private String nombre;
    private CategoriaEvento categoria;
    private String descripcion;
    private String ciudad;
    private LocalDateTime fechaHora;
    private String politicasCAncelacion;
    private Recinto recinto;

    public EventoBuilder setIdEvento (String idEvento) {
        this.idEvento = idEvento;
        return this;
    }

    public EventoBuilder setNombre (String nombre) {
        this.nombre = nombre;
        return this;
    }

    public EventoBuilder setCategoria(CategoriaEvento categoria){
        this.categoria = categoria;
        return this;
    }

    public EventoBuilder setDescripcion(String descripcion){
        this.descripcion = descripcion;
        return this;
    }

    public EventoBuilder setCiudad (String ciusas){
        this.ciudad = ciudad;
        return this;
    }

    public EventoBuilder setFechaHora(LocalDateTime fechaHora){
        this.fechaHora = fechaHora;
        return this;
    }

    public EventoBuilder setPoliticasCancelacion (String piliticasCancelacion){
        this.politicasCAncelacion = politicasCAncelacion;
        return this;
    }

    public EventoBuilder setRecinto(Recinto recinto){
        this.recinto = recinto;
        return this;
    }

    public Evento build() {
        Evento evento = new Evento();
        evento.setIdEvento(idEvento);
        evento.setNombre(nombre);
        evento.setCategoria(categoria);
        evento.setDescripcion(descripcion);
        evento.setCiudad(ciudad);
        evento.setFechaHora(fechaHora);
        evento.setPoliticasCancelacion(politicasCAncelacion);
        evento.setRecinto(recinto);
        return evento;
    }
}
