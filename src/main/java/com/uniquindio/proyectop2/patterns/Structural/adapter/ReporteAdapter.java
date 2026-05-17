package com.uniquindio.proyectop2.patterns.Structural.adapter;

import java.util.List;

public class ReporteAdapter {

    public static String [] convertirAFilaCSV(Object... campos){
        String [] fila = new String[campos.length];
        for(int i = 0; i < campos.length; i++){
            fila[i] = campos[i] != null ? campos[i].toString() : "";
        }
        return fila;
    }
}
