-- Crear base de datos
CREATE DATABASE SERIES_TRACKER;
USE SERIES_TRACKER;

-- Tabla USUARIO
CREATE TABLE IF NOT EXISTS USUARIO (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Tabla SERIE
CREATE TABLE IF NOT EXISTS SERIE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    episodios INT NOT NULL,
    duracion_minutos INT NOT NULL,
    pais VARCHAR(100) NOT NULL,
    anio_emision INT NOT NULL,
    protagonistas_historia VARCHAR(500) NOT NULL,
    imagen_portada VARCHAR(255) NOT NULL
);

-- Tabla USUARIO_SERIE
CREATE TABLE IF NOT EXISTS USUARIO_SERIE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_serie BIGINT NOT NULL,
    plataforma VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    frase_favorita VARCHAR(1000) NOT NULL,
    cancion_favorita VARCHAR(255) NOT NULL,
    calificacion_historia DOUBLE NOT NULL,
    calificacion_ost DOUBLE NOT NULL,
    calificacion_escenografia DOUBLE NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id),
    FOREIGN KEY (id_serie) REFERENCES SERIE(id)
);
