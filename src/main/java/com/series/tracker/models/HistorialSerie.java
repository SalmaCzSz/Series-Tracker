package com.series.tracker.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "HISTORIAL_SERIE")
public class HistorialSerie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_serie", nullable = false)
    private Serie serie;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "genero")
    private String genero;

    @Column(name = "episodios")
    private Integer episodios;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "pais")
    private String pais;

    @Column(name = "anio_emision")
    private Integer anioEmision;

    @Column(name = "protagonistas_historia", length = 500)
    private String protagonistasHistoria;

    @Column(name = "imagen_portada")
    private String imagenPortada;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion = LocalDateTime.now();
}

