package com.series.tracker.controllers;

import com.series.tracker.enums.EstadoSerie;
import com.series.tracker.enums.Genero;
import com.series.tracker.enums.Plataforma;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enums/")
public class EnumController {
    @GetMapping("estado")
    public EstadoSerie[] getEstado(){
        return EstadoSerie.values();
    }

    @GetMapping("genero")
    public Genero[] getGenero(){
        return Genero.values();
    }

    @GetMapping("plataforma")
    public Plataforma[] getPlataforma(){
        return Plataforma.values();
    }
}
