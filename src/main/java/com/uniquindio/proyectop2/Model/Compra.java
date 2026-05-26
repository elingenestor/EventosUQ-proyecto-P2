package com.uniquindio.proyectop2.Model;

import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.patterns.Structural.decorator.ComponenteCompra;
import com.uniquindio.proyectop2.patterns.behavioral.state.EstadoCompraState;
import com.uniquindio.proyectop2.patterns.behavioral.state.EstadoCreada;
import com.uniquindio.proyectop2.patterns.behavioral.observer.Observer;
import com.uniquindio.proyectop2.patterns.behavioral.observer.Observable;
import com.uniquindio.proyectop2.patterns.behavioral.state.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Compra implements ComponenteCompra, Observable {
    private String idCompra;
    private LocalDateTime fechaCreacion;
    private double total;
    private EstadoCompra estado;
    private EstadoCompraState estadoCompraState;
    private Usuario usuario;
    private Evento evento;
    private List<Entrada> entradas;
    private List<ServicioAdicional> serviciosAdicionales;
    private MetodoPago metodoPagoUsado;
    private List<Observer> observers;

    public Compra(){
        this.entradas = new ArrayList<>();
        this.serviciosAdicionales = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
        this.total = 0.0;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoCompra estado) {
        this.estado = estado;
        notificarObservers();
    }
    public Usuario getUsuario(){
        return usuario;
    }
    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }
    public Evento getEvento(){
        return evento;
    }
    public void setEvento(Evento evento){
        this.evento = evento;
    }
    public List<Entrada> getEntradas(){
        return entradas;
    }
    public void setEntradas(List<Entrada> entradas){
        this.entradas = entradas;
    }
    public List<ServicioAdicional> getServiciosAdicionales(){
        return serviciosAdicionales;
    }
    public void setServiciosAdicionales(List<ServicioAdicional> serviciosAdicionales){
        this.serviciosAdicionales = serviciosAdicionales;
    }
    public MetodoPago getMetodoPagoUsado(){
        return metodoPagoUsado;
    }
    public void SetMetodoPagoUsado (MetodoPago metodoPagoUsado){
        this.metodoPagoUsado = metodoPagoUsado;
    }
    public EstadoCompraState getEstadoCompraState() { return estadoCompraState; }

    public void setEstadoCompraState(EstadoCompraState nuevoEstado) {
        this.estadoCompraState = nuevoEstado;
        if (nuevoEstado instanceof EstadoCreada) {
            this.estado = EstadoCompra.CREADA;
        } else if (nuevoEstado instanceof EstadoPagada) {
            this.estado = EstadoCompra.PAGADA;
        } else if (nuevoEstado instanceof EstadoConfirmada) {
            this.estado = EstadoCompra.CONFIRMADA;
        } else if (nuevoEstado instanceof EstadoCancelada) {
            this.estado = EstadoCompra.CANCELADA;
        } else if (nuevoEstado instanceof EstadoReembolsada) {
            this.estado = EstadoCompra.REEMBOLSADA;
        }
        notificarObservers();
    }
    @Override
    public double getCosto() {
        double costo = 0.0;
        for (Entrada entrada : entradas) {
            costo += entrada.getPrecioFinal();
        }
        for (ServicioAdicional servicio : serviciosAdicionales) {
            costo += servicio.getPrecio();
        }
        return costo;
    }

    @Override
    public String getDescripcion(){
        return "Compra #" + idCompra + " - " + (evento != null ? evento.getNombre() : "Sin evento");
    }

    @Override
    public void agregarObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removerObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers() {
        for (Observer observer : observers) {
            observer.actualizar(this, this.estado);
        }
    }

    public void pagar() throws Exception {
        estadoCompraState.pagar(this);
    }

    public void cancelar() throws Exception {
        estadoCompraState.cancelar(this);
    }

    public void confirmar() throws Exception {
        estadoCompraState.confirmar(this);
    }

    public void reembolsar() throws Exception {
        estadoCompraState.reembolsar(this);
    }
}
