package com.uniquindio.proyectop2.Model;

import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.patterns.Structural.decorator.ComponenteCompra;
import com.uniquindio.proyectop2.patterns.behavioral.observer.Observer;
import com.uniquindio.proyectop2.patterns.behavioral.observer.Observable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Compra implements ComponenteCompra,Observable {
    private String idCompra;
    private LocalDateTime fechaCreacion;
    private double total;
    private EstadoCompra estado;
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

    // Getters y setter


    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCOmpra) {
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
        notificarObserver();
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

    @Override
    public double getCosto(){
        double costo = 0.0;
        for (Entrada entrada : entradas){
            costo += entrada.getPrecioFinal();
        }
        for (ServicioAdicional servicio : serviciosAdicionales){
            costo += servicio.getPrecio();
        }
        return costo;
    }
@Override
    public String getDescripcion(){
        return "Compra #" + idCompra + " - " + evento.getNombre();
    }

    //Observer
    @Override
    public void agregarObserver(Observer observer){
        observers.add(observer);
    }

    @Override
    public void removerObserver(Observer observer){
        observers.remove(observer);
    }



    @Override
    public void notificarObserver(){
        for(Observer observer : observers){
            observer.actualizar(this, this.estado);
        }
    }
}
