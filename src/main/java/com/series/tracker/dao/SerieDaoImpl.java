package com.series.tracker.dao;

import com.series.tracker.models.Serie;
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

    @Override
    public void crearSerie(Serie serie) {
        entityManager.persist(serie);
    }

    @Override
    public List<Serie> obtenerSeries() {
        String query = "FROM Serie";

        return entityManager.createQuery(query, Serie.class).getResultList();
    }

    @Override
    public void modificarSerie(Serie serie) {
        entityManager.merge(serie);
    }

    @Override
    public void eliminarSerie(long id) {
        Serie serie = entityManager.find(Serie.class, id);

        if (serie != null) {
            entityManager.remove(serie);
        }
    }

    @Override
    public Serie obtenerSeriePorId(long id) {
        return entityManager.find(Serie.class, id);
    }
}
