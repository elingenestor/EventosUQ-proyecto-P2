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
        evento.setEstado(EstadoEvento.BORRADOR);
        eventoDAO.save(evento);
    }

    @Override
    public void actualizarEvento(Evento evento) throws Exception {
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
        eventoDAO.cambiarEstado(idEvento, EstadoEvento.CANCELADO);
        // Aquí se notificará a los observadores automáticamente por el modelo
    }

    @Override
    public List<Evento> listarEventosDisponibles(String ciudad, CategoriaEvento categoria, LocalDate fechaInicio, LocalDate fechaFin) throws Exception {
        return eventoDAO.findByFiltros(ciudad, categoria, fechaInicio, fechaFin);
    }

    @Override
    public Evento obtenerDetalleEvento(String idEvento) throws Exception {
        return eventoDAO.findById(idEvento);
    }

    @Override
    public boolean verificarDisponibilidadAsientos(String idEvento, Zona zona, List<Asiento> asientos) throws Exception {
        // Lógica de verificación: comprobar que todos los asientos estén disponibles
        for (Asiento a : asientos) {
            Asiento asientoBD = asientoDAO.findById(a.getIdAsiento());
            if (asientoBD == null || asientoBD.getEstado() != com.uniquindio.proyectop2.Enums.EstadoAsiento.DISPONIBLE) {
                return false;
            }
        }
        return true;
    }
}
