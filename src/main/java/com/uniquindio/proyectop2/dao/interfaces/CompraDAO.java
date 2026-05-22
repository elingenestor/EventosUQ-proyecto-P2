package com.uniquindio.proyectop2.dao.interfaces;

import com.uniquindio.proyectop2.Model.Compra;
import com.uniquindio.proyectop2.Model.ServicioAdicional;

import java.util.List;

public interface CompraDAO {
    Compra findById(String id);
    List<Compra> findByUsuario(String idUsuario);
    String save(Compra compra);
    void update(Compra compra);
    void delete(String id);

    void saveServicioCompra(String idCompra, String idServicio);
    List<ServicioAdicional> findServiciosByCompra(String idCompra);
}