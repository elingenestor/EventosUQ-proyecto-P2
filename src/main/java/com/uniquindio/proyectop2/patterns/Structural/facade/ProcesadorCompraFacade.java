package com.uniquindio.proyectop2.patterns.Structural.facade;

import com.uniquindio.proyectop2.Model.Asiento;
import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.MetodoPago;
import com.uniquindio.proyectop2.Model.ServicioAdicional;
import com.uniquindio.proyectop2.Model.Usuario;
import com.uniquindio.proyectop2.service.impl.CompraServiceImpl;

import java.util.List;

public class ProcesadorCompraFacade {

    private final CompraServiceImpl compraService;

    public ProcesadorCompraFacade() {
        this.compraService = new CompraServiceImpl();
    }

    public Compra procesarCompra(Usuario usuario, Evento evento, List<Asiento> asientos, List<ServicioAdicional> servicios, MetodoPago metodoPago) throws Exception {
        Compra compra = compraService.crearCompra(usuario, evento, asientos, servicios);
        if (metodoPago != null) {
            compraService.pagarCompra(compra.getIdCompra(), metodoPago);
            compra = compraService.obtenerCompraPorId(compra.getIdCompra());
        }
        return compra;
    }
}
