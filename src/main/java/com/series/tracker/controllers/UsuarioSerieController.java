package com.series.tracker.controllers;

import com.series.tracker.dao.UsuarioSerieDao;
import com.series.tracker.utils.SecurityUtil;
import com.series.tracker.models.UsuarioSerie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios/series")
public class UsuarioSerieController {
    @Autowired
    private UsuarioSerieDao usuarioSerieDao;

    private String getAuthenticatedUsername() {
        return SecurityUtil.getAuthenticatedUsername();
    }

    private ResponseEntity<Map<String, String>> unauthorizedResponse() {
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Usuario no autorizado. Por favor inicia sesión.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @PostMapping
    public ResponseEntity<?> registrarVisualizacion(@RequestBody UsuarioSerie usuarioSerie) {
        String username = getAuthenticatedUsername();

        if (username == null) {
            return unauthorizedResponse();
        }

        ResponseEntity<Map<String, String>> error = validarUsuarioSerie(usuarioSerie);

        if (error != null){
            return error;
        }

        usuarioSerieDao.registrarVisualizacion(usuarioSerie);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSerie);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> obtenerSeriesPorUsuarioId(@PathVariable long usuarioId) {
        String username = getAuthenticatedUsername();

        if (username == null) {
            return unauthorizedResponse();
        }

        List<UsuarioSerie> series = usuarioSerieDao.obtenerSeriesPorUsuarioId(usuarioId);

        if (!series.isEmpty()) {
            return ResponseEntity.ok(series);
        }

        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Series no encontradas");

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @GetMapping("/{usuarioId}/{serieId}")
    public ResponseEntity<?> obtenerVisualizacion(@PathVariable long usuarioId, @PathVariable long serieId) {
        String username = getAuthenticatedUsername();

        if(username == null){
            return unauthorizedResponse();
        }

        UsuarioSerie usuarioSerie = usuarioSerieDao.obtenerVisualizacion(usuarioId, serieId);

        if (usuarioSerie != null) {
            return ResponseEntity.ok(usuarioSerie);
        }

        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Serie no encontrada");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @PutMapping("/{usuarioId}/{serieId}")
    public ResponseEntity<?> modificarVisualizacion(@PathVariable long usuarioId,
                                                               @PathVariable long serieId,
                                                               @RequestBody UsuarioSerie usuarioSerieActualizada) {
        String username = getAuthenticatedUsername();

        if(username == null){
            return unauthorizedResponse();
        }

        UsuarioSerie usuarioSerieExistente = usuarioSerieDao.obtenerVisualizacion(usuarioId, serieId);

        if (usuarioSerieExistente != null) {
            ResponseEntity<Map<String,String>> error = validarUsuarioSerie(usuarioSerieActualizada);

            if(error != null){
                return error;
            }

            usuarioSerieExistente.setPlataforma(usuarioSerieActualizada.getPlataforma());
            usuarioSerieExistente.setEstado(usuarioSerieActualizada.getEstado());
            usuarioSerieExistente.setFecha_inicio(usuarioSerieActualizada.getFecha_inicio());
            usuarioSerieExistente.setFecha_fin(usuarioSerieActualizada.getFecha_fin());
            usuarioSerieExistente.setFraseFavorita(usuarioSerieActualizada.getFraseFavorita());
            usuarioSerieExistente.setCancionFavorita(usuarioSerieActualizada.getCancionFavorita());
            usuarioSerieExistente.setCalificacionHistoria(usuarioSerieActualizada.getCalificacionHistoria());
            usuarioSerieExistente.setCalificacionOst(usuarioSerieActualizada.getCalificacionOst());
            usuarioSerieExistente.setCalificacionEscenografia(usuarioSerieActualizada.getCalificacionEscenografia());

            usuarioSerieDao.modificarVisualizacion(usuarioSerieExistente);

            return ResponseEntity.ok(usuarioSerieExistente);
        }

        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Serie no encontrada");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @DeleteMapping("/{usuarioId}/{serieId}")
    public ResponseEntity<?> eliminarVisualizacion(@PathVariable long usuarioId, @PathVariable long serieId) {
        String username = getAuthenticatedUsername();

        if(username == null){
            return unauthorizedResponse();
        }

        UsuarioSerie usuarioSerie = usuarioSerieDao.obtenerVisualizacion(usuarioId, serieId);

        if(usuarioSerie == null){
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Serie no encontrada");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        usuarioSerieDao.eliminarVisualizacion(usuarioId, serieId);

        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> validarUsuarioSerie(UsuarioSerie usuarioSerie) {
        if (usuarioSerie.getPlataforma() == null ||
            usuarioSerie.getEstado() == null ||
            usuarioSerie.getFecha_inicio() == null ||
            usuarioSerie.getFecha_fin() == null ||
            usuarioSerie.getFraseFavorita() == null || usuarioSerie.getFraseFavorita().isBlank() ||
            usuarioSerie.getCancionFavorita() == null || usuarioSerie.getCancionFavorita().isBlank() ||
            usuarioSerie.getCalificacionHistoria() <= 0 ||
            usuarioSerie.getCalificacionOst() <= 0 ||
            usuarioSerie.getCalificacionEscenografia() <= 0) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Por favor, ingrese todos los datos");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        return null;
    }
}
