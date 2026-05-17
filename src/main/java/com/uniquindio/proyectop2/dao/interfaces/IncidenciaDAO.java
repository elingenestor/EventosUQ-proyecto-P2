package com.uniquindio.proyectop2.dao.interfaces;

import com.uniquindio.proyectop2.Enums.TipoIncidencia;
import com.uniquindio.proyectop2.Model.Incidencia;
import java.time.LocalDate;
import java.util.List;

public interface IncidenciaDAO {
    Incidencia findById (String id);
    List<Incidencia> findAll ();
    List<Incidencia> findByRangoFechas (LocalDate inicio, LocalDate fin);
    List<Incidencia> findByTipo (TipoIncidencia tipo);
    void save (Incidencia incidencia);
    void update (Incidencia incidencia);
    void delete (String id);
}
