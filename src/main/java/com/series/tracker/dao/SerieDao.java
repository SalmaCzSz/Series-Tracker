package com.series.tracker.dao;

import java.util.List;
import com.series.tracker.models.Serie;

public interface SerieDao {
    void crearSerie(Serie serie);

    List<Serie> obtenerSeries();

    void modificarSerie(Serie serie);

    void eliminarSerie(long id);

    Serie obtenerSeriePorId(long id);
}
