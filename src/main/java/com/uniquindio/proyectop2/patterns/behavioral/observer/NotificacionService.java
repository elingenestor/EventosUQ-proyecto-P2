package com.uniquindio.proyectop2.patterns.behavioral.observer;

import  com.uniquindio.proyectop2.Model.Compra;
import  com.uniquindio.proyectop2.Model.Evento;
import com.uniquindio.proyectop2.patterns.behavioral.observer.Observable;
import com.uniquindio.proyectop2.patterns.behavioral.observer.Observer;

public class NotificacionService implements Observer{
    @Override
    public void actualizar(Observable observable, Object data){
        if (observable instanceof Evento){
            Evento evento = (Evento) observable;
            System.out.println("Notificacion: El evento " + evento.getNombre() + "cambio su estado a " + data);
        } else if ( observable instanceof Compra){
            Compra compra = (Compra) observable;
            System.out.println("Notificacion: LA compra #" + compra.getIdCompra() + " cambio su estado a " + data);
        }
    }
}
