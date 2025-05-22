package com.series.tracker.dao;

import java.util.List;
import com.series.tracker.models.UsuarioSerie;

public interface UsuarioSerieDao {

    void registrarVisualizacion(UsuarioSerie usuarioSerie);

    List<UsuarioSerie> obtenerSeriesPorUsuarioId(long usuarioId);

    UsuarioSerie obtenerVisualizacion(long usuarioId, long serieId);

    UsuarioSerie obtenerVisualizacionPorId(long usuarioId, long serieId);

    void modificarVisualizacion(UsuarioSerie usuarioSerie);

    void eliminarVisualizacion(long usuarioId, long serieId);
}
