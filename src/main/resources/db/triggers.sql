DELIMITER //

CREATE TRIGGER serie_update_historial AFTER UPDATE ON SERIE FOR EACH ROW
BEGIN
    INSERT INTO HISTORIAL_SERIE(id_serie,
                                nombre,
                                genero,
                                episodios,
                                duracion_minutos,
                                pais,
                                anio_emision,
                                protagonistas_historia,
                                imagen_portada,
                                fecha_modificacion)
        VALUES(OLD.id,
               OLD.nombre,
               OLD.genero,
               OLD.episodios,
               OLD.duracion_minutos,
               OLD.pais,
               OLD.anio_emision,
               OLD.protagonistas_historia,
               OLD.imagen_portada,
               NOW());
END;
//

DELIMITER ;



DELIMITER //

CREATE TRIGGER usuario_serie_update_historial AFTER UPDATE ON USUARIO_SERIE FOR EACH ROW
BEGIN
    INSERT INTO HISTORIAL_USUARIO_SERIE(id_usuario_serie,
                                        plataforma,
                                        estado,
                                        fecha_inicio,
                                        fecha_fin,
                                        frase_favorita,
                                        cancion_favorita,
                                        calificacion_historia,
                                        calificacion_ost,
                                        calificacion_escenografia,
                                        fecha_modificacion)
        VALUES(OLD.id,
               OLD.plataforma,
               OLD.estado,
               OLD.fecha_inicio,
               OLD.fecha_fin,
               OLD.frase_favorita,
               OLD.cancion_favorita,
               OLD.calificacion_historia,
               OLD.calificacion_ost,
               OLD.calificacion_escenografia,
               NOW());
END;
//

DELIMITER ;