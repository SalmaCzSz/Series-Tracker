package com.series.tracker.controllers;

import com.series.tracker.dao.SerieDao;
import com.series.tracker.models.Serie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/series")
public class SerieController {
    @Autowired
    private SerieDao serieDao;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Serie>> obtenerSeries() {
        List<Serie> series = serieDao.obtenerSeries();

        return ResponseEntity.ok(series);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Serie> crearSerie(@RequestBody Serie serie) {
        serieDao.crearSerie(serie);

        return ResponseEntity.status(HttpStatus.CREATED).body(serie);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Serie> obtenerSeriePorId(@PathVariable long id) {
        Serie serie = serieDao.obtenerSeriePorId(id);

        if (serie != null) {
            return ResponseEntity.ok(serie);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Serie> modificarSerie(@PathVariable long id, @RequestBody Serie serieActualizada) {
        Serie serieExistente = serieDao.obtenerSeriePorId(id);

        if (serieExistente != null) {
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

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> eliminarSerie(@PathVariable long id) {
        serieDao.eliminarSerie(id);

        return ResponseEntity.noContent().build();
    }
}
