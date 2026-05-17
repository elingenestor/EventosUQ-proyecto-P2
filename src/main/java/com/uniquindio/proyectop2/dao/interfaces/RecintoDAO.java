package com.uniquindio.proyectop2.dao.interfaces;
 import com.uniquindio.proyectop2.Model.Recinto;
 import java.util.List;

public interface RecintoDAO {
    Recinto finById(String id);
    List<Recinto> findAll();
    List<Recinto> finByCiudad(String ciudad);
    void save(Recinto recinto);
    void update(Recinto recinto);
    void delete(String id);
}
