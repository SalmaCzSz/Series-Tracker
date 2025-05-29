package com.series.tracker.controllers;

import com.series.tracker.dao.SerieDao;
import com.series.tracker.models.Serie;
import com.series.tracker.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/series")
public class SerieController {
    @Autowired
    private SerieDao serieDao;

    private String getAuthenticatedUsername() {
        return SecurityUtil.getAuthenticatedUsername();
    }

    private ResponseEntity<Map<String, String>> unauthorizedResponse() {
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Usuario no autorizado. Por favor inicia sesión.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @GetMapping
    public ResponseEntity<?> obtenerSeries() {
        String username = getAuthenticatedUsername();

        if(username == null){
            return unauthorizedResponse();
        }

        List<Serie> series = serieDao.obtenerSeries();

        return ResponseEntity.ok(series);
    }

    @PostMapping
    public ResponseEntity<?> crearSerie(@RequestBody Serie serie) {
        String username = getAuthenticatedUsername();

        if(username == null){
            return unauthorizedResponse();
        }

        ResponseEntity<Map<String,String>> error = validarSerie(serie);

        if(error != null){
            return error;
        }

        serieDao.crearSerie(serie);

        return ResponseEntity.status(HttpStatus.CREATED).body(serie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerSeriePorId(@PathVariable long id) {
        String username = getAuthenticatedUsername();

        if(username == null){
            return unauthorizedResponse();
        }

        Serie serie = serieDao.obtenerSeriePorId(id);

        if (serie != null) {
            return ResponseEntity.ok(serie);
        }

        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Serie no encontrada");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarSerie(@PathVariable long id, @RequestBody Serie serieActualizada) {
        String username = getAuthenticatedUsername();

        if(username == null){
            return unauthorizedResponse();
        }

        Serie serieExistente = serieDao.obtenerSeriePorId(id);

        if (serieExistente != null) {
            ResponseEntity<Map<String,String>> error = validarSerie(serieActualizada);

            if(error != null){
                return error;
            }

            serieExistente.setNombre(serieActualizada.getNombre());
            serieExistente.setGenero(serieActualizada.getGenero());
            serieExistente.setEpisodios(serieActualizada.getEpisodios());
            serieExistente.setDuracionMinutos(serieActualizada.getDuracionMinutos());
            serieExistente.setPais(serieActualizada.getPais());
            serieExistente.setAnioEmision(serieActualizada.getAnioEmision());
            serieExistente.setProtagonistasHistoria(serieActualizada.getProtagonistasHistoria());
            serieExistente.setImagenPortada(serieActualizada.getImagenPortada());

            serieDao.modificarSerie(serieExistente);

            return ResponseEntity.ok(serieExistente);
        }

        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Serie no encontrada");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarSerie(@PathVariable long id) {
        String username = getAuthenticatedUsername();

        if(username == null){
            return unauthorizedResponse();
        }

        Serie serie = serieDao.obtenerSeriePorId(id);

        if(serie == null){
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Serie no encontrada");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        serieDao.eliminarSerie(id);

        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> validarSerie(Serie serie) {
        if (serie.getNombre() == null || serie.getNombre().isBlank() ||
            serie.getGenero() == null ||
            serie.getEpisodios() <= 0 ||
            serie.getDuracionMinutos() <= 0 ||
            serie.getPais() == null || serie.getPais().isBlank() ||
            serie.getAnioEmision() <= 0 ||
            serie.getProtagonistasHistoria() == null || serie.getProtagonistasHistoria().isBlank() ||
            serie.getImagenPortada() == null || serie.getImagenPortada().isBlank()) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Por favor, ingrese todos los datos");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        return null;
    }
}
