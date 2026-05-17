package com.uniquindio.proyectop2.dao.interfaces;

import com.uniquindio.proyectop2.Model.ServicioAdicional;
import java.util.List;

public interface ServicioAdicionalDAO {
    ServicioAdicional findById(String id);
    List<ServicioAdicional> findAll();
    void save (ServicioAdicional servicioAdicional);
    void update (ServicioAdicional servicioAdicional);
    void delete (String id);

}
