package com.uniquindio.proyectop2.Model;

import com.uniquindio.proyectop2.Enums.EstadoEvento;
import com.uniquindio.proyectop2.Enums.CategoriaEvento;
import com.uniquindio.proyectop2.patterns.behavioral.observer.Observer;
import com.uniquindio.proyectop2.patterns.behavioral.observer.Observable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Evento implements Observable {
    private String idEvento;
    private String nombre;
    private CategoriaEvento categoria;
    private String  descripcion;
    private String ciudad;
    private LocalDateTime fechaHora;
    private EstadoEvento estado;
    private String politicasCancelacion;
    private Recinto recinto;
    private List<Zona> zonas;
    private List<Compra> compras;
    private List<Observer> observers;

    public Evento(){
        this.zonas = new ArrayList<>();
        this.compras = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CategoriaEvento getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEvento categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }

    public String getPoliticasCancelacion() {
        return politicasCancelacion;
    }

    public void setPoliticasCancelacion(String politicasCancelacion) {
        this.politicasCancelacion = politicasCancelacion;
    }

    public Recinto getRecinto() {
        return recinto;
    }

    public void setRecinto(Recinto recinto) {
        this.recinto = recinto;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public void setZonas(List<Zona> zonas) {
        this.zonas = zonas;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    @Override
    public void agregarObserver (Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removerObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers(){
        for (Observer observer : observers) {
            observer.actualizar(this, this.estado);
        }
    }
}
