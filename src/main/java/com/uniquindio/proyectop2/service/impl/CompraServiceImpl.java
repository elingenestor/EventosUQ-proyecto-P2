package com.uniquindio.proyectop2.service.impl;

import com.uniquindio.proyectop2.Enums.EstadoAsiento;
import com.uniquindio.proyectop2.Enums.EstadoCompra;
import com.uniquindio.proyectop2.Enums.EstadoEntrada;
import com.uniquindio.proyectop2.Model.*;
import com.uniquindio.proyectop2.dao.interfaces.*;
import com.uniquindio.proyectop2.patterns.Creational.builder.CompraBuilder;
import com.uniquindio.proyectop2.patterns.Creational.factory.DAOFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Compra compra = compraDAO.findById(idCompra);
        if (compra == null) {
            throw new Exception("No existe la compra.");
        }

        if (compra.getEstado() == EstadoCompra.CANCELADA
                || compra.getEstado() == EstadoCompra.REEMBOLSADA
                || compra.getEstado() == EstadoCompra.INCIDENCIA) {
            throw new Exception("La compra no se puede modificar en su estado actual.");
        }

        if (nuevosAsientos == null || nuevosAsientos.isEmpty()) {
            throw new Exception("Debes seleccionar al menos un asiento.");
        }

        // Validar que no haya asientos repetidos en la nueva selección
        Set<String> asientosVistos = new HashSet<>();
        for (Asiento asiento : nuevosAsientos) {
            if (asiento == null || asiento.getIdAsiento() == null) {
                throw new Exception("Hay asientos inválidos en la nueva selección.");
            }
            if (!asientosVistos.add(asiento.getIdAsiento())) {
                throw new Exception("No puedes repetir el mismo asiento en la compra.");
            }
        }

        List<Entrada> entradasAnteriores = entradaDAO.findByCompra(idCompra);
        Set<String> asientosActuales = new HashSet<>();
        for (Entrada entrada : entradasAnteriores) {
            if (entrada.getAsiento() != null && entrada.getAsiento().getIdAsiento() != null) {
                asientosActuales.add(entrada.getAsiento().getIdAsiento());
            }
        }

        // Validar disponibilidad antes de tocar la compra anterior
        List<Asiento> asientosValidados = new ArrayList<>();
        for (Asiento asiento : nuevosAsientos) {
            Asiento asientoBD = asientoDAO.findById(asiento.getIdAsiento());
            if (asientoBD == null) {
                throw new Exception("Uno de los asientos seleccionados no existe.");
            }
            boolean perteneceALaMismaCompra = asientosActuales.contains(asientoBD.getIdAsiento());
            if (!perteneceALaMismaCompra && asientoBD.getEstado() != EstadoAsiento.DISPONIBLE) {
                throw new Exception("Uno de los asientos ya no está disponible.");
            }
            asientosValidados.add(asientoBD);
        }

        List<ServicioAdicional> serviciosValidados = new ArrayList<>();
        if (nuevosServicios != null) {
            Set<String> serviciosVistos = new HashSet<>();
            for (ServicioAdicional servicio : nuevosServicios) {
                if (servicio == null || servicio.getIdServicio() == null) {
                    continue;
                }
                if (!serviciosVistos.add(servicio.getIdServicio())) {
                    continue;
                }
                ServicioAdicional servicioBD = servicioAdicionalDAO.findById(servicio.getIdServicio());
                if (servicioBD != null) {
                    serviciosValidados.add(servicioBD);
                }
            }
        }

        // Liberar asientos anteriores y eliminar sus entradas
        for (Entrada entrada : entradasAnteriores) {
            if (entrada.getAsiento() != null && entrada.getAsiento().getIdAsiento() != null) {
                asientoDAO.cambiarEstado(entrada.getAsiento().getIdAsiento(), EstadoAsiento.DISPONIBLE);
            }
            if (entrada.getIdEntrada() != null) {
                entradaDAO.delete(entrada.getIdEntrada());
            }
        }

        // Reservar los nuevos asientos y crear nuevas entradas
        List<Entrada> nuevasEntradas = new ArrayList<>();
        for (Asiento asientoBD : asientosValidados) {
            asientoDAO.cambiarEstado(asientoBD.getIdAsiento(), EstadoAsiento.RESERVADO);

            Entrada entrada = new Entrada();
            entrada.setCompra(compra);
            entrada.setAsiento(asientoBD);
            entrada.setZona(asientoBD.getZona());
            entrada.setPrecioFinal(asientoBD.getZona() != null ? asientoBD.getZona().getPrecioBase() : 0.0);
            entrada.setEstadoEntrada(EstadoEntrada.ACTIVA);
            entradaDAO.save(entrada);
            nuevasEntradas.add(entrada);
        }

        compra.setEntradas(nuevasEntradas);
        compra.setServiciosAdicionales(serviciosValidados);
        compra.setTotal(compra.getCosto());
        compraDAO.update(compra);
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
        compra.setEstado(EstadoCompra.REEMBOLSADA);
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
