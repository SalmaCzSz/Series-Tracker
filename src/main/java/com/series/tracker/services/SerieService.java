package com.series.tracker.services;

import com.series.tracker.enums.Genero;
import com.series.tracker.enums.EstadoSerie;
import com.series.tracker.enums.Plataforma;
import com.series.tracker.dao.SerieDao;
import com.series.tracker.dao.UsuarioSerieDao;
import com.series.tracker.models.Serie;
import com.series.tracker.models.Usuario;
import com.series.tracker.models.UsuarioSerie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Base64;
import java.util.Map;

@Service
public class SerieService {
    private final SerieDao serieDao;
    private final UsuarioSerieDao usuarioSerieDao;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public SerieService(SerieDao serieDao, UsuarioSerieDao usuarioSerieDao) {
        this.serieDao = serieDao;
        this.usuarioSerieDao = usuarioSerieDao;
    }

    @Transactional
    public void guardarSerie(Map<String, Object> datos) {
        Serie serie = new Serie();

        String imgBase64 = (String) datos.get("imgPreview");
        if(imgBase64 != null && !imgBase64.isEmpty()){
            byte[] imgBytes = Base64.getDecoder().decode(imgBase64);
            serie.setImagenPortada(imgBytes);
        } else {
            throw new IllegalArgumentException("Imagen portada es obligatorio");
        }

        serie.setNombre((String) datos.get("nombre"));
        serie.setGenero(Genero.valueOf((String) datos.get("genero")));
        serie.setPais((String) datos.get("pais"));
        serie.setAnioEmision((Integer) datos.get("anioEmision"));
        serie.setEpisodios((Integer) datos.get("noEpisodios"));
        serie.setDuracionMinutos((Integer) datos.get("duracionMinutos"));
        serie.setProtagonistasHistoria((String) datos.get("protagonistas"));

        serieDao.crearSerie(serie);



        Long idUsuario = Long.valueOf(datos.get("idUsuario").toString());
        Usuario usuario = em.find(Usuario.class, idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado para id: " + idUsuario);
        }

        UsuarioSerie usuarioSerie = new UsuarioSerie();
        usuarioSerie.setUsuario(usuario);
        usuarioSerie.setSerie(serie);
        usuarioSerie.setPlataforma(Plataforma.valueOf((String) datos.get("plataforma")));
        usuarioSerie.setEstado(EstadoSerie.valueOf((String) datos.get("estatus")));

        String fechaInicioStr = (String) datos.get("fechaInicio");
        String fechaFinStr = (String) datos.get("fechaFin");

        if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
            usuarioSerie.setFecha_inicio(Date.valueOf(fechaInicioStr));
        }

        if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
            usuarioSerie.setFecha_fin(Date.valueOf(fechaFinStr));
        }

        usuarioSerie.setFraseFavorita((String) datos.get("fraseFavorita"));
        usuarioSerie.setCancionFavorita((String) datos.get("cancionFavorita"));
        usuarioSerie.setCalificacionHistoria(Double.valueOf(datos.get("calificacionHistoria").toString()));
        usuarioSerie.setCalificacionOst(Double.valueOf(datos.get("calificacionOST").toString()));
        usuarioSerie.setCalificacionEscenografia(Double.valueOf(datos.get("calificacionEscenografia").toString()));

        usuarioSerieDao.registrarVisualizacion(usuarioSerie);
    }

    @Transactional
    public void actualizarSerie(Long idSerie, Map<String, Object> datos){
        Serie serieExistente = em.find(Serie.class, idSerie);

        if(serieExistente == null){
            throw new IllegalArgumentException("Serie con ID: " + idSerie + " no encontrada.");
        }


        String imgBase64 = (String) datos.get("imgPreview");

        if(imgBase64 != null && !imgBase64.isEmpty()){
            byte[] imgBytes = Base64.getDecoder().decode(imgBase64);
            serieExistente.setImagenPortada(imgBytes);
        }

        serieExistente.setNombre((String) datos.get("nombre"));
        serieExistente.setGenero(Genero.valueOf((String) datos.get("genero")));
        serieExistente.setPais((String) datos.get("pais"));
        serieExistente.setAnioEmision((Integer) datos.get("anioEmision"));
        serieExistente.setEpisodios((Integer) datos.get("noEpisodios"));
        serieExistente.setDuracionMinutos((Integer) datos.get("duracionMinutos"));
        serieExistente.setProtagonistasHistoria((String) datos.get("protagonistas"));

        serieDao.modificarSerie(serieExistente);


        Long idUsuario = Long.valueOf(datos.get("idUsuario").toString());
        Usuario usuario = em.find(Usuario.class, idUsuario);

        if(usuario == null){
            throw  new IllegalArgumentException("Usuario con ID " + idUsuario + " no encontrado.");
        }


        UsuarioSerie usuarioSerie = usuarioSerieDao.obtenerVisualizacion(idUsuario, idSerie);
        if(usuarioSerie == null){
            throw new IllegalArgumentException("No se encontró relación entre el usuario y la serie.");
        }

        usuarioSerie.setPlataforma(Plataforma.valueOf((String) datos.get("plataforma")));
        usuarioSerie.setEstado(EstadoSerie.valueOf((String) datos.get("estatus")));

        String fechaInicioStr = (String) datos.get("fechaInicio");
        String fechaFinStr = (String) datos.get("fechaFin");

        if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
            usuarioSerie.setFecha_inicio(Date.valueOf(fechaInicioStr));
        } else {
            usuarioSerie.setFecha_inicio(null);
        }

        if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
            usuarioSerie.setFecha_fin(Date.valueOf(fechaFinStr));
        } else {
            usuarioSerie.setFecha_fin(null);
        }

        usuarioSerie.setFraseFavorita((String) datos.get("fraseFavorita"));
        usuarioSerie.setCancionFavorita((String) datos.get("cancionFavorita"));
        usuarioSerie.setCalificacionHistoria(Double.valueOf(datos.get("calificacionHistoria").toString()));
        usuarioSerie.setCalificacionOst(Double.valueOf(datos.get("calificacionOST").toString()));
        usuarioSerie.setCalificacionEscenografia(Double.valueOf(datos.get("calificacionEscenografia").toString()));

        usuarioSerieDao.modificarVisualizacion(usuarioSerie);
    }
}
