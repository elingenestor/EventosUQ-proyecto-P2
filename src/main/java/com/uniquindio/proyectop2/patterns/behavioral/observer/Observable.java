package com.uniquindio.proyectop2.patterns.behavioral.observer;

import com.uniquindio.proyectop2.patterns.behavioral.observer.Observer;

public interface Observable {

    void agregarObserver(Observer observer);
    void removerObserver(Observer observer);
    void notificarObserver();
}
