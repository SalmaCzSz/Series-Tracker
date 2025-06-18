package com.series.tracker.models;

import com.series.tracker.enums.EstadoSerie;
import com.series.tracker.enums.Plataforma;
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
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.sql.Date;

@Entity
@Table(name = "USUARIO_SERIE")
@ToString @EqualsAndHashCode
public class UsuarioSerie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @Getter @Setter
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_serie", nullable = false)
    @Getter @Setter
    private Serie serie;

    @Column(name = "plataforma")
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private Plataforma plataforma;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private EstadoSerie estado;

    @Getter @Setter
    @Column(name = "fecha_inicio")
    private Date fecha_inicio;

    @Getter @Setter
    @Column(name = "fecha_fin")
    private Date fecha_fin;

    @Getter @Setter
    @Column(name = "frase_favorita")
    private String fraseFavorita;

    @Getter @Setter
    @Column(name = "cancion_favorita")
    private String cancionFavorita;

    @Column(name = "calificacion_historia")
    @Getter @Setter
    private Double calificacionHistoria;

    @Column(name = "calificacion_ost")
    @Getter @Setter
    private Double calificacionOst;

    @Column(name = "calificacion_escenografia")
    @Getter @Setter
    private Double calificacionEscenografia;

    @Column(name = "activo")
    @Getter @Setter
    private boolean activo;
}
