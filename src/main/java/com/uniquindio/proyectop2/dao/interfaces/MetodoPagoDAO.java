package com.uniquindio.proyectop2.dao.interfaces;

import com.uniquindio.proyectop2.Model.MetodoPago;
import java.util.List;

public interface MetodoPagoDAO {
    MetodoPago findById (String id);
    List<MetodoPago> findByUsuario (String idUsuario);
    void save (MetodoPago metodoPago);
    void update (MetodoPago metodoPago);
    void delete (String id);
}
