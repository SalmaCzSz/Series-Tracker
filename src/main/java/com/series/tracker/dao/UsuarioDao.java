package com.series.tracker.dao;

import java.util.List;
import com.series.tracker.models.Usuario;

public interface UsuarioDao {
    void crearUsuario(Usuario usuario);

    List<Usuario> obtenerUsuarios();

    Usuario obtenerUsuarioPorId(long id);

    void modificarUsuario(Usuario usuario);

    void eliminarUsuario(long id);

    Usuario obtenerUsuarioPorCredenciales(Usuario usuario);
}