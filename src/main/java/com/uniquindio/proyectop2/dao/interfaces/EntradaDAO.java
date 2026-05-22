package com.uniquindio.proyectop2.dao.interfaces;

import com.uniquindio.proyectop2.Model.Entrada;
import com.uniquindio.proyectop2.Enums.EstadoEntrada;
import java.util.List;

public interface EntradaDAO {
    Entrada findById (String id);
    List<Entrada> findByCompra(String idCompra);
    List<Entrada> findByEvento(String idEvento);
    void save (Entrada entrada);
    void update(Entrada entrada);
    void delete (String id);
    void cambiarEstado (String id, EstadoEntrada nuevoEstado);
}
