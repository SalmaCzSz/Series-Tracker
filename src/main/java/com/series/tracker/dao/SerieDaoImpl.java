package com.series.tracker.dao;

import com.series.tracker.models.Serie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class SerieDaoImpl implements SerieDao{
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private UsuarioSerieDao usuarioSerieDao;

    @Override
    public void crearSerie(Serie serie) {
        entityManager.persist(serie);
    }

    @Override
    public List<Serie> obtenerSeries() {
        String query = "FROM Serie WHERE activo = :activo";

        return entityManager.createQuery(query, Serie.class)
                .setParameter("activo", true)
                .getResultList();
    }

    @Override
    public void modificarSerie(Serie serie) {
        entityManager.merge(serie);
    }

    @Override
    public void eliminarSerie(long id) {
        Serie serie = entityManager.find(Serie.class, id);

        if (serie != null) {
            serie.setActivo(false);
            entityManager.merge(serie);

            usuarioSerieDao.eliminarVisualizacionesPorSerieId(id);
        }
    }

    @Override
    public Serie obtenerSeriePorId(long id) {
        String query = "FROM Serie WHERE id = :id AND activo = :activo";

        List<Serie> lista = entityManager.createQuery(query, Serie.class)
                .setParameter("id", id)
                .setParameter("activo", true)
                .getResultList();

        return lista.isEmpty() ? null : lista.get(0);
    }
}
