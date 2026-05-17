package com.uniquindio.proyectop2.service.interfaces;

import com.uniquindio.proyectop2.Model.*;

import java.util.List;

public interface CompraService {
    Compra crearCompra(Usuario usuario, Evento evento, List<Asiento> asientos, List<ServicioAdicional> servicios) throws Exception;
    void modificarCompra(String idCompra, List<Asiento> nuevosAsientos, List<ServicioAdicional> nuevosServicios) throws Exception;
    void cancelarCompra(String idCompra) throws Exception;
    void pagarCompra(String idCompra, MetodoPago metodo) throws Exception;
    void reembolsarCompra(String idCompra) throws Exception;
    double calcularTotal(Compra compra) throws Exception;
}
