package com.uniquindio.proyectop2.dao.interfaces;

import com.uniquindio.proyectop2.Enums.EstadoAsiento;
import com.uniquindio.proyectop2.Model.Asiento;
import java.util.List;

public interface AsientoDAO {
    Asiento findById(String id);
    List<Asiento> findByZona(String idZona);
    void save(Asiento asiento);
    void update(Asiento asiento);
    void delete(String id);
    void cambiarEstado(String idAsiento, EstadoAsiento nuevoEstado);
    void bloquearAsientos(List<Integer> idsAsientos);
    void liberarAsientos(List<Integer> idsAsientos);
}
