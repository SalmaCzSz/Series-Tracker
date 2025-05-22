package com.series.tracker.dao;

import com.series.tracker.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class UsuarioDaoImpl implements UsuarioDao{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void crearUsuario(Usuario usuario) {
        entityManager.persist(usuario);
    }

    @Override
    public List<Usuario> obtenerUsuarios() {
        String query = "FROM Usuario";

        return entityManager.createQuery(query, Usuario.class).getResultList();
    }

    @Override
    public Usuario obtenerUsuarioPorId(long id) {
        return entityManager.find(Usuario.class, id);
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
        entityManager.merge(usuario);
    }

    @Override
    public void eliminarUsuario(long id) {
        Usuario usuario = entityManager.find(Usuario.class, id);

        if (usuario != null) {
            entityManager.remove(usuario);
        }
    }

    @Override
    public Usuario obtenerUsuarioPorCredenciales(Usuario usuario){
        String query = "FROM Usuario WHERE correo = :correo";

        List<Usuario> lista = entityManager.createQuery(query, Usuario.class)
                .setParameter("correo", usuario.getCorreo())
                .getResultList();

        if(lista.isEmpty()){
            return null;
        }

        String passwordHashed = lista.get(0).getPassword();
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        if(argon2.verify(passwordHashed, usuario.getPassword())){
            return lista.get(0);
        } else {
            return null;
        }
    }
}
