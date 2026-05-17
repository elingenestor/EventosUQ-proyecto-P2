package com.uniquindio.proyectop2.service.interfaces;

import com.uniquindio.proyectop2.Enums.CategoriaEvento;
import com.uniquindio.proyectop2.Model.Asiento;
import com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.Model.Zona;

import java.time.LocalDate;
import java.util.List;

public interface EventoService {
    void crearEvento(Evento evento) throws Exception;
    void actualizarEvento(Evento evento) throws Exception;
    void publicarEvento(String idEvento) throws Exception;
    void pausarEvento(String idEvento) throws Exception;
    void cancelarEvento(String idEvento) throws Exception;
    List<Evento> listarEventosDisponibles(String ciudad, CategoriaEvento categoria,LocalDate fechaInicio, LocalDate fechaFin) throws Exception;
    Evento obtenerDetalleEvento(String idEvento) throws Exception;
    boolean verificarDisponibilidadAsientos(String idEvento, Zona zona, List<Asiento> asientos) throws Exception;


}
