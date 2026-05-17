package com.uniquindio.proyectop2.service.interfaces;

import com.uniquindio.proyectop2.Model.Recinto;
import com.uniquindio.proyectop2.Model.Usuario;

import java.util.List;

public interface AdminService {
    List<Usuario> listarUsuarios() throws Exception;
    void crearUsuario(Usuario usuario) throws Exception;
    void actualizarUsuario(Usuario usuario) throws Exception;
    void eliminarUsuario(String idUsuario) throws Exception;

    List<Recinto>  listarRecintos() throws Exception;
    void crearRecinto(Recinto recinto) throws Exception;
    void actualizarRecinto(Recinto recinto) throws Exception;
    void eliminarRecinto(String idRecinto) throws Exception;

    void generarReportes();
}
