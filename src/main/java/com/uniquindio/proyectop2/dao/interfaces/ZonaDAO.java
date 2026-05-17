package com.uniquindio.proyectop2.dao.interfaces;

import com.uniquindio.proyectop2.Model.Zona;
import java.util.List;

public interface ZonaDAO {
    Zona findById(String id);
    List<Zona> findByRecinto(String idRecinto);
    void save(Zona zona);
    void update(Zona zona);
    void delete(String id);

}
