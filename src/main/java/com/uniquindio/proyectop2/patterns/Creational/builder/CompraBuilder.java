package com.uniquindio.proyectop2.patterns.Creational.builder;


import com.uniquindio.proyectop2.Model.*;

import java.util.List;
import java.util.ArrayList;

public class CompraBuilder {
    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private List<Entrada> entradas = new ArrayList<>();
    private List<ServicioAdicional> servicios = new ArrayList<>();
    private MetodoPago metodoPago;

    public CompraBuilder setIdCompra (String idCompra){
        this.idCompra = idCompra;
        return this;
    }

    public CompraBuilder setUsuario ( Usuario usuario){
        this.usuario = usuario;
        return this;
    }

    public CompraBuilder setEvento (Evento evento){
        this.evento = evento;
        return this;
    }

    public CompraBuilder addEntrada (Entrada entrada){
        this.entradas.add(entrada);
        return this;
    }

    public CompraBuilder addServicio (ServicioAdicional servicio){
        this.servicios.add(servicio);
        return this;
    }

    public CompraBuilder setMetodoPago (MetodoPago metodoPago){
        this.metodoPago = metodoPago;
        return this;
    }

    public Compra Build() {
        Compra compra = new Compra();
        compra.setIdCompra(idCompra);
        compra.setUsuario(usuario);
        compra.setEvento(evento);
        compra.getEntradas().addAll(entradas);
        compra.getServiciosAdicionales().addAll(servicios);
        compra.SetMetodoPagoUsado(metodoPago);
        compra.setTotal(compra.getCosto());
        return compra;
    }
}
