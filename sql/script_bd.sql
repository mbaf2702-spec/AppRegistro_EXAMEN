-- ============================================================
-- Sistema de Gestion de Reclutamiento y Seleccion de Personal
-- NexoTalento - Script del modelo fisico
-- Version actualizada: incluye postulacion publica de aspirantes
-- ============================================================

DROP DATABASE IF EXISTS db_reclutamiento;
CREATE DATABASE db_reclutamiento
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE db_reclutamiento;

-- ------------------------------------------------------------
-- Tabla: usuarios
-- Usuarios del sistema (empleados de RRHH que usan la aplicacion).
-- rol: 'RRHH' = administrador con acceso total.
--      'Empleado' = acceso limitado (solo actualizar entrevistas).
-- Contrasena y respuesta de seguridad se guardan HASHEADAS
-- (SHA-256), nunca en texto plano.
-- ------------------------------------------------------------
CREATE TABLE usuarios (
    id_usuario          INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario      VARCHAR(50)  NOT NULL UNIQUE,
    contrasena_hash     VARCHAR(255) NOT NULL,
    pregunta_seguridad  VARCHAR(150) NOT NULL,
    respuesta_hash      VARCHAR(255) NOT NULL,
    rol                 VARCHAR(20)  NOT NULL DEFAULT 'RRHH',
    fecha_creacion      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- Tabla: vacantes
-- Cargos/plazas que la empresa necesita cubrir.
-- ------------------------------------------------------------
CREATE TABLE vacantes (
    id_vacante        INT AUTO_INCREMENT PRIMARY KEY,
    cargo             VARCHAR(100)   NOT NULL,
    departamento      VARCHAR(100)   NOT NULL,
    salario_ofertado  DECIMAL(10,2)  NOT NULL,
    fecha_apertura    DATE           NOT NULL,
    estado            ENUM('Abierta','Cerrada') NOT NULL DEFAULT 'Abierta'
);

-- ------------------------------------------------------------
-- Tabla: aspirantes
-- Candidatos que aplican a una vacante. Pueden registrarse:
--   a) un empleado de RRHH los registra manualmente (id_usuario = su ID), o
--   b) el propio candidato se autopostula desde el portal publico,
--      sin iniciar sesion (id_usuario queda en NULL, por eso esa
--      columna admite valores nulos).
-- FK -> vacantes: a que vacante aplica
-- FK -> usuarios: que empleado lo registro (NULL si fue autopostulacion)
-- ------------------------------------------------------------
CREATE TABLE aspirantes (
    id_aspirante      INT AUTO_INCREMENT PRIMARY KEY,
    cedula            VARCHAR(10)   NOT NULL UNIQUE,
    nombres           VARCHAR(100)  NOT NULL,
    apellidos         VARCHAR(100)  NOT NULL,
    telefono          VARCHAR(15)   NOT NULL,
    correo            VARCHAR(100)  NOT NULL,
    fecha_registro    DATE          NOT NULL DEFAULT (CURRENT_DATE),
    estado            ENUM('En proceso','Contratado','Rechazado') NOT NULL DEFAULT 'En proceso',
    id_vacante        INT           NOT NULL,
    id_usuario        INT           NULL,
    CONSTRAINT fk_aspirante_vacante
        FOREIGN KEY (id_vacante) REFERENCES vacantes(id_vacante)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_aspirante_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ------------------------------------------------------------
-- Tabla: entrevistas
-- Evaluaciones realizadas a cada aspirante.
-- puntaje: escala Likert de 1 a 5 (1=Muy deficiente, 5=Excelente).
-- FK -> aspirantes: a quien se entrevisto
-- FK -> usuarios: quien registro/actualizo la entrevista
-- ------------------------------------------------------------
CREATE TABLE entrevistas (
    id_entrevista     INT AUTO_INCREMENT PRIMARY KEY,
    id_aspirante      INT           NOT NULL,
    fecha_entrevista  DATE          NOT NULL,
    entrevistador     VARCHAR(100)  NOT NULL,
    puntaje           DECIMAL(4,2)  NOT NULL,
    resultado         ENUM('Aprobado','No aprobado','Pendiente') NOT NULL DEFAULT 'Pendiente',
    observaciones     VARCHAR(300),
    id_usuario        INT           NOT NULL,
    CONSTRAINT fk_entrevista_aspirante
        FOREIGN KEY (id_aspirante) REFERENCES aspirantes(id_aspirante)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_entrevista_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_puntaje CHECK (puntaje BETWEEN 1 AND 5)
);

-- ------------------------------------------------------------
-- Usuario administrador de prueba
-- Usuario: admin   Contrasena: Admin2026*
-- Pregunta: ¿Cual es el nombre de tu primera mascota?
-- Respuesta: Firulais
-- Rol: RRHH (acceso total al sistema)
-- ------------------------------------------------------------
INSERT INTO usuarios (nombre_usuario, contrasena_hash, pregunta_seguridad, respuesta_hash, rol)
VALUES (
  'admin',
  SHA2('Admin2026*', 256),
  '¿Cual es el nombre de tu primera mascota?',
  SHA2('Firulais', 256),
  'RRHH'
);

-- Datos de ejemplo para probar la aplicacion de inmediato
INSERT INTO vacantes (cargo, departamento, salario_ofertado, fecha_apertura, estado)
VALUES
 ('Auxiliar Contable', 'Contabilidad', 550.00, '2026-08-01', 'Abierta'),
 ('Desarrollador Junior', 'Sistemas', 800.00, '2026-08-15', 'Abierta');