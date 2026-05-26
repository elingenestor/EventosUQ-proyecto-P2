package com.uniquindio.proyectop2.service.impl;

import com.uniquindio.proyectop2.dao.interfaces.AsientoDAO;
import com.uniquindio.proyectop2.dao.interfaces.EventoDAO;
import com.uniquindio.proyectop2.Enums.CategoriaEvento;
import com.uniquindio.proyectop2.Enums.EstadoEvento;
import com.uniquindio.proyectop2.Model.Asiento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.Zona;
import com.uniquindio.proyectop2.service.interfaces.EventoService;

import java.time.LocalDate;
import java.util.List;

public class EventoServiceImpl implements EventoService {
    private final EventoDAO eventoDAO;
    private final AsientoDAO asientoDAO;

    public EventoServiceImpl(EventoDAO eventoDAO, AsientoDAO asientoDAO) {
        this.eventoDAO = eventoDAO;
        this.asientoDAO = asientoDAO;
    }

    @Override
    public void crearEvento(Evento evento) throws Exception {
        validarEvento(evento);
        evento.setEstado(EstadoEvento.BORRADOR);
        eventoDAO.save(evento);
    }

    @Override
    public void actualizarEvento(Evento evento) throws Exception {
        validarEvento(evento);
        if (evento.getIdEvento() == null || evento.getIdEvento().isBlank()) {
            throw new Exception("El evento no tiene un ID válido para actualizar.");
        }
        eventoDAO.update(evento);
    }

    @Override
    public void publicarEvento(String idEvento) throws Exception {
        eventoDAO.cambiarEstado(idEvento, EstadoEvento.PUBLICADO);
    }

    @Override
    public void pausarEvento(String idEvento) throws Exception {
        eventoDAO.cambiarEstado(idEvento, EstadoEvento.PAUSADO);
    }

    @Override
    public void cancelarEvento(String idEvento) throws Exception {
        if (idEvento == null || idEvento.isBlank()) {
            throw new Exception("El ID del evento es obligatorio.");
        }
        eventoDAO.cambiarEstado(idEvento, EstadoEvento.CANCELADO);
    }

    @Override
    public List<Evento> listarEventosDisponibles(String ciudad, CategoriaEvento categoria, LocalDate fechaInicio, LocalDate fechaFin) throws Exception {
        List<Evento> eventos;

        if (ciudad == null && categoria == null && fechaInicio == null && fechaFin == null) {
            eventos = eventoDAO.findAll();
        } else {
            eventos = eventoDAO.findByFiltros(ciudad, categoria, fechaInicio, fechaFin);
        }

        return eventos.stream()
                .filter(evento -> evento.getEstado() == EstadoEvento.PUBLICADO)
                .filter(evento -> evento.getFechaHora() != null && !evento.getFechaHora().isBefore(java.time.LocalDateTime.now()))
                .toList();
    }

    @Override
    public Evento obtenerDetalleEvento(String idEvento) throws Exception {
        return eventoDAO.findById(idEvento);
    }

    @Override
    public boolean verificarDisponibilidadAsientos(String idEvento, Zona zona, List<Asiento> asientos) throws Exception {
        for (Asiento a : asientos) {
            Asiento asientoBD = asientoDAO.findById(a.getIdAsiento());
            if (asientoBD == null || asientoBD.getEstado() != com.uniquindio.proyectop2.Enums.EstadoAsiento.DISPONIBLE) {
                return false;
            }
        }
        return true;
    }

    private void validarEvento(Evento evento) throws Exception {
        if (evento == null) throw new Exception("El evento no puede ser nulo.");
        if (evento.getNombre() == null || evento.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
        if (evento.getCategoria() == null) throw new Exception("La categoría es obligatoria.");
        if (evento.getCiudad() == null || evento.getCiudad().isBlank()) throw new Exception("La ciudad es obligatoria.");
        if (evento.getFechaHora() == null) throw new Exception("La fecha y hora son obligatorias.");
    }
}
