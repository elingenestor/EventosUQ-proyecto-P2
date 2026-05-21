package com.uniquindio.proyectop2.patterns.Structural.facade;

import com.uniquindio.proyectop2.Model.*;
import com.uniquindio.proyectop2.service.interfaces.CompraService;
import com.uniquindio.proyectop2.service.interfaces.EventoService;

import java.util.List;

public class ProcesadorCompraFacade {
    private final CompraService compraService;
    private final EventoService eventoService;

    public ProcesadorCompraFacade(CompraService compraService, EventoService eventoService) {
        this.compraService = compraService;
        this.eventoService = eventoService;
    }

    public Compra realizarCompra(Usuario usuario, Evento evento, List<Asiento> asientos, List<ServicioAdicional> servicios, MetodoPago metodoPago) throws Exception {
        // 1. Verificar disponibilidad
        if (!eventoService.verificarDisponibilidadAsientos(evento.getIdEvento(), null, asientos)) {
            throw new Exception("Algunos asientos no están disponibles.");
        }
        // 2. Crear compra
        Compra compra = compraService.crearCompra(usuario, evento, asientos, servicios);
        // 3. Pagar compra
        compraService.pagarCompra(compra.getIdCompra(), metodoPago);
        // 4. (Opcional) Enviar notificaciones
           return compra;
    }
}
