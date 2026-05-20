package com.uniquindio.proyectop2.patterns.Creational.factory;

import com.uniquindio.proyectop2.Enums.TipoIncidencia;
import com.uniquindio.proyectop2.Model.Incidencia;

public class IncidenciaFactory {
    public static Incidencia createIncidencia(TipoIncidencia tipoIncidencia,String descripcion, String entidadAfectada) {
        Incidencia incidencia = new Incidencia();
        incidencia.setTipo(tipoIncidencia);
        incidencia.setDescripcion(descripcion);
        incidencia.setEntidadAfectada(entidadAfectada);
        return incidencia;
    }
}
