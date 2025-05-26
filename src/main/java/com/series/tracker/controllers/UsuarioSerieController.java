package com.series.tracker.controllers;

import com.series.tracker.dao.UsuarioSerieDao;
import com.series.tracker.models.UsuarioSerie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios/series")
public class UsuarioSerieController {
    @Autowired
    private UsuarioSerieDao usuarioSerieDao;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UsuarioSerie> registrarVisualizacion(@RequestBody UsuarioSerie usuarioSerie) {
        if (usuarioSerie.getUsuario() == null || usuarioSerie.getSerie() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        usuarioSerieDao.registrarVisualizacion(usuarioSerie);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSerie);
    }

    @RequestMapping(value = "/{usuarioId}", method = RequestMethod.GET)
    public ResponseEntity<List<UsuarioSerie>> obtenerSeriesPorUsuarioId(@PathVariable long usuarioId) {
        List<UsuarioSerie> series = usuarioSerieDao.obtenerSeriesPorUsuarioId(usuarioId);

        if (series.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(series);
    }

    @RequestMapping(value = "/{usuarioId}/{serieId}", method = RequestMethod.GET)
    public ResponseEntity<UsuarioSerie> obtenerVisualizacion(@PathVariable long usuarioId, @PathVariable long serieId) {
        UsuarioSerie usuarioSerie = usuarioSerieDao.obtenerVisualizacion(usuarioId, serieId);

        if (usuarioSerie != null) {
            return ResponseEntity.ok(usuarioSerie);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(value = "/{usuarioId}/{serieId}", method = RequestMethod.PUT)
    public ResponseEntity<UsuarioSerie> modificarVisualizacion(@PathVariable long usuarioId,
                                                               @PathVariable long serieId,
                                                               @RequestBody UsuarioSerie usuarioSerieActualizada) {
        UsuarioSerie usuarioSerieExistente = usuarioSerieDao.obtenerVisualizacion(usuarioId, serieId);

        if (usuarioSerieExistente != null) {
            if (usuarioSerieActualizada.getEstado() != null) {
                usuarioSerieExistente.setEstado(usuarioSerieActualizada.getEstado());
            }
            if (usuarioSerieActualizada.getPlataforma() != null) {
                usuarioSerieExistente.setPlataforma(usuarioSerieActualizada.getPlataforma());
            }
            if (usuarioSerieActualizada.getFecha_inicio() != null) {
                usuarioSerieExistente.setFecha_inicio(usuarioSerieActualizada.getFecha_inicio());
            }
            if (usuarioSerieActualizada.getFecha_fin() != null) {
                usuarioSerieExistente.setFecha_fin(usuarioSerieActualizada.getFecha_fin());
            }
            if (usuarioSerieActualizada.getFraseFavorita() != null) {
                usuarioSerieExistente.setFraseFavorita(usuarioSerieActualizada.getFraseFavorita());
            }
            if (usuarioSerieActualizada.getCancionFavorita() != null) {
                usuarioSerieExistente.setCancionFavorita(usuarioSerieActualizada.getCancionFavorita());
            }
            if (usuarioSerieActualizada.getCalificacionHistoria() != null) {
                usuarioSerieExistente.setCalificacionHistoria(usuarioSerieActualizada.getCalificacionHistoria());
            }
            if (usuarioSerieActualizada.getCalificacionOst() != null) {
                usuarioSerieExistente.setCalificacionOst(usuarioSerieActualizada.getCalificacionOst());
            }
            if (usuarioSerieActualizada.getCalificacionEscenografia() != null) {
                usuarioSerieExistente.setCalificacionEscenografia(usuarioSerieActualizada.getCalificacionEscenografia());
            }

            usuarioSerieDao.modificarVisualizacion(usuarioSerieExistente);

            return ResponseEntity.ok(usuarioSerieExistente);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(value = "/{usuarioId}/{serieId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> eliminarVisualizacion(@PathVariable long usuarioId, @PathVariable long serieId) {
        usuarioSerieDao.eliminarVisualizacion(usuarioId, serieId);

        return ResponseEntity.noContent().build();
    }
}
