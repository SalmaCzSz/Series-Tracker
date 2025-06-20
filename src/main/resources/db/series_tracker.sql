-- Crear base de datos
CREATE DATABASE SERIES_TRACKER;
USE SERIES_TRACKER;

-- Tabla USUARIO
CREATE TABLE IF NOT EXISTS USUARIO (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
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
    imagen_portada LONGBLOB NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla USUARIO_SERIE
CREATE TABLE IF NOT EXISTS USUARIO_SERIE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_serie BIGINT NOT NULL,
    plataforma VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    frase_favorita VARCHAR(1000) DEFAULT '',
    cancion_favorita VARCHAR(255) DEFAULT '',
    calificacion_historia DOUBLE DEFAULT 0.0,
    calificacion_ost DOUBLE DEFAULT 0.0,
    calificacion_escenografia DOUBLE DEFAULT 0.0,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id) ON DELETE CASCADE,
    FOREIGN KEY (id_serie) REFERENCES SERIE(id) ON DELETE CASCADE
);

-- Tabla HISTORIAL_USUARIO_SERIE
CREATE TABLE IF NOT EXISTS HISTORIAL_USUARIO_SERIE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario_serie BIGINT NOT NULL,
    plataforma VARCHAR(100),
    estado VARCHAR(50),
    fecha_inicio DATE,
    fecha_fin DATE,
    frase_favorita VARCHAR(1000),
    cancion_favorita VARCHAR(255),
    calificacion_historia DOUBLE,
    calificacion_ost DOUBLE,
    calificacion_escenografia DOUBLE,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario_serie) REFERENCES USUARIO_SERIE(id)
);

-- Tabla HISTORIAL_SERIE
CREATE TABLE IF NOT EXISTS HISTORIAL_SERIE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_serie BIGINT NOT NULL,
    nombre VARCHAR(200),
    genero VARCHAR(50),
    episodios INT,
    duracion_minutos INT,
    pais VARCHAR(100),
    anio_emision INT,
    protagonistas_historia VARCHAR(500),
    imagen_portada LONGBLOB,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_serie) REFERENCES SERIE(id)
);
