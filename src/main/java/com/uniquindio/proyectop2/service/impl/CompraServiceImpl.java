package com.uniquindio.proyectop2.service.impl;

import com.uniquindio.proyectop2.Enums.EstadoAsiento;
import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Enums.EstadoEntrada;
import com.uniquindio.proyectop2.Model.*;
import com.uniquindio.proyectop2.dao.interfaces.*;
import com.uniquindio.proyectop2.patterns.Creational.builder.CompraBuilder;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;

import java.util.ArrayList;
import java.util.List;

public class CompraServiceImpl implements com.uniquindio.proyectop2.service.interfaces.CompraService {

    private final CompraDAO compraDAO;
    private final EntradaDAO entradaDAO;
    private final AsientoDAO asientoDAO;
    private final ServicioAdicionalDAO servicioAdicionalDAO;

    public CompraServiceImpl() {
        this(DAOFactory.crearCompraDAO(), DAOFactory.crearEntradaDAO(), DAOFactory.crearAsientoDAO(), DAOFactory.crearServicioAdicionalDAO());
    }

    public CompraServiceImpl(CompraDAO compraDAO, EntradaDAO entradaDAO, AsientoDAO asientoDAO, ServicioAdicionalDAO servicioAdicionalDAO) {
        this.compraDAO = compraDAO;
        this.entradaDAO = entradaDAO;
        this.asientoDAO = asientoDAO;
        this.servicioAdicionalDAO = servicioAdicionalDAO;
    }

    @Override
    public Compra crearCompra(Usuario usuario, Evento evento, List<Asiento> asientos, List<ServicioAdicional> servicios) throws Exception {
        if (usuario == null || evento == null) {
            throw new Exception("La compra requiere un usuario y un evento.");
        }
        if (asientos == null || asientos.isEmpty()) {
            throw new Exception("Debes seleccionar al menos un asiento.");
        }

        for (Asiento asiento : asientos) {
            Asiento asientoBD = asientoDAO.findById(asiento.getIdAsiento());
            if (asientoBD == null || asientoBD.getEstado() != EstadoAsiento.DISPONIBLE) {
                throw new Exception("Uno de los asientos ya no está disponible.");
            }
        }

        CompraBuilder builder = new CompraBuilder()
                .setUsuario(usuario)
                .setEvento(evento);

        List<Entrada> entradas = new ArrayList<>();
        for (Asiento asiento : asientos) {
            asientoDAO.cambiarEstado(asiento.getIdAsiento(), EstadoAsiento.RESERVADO);
            Entrada entrada = new Entrada();
            entrada.setAsiento(asiento);
            entrada.setZona(asiento.getZona());
            entrada.setPrecioFinal(asiento.getZona() != null ? asiento.getZona().getPrecioBase() : 0.0);
            entrada.setEstadoEntrada(EstadoEntrada.ACTIVA);
            entradas.add(entrada);
            builder.addEntrada(entrada);
        }

        if (servicios != null) {
            for (ServicioAdicional servicio : servicios) {
                ServicioAdicional servicioBD = servicioAdicionalDAO.findById(servicio.getIdServicio());
                if (servicioBD != null) {
                    builder.addServicio(servicioBD);
                }
            }
        }

        Compra compra = builder.Build();
        compra.setEstado(EstadoCompra.CREADA);
        compra.setTotal(compra.getCosto());
        String idCompra = compraDAO.save(compra);

        for (Entrada entrada : entradas) {
            entrada.setCompra(compra);
            entradaDAO.save(entrada);
        }

        compra.setEntradas(entradas);
        return compra;
    }

    @Override
    public void modificarCompra(String idCompra, List<Asiento> nuevosAsientos, List<ServicioAdicional> nuevosServicios) throws Exception {
        throw new UnsupportedOperationException("Modificar compra aún no está implementado.");
    }

    @Override
    public void cancelarCompra(String idCompra) throws Exception {
        Compra compra = compraDAO.findById(idCompra);
        if (compra == null) {
            throw new Exception("No existe la compra.");
        }
        compra.setEstado(EstadoCompra.CANCELADA);
        compraDAO.update(compra);

        List<Entrada> entradas = entradaDAO.findByCompra(idCompra);
        for (Entrada entrada : entradas) {
            if (entrada.getAsiento() != null) {
                asientoDAO.cambiarEstado(entrada.getAsiento().getIdAsiento(), EstadoAsiento.DISPONIBLE);
            }
            entradaDAO.cambiarEstado(entrada.getIdEntrada(), EstadoEntrada.ANULADA);
        }
    }

    @Override
    public void pagarCompra(String idCompra, MetodoPago metodo) throws Exception {
        Compra compra = compraDAO.findById(idCompra);
        if (compra == null) {
            throw new Exception("No existe la compra.");
        }
        compra.SetMetodoPagoUsado(metodo);
        compra.setEstado(EstadoCompra.PAGADA);
        compraDAO.update(compra);

        List<Entrada> entradas = entradaDAO.findByCompra(idCompra);
        for (Entrada entrada : entradas) {
            if (entrada.getAsiento() != null) {
                asientoDAO.cambiarEstado(entrada.getAsiento().getIdAsiento(), EstadoAsiento.VENDIDO);
            }
            entradaDAO.cambiarEstado(entrada.getIdEntrada(), EstadoEntrada.ACTIVA);
        }
    }

    @Override
    public void reembolsarCompra(String idCompra) throws Exception {
        Compra compra = compraDAO.findById(idCompra);
        if (compra == null) {
            throw new Exception("No existe la compra.");
        }
        compra.setEstado(EstadoCompra.REEMBOLDADA);
        compraDAO.update(compra);

        List<Entrada> entradas = entradaDAO.findByCompra(idCompra);
        for (Entrada entrada : entradas) {
            if (entrada.getAsiento() != null) {
                asientoDAO.cambiarEstado(entrada.getAsiento().getIdAsiento(), EstadoAsiento.DISPONIBLE);
            }
            entradaDAO.cambiarEstado(entrada.getIdEntrada(), EstadoEntrada.ANULADA);
        }
    }

    @Override
    public double calcularTotal(Compra compra) throws Exception {
        if (compra == null) {
            throw new Exception("La compra no puede ser nula.");
        }
        return compra.getCosto();
    }

    public Compra obtenerCompraPorId(String idCompra) {
        return compraDAO.findById(idCompra);
    }
}
