package com.uniquindio.proyectop2.service.interfaces;


import  com.uniquindio.proyectop2.Model.MetodoPago;
import  com.uniquindio.proyectop2.Model.Usuario;

import java.util.List;

public interface UsuarioService {
    void registrar(Usuario usuario) throws Exception;
    Usuario login(String email, String password) throws Exception;
    void actualizarPerfil(Usuario usuario) throws Exception;
    void agregarMetodoPago(Usuario usuario,MetodoPago metodoPago) throws Exception;
    List<MetodoPago> listarMetodosPago(Usuario usuario) throws Exception;
}
