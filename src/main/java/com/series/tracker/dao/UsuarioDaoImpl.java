package com.series.tracker.dao;

import com.series.tracker.models.Serie;
import com.series.tracker.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UsuarioSerieDao usuarioSerieDao;

    @Override
    public void crearUsuario(Usuario usuario) {
        entityManager.persist(usuario);
    }

    @Override
    public List<Usuario> obtenerUsuarios() {
        String query = "FROM Usuario WHERE activo = :activo";

        return entityManager.createQuery(query, Usuario.class)
                .setParameter("activo", true)
                .getResultList();
    }

    @Override
    public Usuario obtenerUsuarioPorId(long id) {
        String query = "FROM Usuario WHERE id = :id AND activo = :activo";

        List<Usuario> lista = entityManager.createQuery(query, Usuario.class)
                .setParameter("id", id)
                .setParameter("activo", true)
                .getResultList();

        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
        entityManager.merge(usuario);
    }

    @Override
    public void eliminarUsuario(long id) {
        Usuario usuario = entityManager.find(Usuario.class, id);

        if (usuario != null) {
            String querySeriesIds = "SELECT us.serie.id FROM UsuarioSerie us WHERE us.usuario.id = :usuarioId AND us.activo = true";
            List<Long> seriesIds = entityManager.createQuery(querySeriesIds, Long.class)
                    .setParameter("usuarioId", id)
                    .getResultList();

            usuarioSerieDao.eliminarVisualizacionesPorUsuarioId(id);

            for (Long serieId : seriesIds) {
                Serie serie = entityManager.find(Serie.class, serieId);

                if (serie != null) {
                    serie.setActivo(false);
                    entityManager.merge(serie);
                }
            }

            usuario.setActivo(false);
            entityManager.merge(usuario);
        }
    }

    @Override
    public Usuario obtenerUsuarioPorCredenciales(Usuario usuario){
        String query = "FROM Usuario WHERE correo = :correo AND activo = :activo";

        List<Usuario> lista = entityManager.createQuery(query, Usuario.class)
                .setParameter("correo", usuario.getCorreo())
                .setParameter("activo", true)
                .getResultList();

        if(lista.isEmpty()){
            return null;
        }

        String passwordHashed = lista.get(0).getPassword();
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        return argon2.verify(passwordHashed, usuario.getPassword()) ? lista.get(0) : null;
    }
}
