package com.uniquindio.proyectop2.Model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String idUsuario;
    private String nombreCompleto;
    private String email;   //Debe ser unico
    private String password;
    private String telefono;
    private List<MetodoPago> metodosPago;
    private List<Compra> compras;

    public Usuario (){
        this.metodosPago = new ArrayList<>();
        this.compras = new ArrayList<>();
    }

    public Usuario (String idUsuario, String nombreCompleto, String email, String password, String telefono){
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
    }

    //Getters y Setters

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public List<MetodoPago> getMetodosPago() {
        return metodosPago;
    }

    public void setMetodosPago(List<MetodoPago> metodosPago) {
        this.metodosPago = metodosPago;
    }

    public void agregarMetodoPago(MetodoPago metodo) {
        this.metodosPago.add(metodo);
    }

    public void agregarCompra(Compra compra) {
        this.compras.add(compra);
    }
}
