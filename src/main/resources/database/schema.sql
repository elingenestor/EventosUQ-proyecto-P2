-- =====================================================
-- ESQUEMA BASE DE DATOS - Eventos UQ
-- =====================================================
-- Este esquema está alineado con las entidades y DAOs del proyecto.
-- Los identificadores son INT AUTO_INCREMENT para que coincidan con
-- el uso de getGeneratedKeys() en los DAO.

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    es_admin BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario)
);

CREATE TABLE IF NOT EXISTS metodo_pago (
    id_metodo_pago INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    titular VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_metodo_pago),
    CONSTRAINT fk_metodo_pago_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recinto (
    id_recinto INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    ciudad VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_recinto)
);

CREATE TABLE IF NOT EXISTS zona (
    id_zona INT NOT NULL AUTO_INCREMENT,
    id_recinto INT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    capacidad INT NOT NULL,
    precio_base DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_zona),
    CONSTRAINT fk_zona_recinto
        FOREIGN KEY (id_recinto) REFERENCES recinto(id_recinto)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS asiento (
    id_asiento INT NOT NULL AUTO_INCREMENT,
    id_zona INT NOT NULL,
    fila VARCHAR(5),
    numero INT NOT NULL,
    estado VARCHAR(20) DEFAULT 'DISPONIBLE',
    PRIMARY KEY (id_asiento),
    CONSTRAINT fk_asiento_zona
        FOREIGN KEY (id_zona) REFERENCES zona(id_zona)
        ON DELETE CASCADE,
    CONSTRAINT uk_asiento_zona_fila_numero UNIQUE (id_zona, fila, numero)
);

CREATE TABLE IF NOT EXISTS evento (
    id_evento INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    descripcion TEXT,
    ciudad VARCHAR(100) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado VARCHAR(20) DEFAULT 'BORRADOR',
    politicas_cancelacion TEXT,
    id_recinto INT NOT NULL,
    PRIMARY KEY (id_evento),
    CONSTRAINT fk_evento_recinto
        FOREIGN KEY (id_recinto) REFERENCES recinto(id_recinto)
);

CREATE TABLE IF NOT EXISTS compra (
    id_compra INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_evento INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'CREADA',
    id_metodo_pago INT DEFAULT NULL,
    PRIMARY KEY (id_compra),
    CONSTRAINT fk_compra_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_compra_evento
        FOREIGN KEY (id_evento) REFERENCES evento(id_evento),
    CONSTRAINT fk_compra_metodo_pago
        FOREIGN KEY (id_metodo_pago) REFERENCES metodo_pago(id_metodo_pago)
);

CREATE TABLE IF NOT EXISTS entrada (
    id_entrada INT NOT NULL AUTO_INCREMENT,
    id_compra INT NOT NULL,
    id_zona INT NOT NULL,
    id_asiento INT DEFAULT NULL,
    precio_final DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'ACTIVA',
    PRIMARY KEY (id_entrada),
    CONSTRAINT fk_entrada_compra
        FOREIGN KEY (id_compra) REFERENCES compra(id_compra)
        ON DELETE CASCADE,
    CONSTRAINT fk_entrada_zona
        FOREIGN KEY (id_zona) REFERENCES zona(id_zona),
    CONSTRAINT fk_entrada_asiento
        FOREIGN KEY (id_asiento) REFERENCES asiento(id_asiento)
);

CREATE TABLE IF NOT EXISTS servicio_adicional (
    id_servicio INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    tipo VARCHAR(30),
    PRIMARY KEY (id_servicio)
);

CREATE TABLE IF NOT EXISTS compra_servicio (
    id_compra INT NOT NULL,
    id_servicio INT NOT NULL,
    PRIMARY KEY (id_compra, id_servicio),
    CONSTRAINT fk_compra_servicio_compra
        FOREIGN KEY (id_compra) REFERENCES compra(id_compra)
        ON DELETE CASCADE,
    CONSTRAINT fk_compra_servicio_servicio
        FOREIGN KEY (id_servicio) REFERENCES servicio_adicional(id_servicio)
);

CREATE TABLE IF NOT EXISTS incidencia (
    id_incidencia INT NOT NULL AUTO_INCREMENT,
    tipo VARCHAR(30) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    entidad_afectada VARCHAR(100),
    id_compra INT DEFAULT NULL,
    id_evento INT DEFAULT NULL,
    PRIMARY KEY (id_incidencia),
    CONSTRAINT fk_incidencia_compra
        FOREIGN KEY (id_compra) REFERENCES compra(id_compra)
        ON DELETE SET NULL,
    CONSTRAINT fk_incidencia_evento
        FOREIGN KEY (id_evento) REFERENCES evento(id_evento)
        ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_evento_fecha ON evento(fecha_hora);
CREATE INDEX IF NOT EXISTS idx_evento_estado ON evento(estado);
CREATE INDEX IF NOT EXISTS idx_evento_ciudad ON evento(ciudad);
CREATE INDEX IF NOT EXISTS idx_compra_usuario ON compra(id_usuario);
CREATE INDEX IF NOT EXISTS idx_compra_estado ON compra(estado);
CREATE INDEX IF NOT EXISTS idx_asiento_estado ON asiento(estado);

