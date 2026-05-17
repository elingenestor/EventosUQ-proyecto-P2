package com.uniquindio.proyectop2.dao.interfaces;

import com.uniquindio.proyectop2.Model.Usuario;
import java.util.List;

public interface UsuarioDAO {
    Usuario findById(String id);
    Usuario findByEmail(String email);
    List<Usuario> findAll();
    void save(Usuario usuario);
    void update(Usuario usuario);
    void delete(String id);
    boolean existeEmail(String email);
}
