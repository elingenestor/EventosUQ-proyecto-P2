package com.uniquindio.proyectop2.dao.interfaces;

import com.uniquindio.proyectop2.Enums.CategoriaEvento;
import com.uniquindio.proyectop2.Enums.EstadoEvento;
import com.uniquindio.proyectop2.Model.Evento;

import java.time.LocalDate;
import java.util.List;

public interface EventoDAO {
    Evento findById(String id);
    List<Evento> findAll();
    List<Evento> findByFiltros(String ciudad, CategoriaEvento categoriaEvento, LocalDate inicio, LocalDate fin);
    void save(Evento evento);
    void update(Evento evento);
    void delete (String id);
    void cambiarEstado(String id, EstadoEvento nuevoEstado);
}
