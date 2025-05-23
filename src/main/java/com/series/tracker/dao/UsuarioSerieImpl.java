package com.series.tracker.dao;

import com.series.tracker.models.UsuarioSerie;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class UsuarioSerieImpl implements UsuarioSerieDao{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void registrarVisualizacion(UsuarioSerie usuarioSerie) {
        entityManager.persist(usuarioSerie);
    }

    @Override
    public List<UsuarioSerie> obtenerSeriesPorUsuarioId(long usuarioId) {
        String query = "FROM UsuarioSerie WHERE usuario.id = :usuarioId";

        return entityManager.createQuery(query, UsuarioSerie.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

    @Override
    public UsuarioSerie obtenerVisualizacion(long usuarioId, long serieId) {
        String query = "FROM UsuarioSerie WHERE usuario.id = :usuarioId AND serie.id = :serieId";

        List<UsuarioSerie> lista = entityManager.createQuery(query, UsuarioSerie.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("serieId", serieId)
                .getResultList();

        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public UsuarioSerie obtenerVisualizacionPorId(long usuarioId, long serieId) {
        String query = "FROM UsuarioSerie WHERE usuario.id = :usuarioId AND serie.id = :serieId";

        List<UsuarioSerie> lista = entityManager.createQuery(query, UsuarioSerie.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("serieId", serieId)
                .getResultList();

        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public void modificarVisualizacion(UsuarioSerie usuarioSerie) {
        entityManager.merge(usuarioSerie);
    }

    @Override
    public void eliminarVisualizacion(long usuarioId, long serieId) {
        UsuarioSerie usuarioSerie = obtenerVisualizacion(usuarioId, serieId);

        if (usuarioSerie != null) {
            entityManager.remove(usuarioSerie);
        }
    }
}
