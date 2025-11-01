-- Crear la base de datos
CREATE DATABASE universidad_db;

-- Conectarse a la base de datos universidad_db
\c universidad_db;

-- Tabla de Facultades
CREATE TABLE IF NOT EXISTS facultades (
                                          id BIGINT PRIMARY KEY,
                                          nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(500),
    ubicacion VARCHAR(200),
    decano VARCHAR(100),
    fecha_registro TIMESTAMP NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Tabla de Carreras
CREATE TABLE IF NOT EXISTS carreras (
                                        id BIGINT PRIMARY KEY,
                                        facultad_id BIGINT NOT NULL,
                                        nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(500),
    duracion_semestres INTEGER NOT NULL CHECK (duracion_semestres BETWEEN 6 AND 14),
    titulo_otorgado VARCHAR(100) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_facultad FOREIGN KEY (facultad_id) REFERENCES facultades(id)
    );

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_carreras_facultad_id ON carreras(facultad_id);
CREATE INDEX IF NOT EXISTS idx_carreras_activo ON carreras(activo);
CREATE INDEX IF NOT EXISTS idx_carreras_duracion ON carreras(duracion_semestres);
CREATE INDEX IF NOT EXISTS idx_facultades_activo ON facultades(activo);
CREATE INDEX IF NOT EXISTS idx_facultades_nombre ON facultades(nombre);
CREATE INDEX IF NOT EXISTS idx_carreras_nombre ON carreras(nombre);

-- Datos de ejemplo
INSERT INTO facultades (id, nombre, descripcion, ubicacion, decano, fecha_registro, activo, created_at, updated_at)
VALUES
    (1, 'Facultad de Ingeniería', 'Facultad de ciencias de la ingeniería', 'Edificio A', 'Dr. Juan Pérez', CURRENT_TIMESTAMP, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Facultad de Medicina', 'Facultad de ciencias médicas', 'Edificio B', 'Dra. María García', CURRENT_TIMESTAMP, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO carreras (id, facultad_id, nombre, descripcion, duracion_semestres, titulo_otorgado, fecha_registro, activo, created_at, updated_at)
VALUES
    (1, 1, 'Ingeniería de Sistemas', 'Carrera de ingeniería en sistemas computacionales', 10, 'Ingeniero de Sistemas', CURRENT_TIMESTAMP, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 1, 'Ingeniería Civil', 'Carrera de ingeniería civil', 11, 'Ingeniero Civil', CURRENT_TIMESTAMP, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 2, 'Medicina', 'Carrera de medicina humana', 14, 'Médico Cirujano', CURRENT_TIMESTAMP, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


