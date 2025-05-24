package com.series.tracker.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "HISTORIAL_USUARIO_SERIE")
public class HistorialUsuarioSerie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario_serie", nullable = false)
    private UsuarioSerie usuarioSerie;

    @Column(name = "plataforma")
    private String plataforma;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "frase_favorita", length = 1000)
    private String fraseFavorita;

    @Column(name = "cancion_favorita")
    private String cancionFavorita;

    @Column(name = "calificacion_historia")
    private Double calificacionHistoria;

    @Column(name = "calificacion_ost")
    private Double calificacionOst;

    @Column(name = "calificacion_escenografia")
    private Double calificacionEscenografia;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion = LocalDateTime.now();
}
