package com.series.tracker.models;

import com.series.tracker.enums.Genero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@Entity
@Table(name = "SERIE")
@ToString @EqualsAndHashCode
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter
    private long id;

    @Column(name = "nombre")
    @Getter @Setter
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    @Getter @Setter
    private Genero genero;

    @Column(name = "episodios")
    @Getter @Setter
    private int episodios;

    @Column(name = "duracion_minutos")
    @Getter @Setter
    private int duracionMinutos;

    @Column(name = "pais")
    @Getter @Setter
    private String pais;

    @Column(name = "anio_emision")
    @Getter @Setter
    private int anioEmision;

    @Column(name = "protagonistas_historia")
    @Getter @Setter
    private String protagonistasHistoria;

    @Column(name = "imagen_portada")
    @Getter @Setter
    private String imagenPortada;
}

