CREATE TABLE IF NOT EXISTS usuario (
    id_usuario VARCHAR(100) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    es_admin BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TABLA: metodo_pago
-- =====================================================
CREATE TABLE IF NOT EXISTS metodo_pago (
                                           id_metodo_pago VARCHAR(100) KEY AUTO_INCREMENT,
                                           id_usuario VARCHAR(100) NOT NULL,
                                           tipo VARCHAR(20) NOT NULL, -- TARJETA_CREDITO, TARJETA_DEBITO, etc.
                                           numero VARCHAR(20) NOT NULL, -- últimos 4 dígitos o máscara
                                           titular VARCHAR(100) NOT NULL,
                                           FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

-- =====================================================
-- TABLA: recinto
-- =====================================================
CREATE TABLE IF NOT EXISTS recinto (
                                       id_recinto VARCHAR(100) PRIMARY KEY AUTO_INCREMENT,
                                       nombre VARCHAR(100) NOT NULL,
                                       direccion VARCHAR(200),
                                       ciudad VARCHAR(100) NOT NULL
);

-- =====================================================
-- TABLA: zona
-- =====================================================
CREATE TABLE IF NOT EXISTS zona (
                                    id_zona VARCHAR(100) PRIMARY KEY AUTO_INCREMENT,
                                    id_recinto VARCHAR(100) NOT NULL,
                                    nombre VARCHAR(50) NOT NULL,
                                    capacidad INT NOT NULL,
                                    precio_base DECIMAL(10,2) NOT NULL,
                                    FOREIGN KEY (id_recinto) REFERENCES recinto(id_recinto) ON DELETE CASCADE
);

-- =====================================================
-- TABLA: asiento (solo para zonas numeradas)
-- =====================================================
CREATE TABLE IF NOT EXISTS asiento (
                                       id_asiento VARCHAR(100) PRIMARY KEY AUTO_INCREMENT,
                                       id_zona VARCHAR(100) NOT NULL,
                                       fila VARCHAR(5),
                                       numero INT NOT NULL,
                                       estado VARCHAR(20) DEFAULT 'DISPONIBLE', -- DISPONIBLE, RESERVADO, VENDIDO, BLOQUEADO
                                       FOREIGN KEY (id_zona) REFERENCES zona(id_zona) ON DELETE CASCADE,
                                       UNIQUE KEY unique_asiento (id_zona, fila, numero)
);

-- =====================================================
-- TABLA: evento
-- =====================================================
CREATE TABLE IF NOT EXISTS evento (
                                      id_evento VARCHAR(100) PRIMARY KEY AUTO_INCREMENT,
                                      nombre VARCHAR(100) NOT NULL,
                                      categoria VARCHAR(50) NOT NULL, -- CONCIERTO, TEATRO, etc.
                                      descripcion TEXT,
                                      ciudad VARCHAR(100) NOT NULL,
                                      fecha_hora DATETIME NOT NULL,
                                      estado VARCHAR(20) DEFAULT 'BORRADOR', -- BORRADOR, PUBLICADO, PAUSADO, CANCELADO, FINALIZADO
                                      politicas_cancelacion TEXT,
                                      id_recinto INT NOT NULL,
                                      FOREIGN KEY (id_recinto) REFERENCES recinto(id_recinto)
);

-- =====================================================
-- TABLA: compra
-- =====================================================
CREATE TABLE IF NOT EXISTS compra (
                                      id_compra VARCHAR(100) PRIMARY KEY AUTO_INCREMENT,
                                      id_usuario VARCHAR(100) NOT NULL,
                                      id_evento VARCHAR(100) NOT NULL,
                                      fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      total DECIMAL(10,2) NOT NULL,
                                      estado VARCHAR(20) DEFAULT 'CREADA', -- CREADA, PAGADA, CONFIRMADA, CANCELADA, REEMBOLSADA, INCIDENCIA
                                      id_metodo_pago INT,
                                      FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
                                      FOREIGN KEY (id_evento) REFERENCES evento(id_evento),
                                      FOREIGN KEY (id_metodo_pago) REFERENCES metodo_pago(id_metodo_pago)
);

-- =====================================================
-- TABLA: entrada (ticket)
-- =====================================================
CREATE TABLE IF NOT EXISTS entrada (
                                       id_entrada VARCHAR(100) PRIMARY KEY AUTO_INCREMENT,
                                       id_compra VARCHAR(100) NOT NULL,
                                       id_zona VARCHAR(100) NOT NULL,
                                       id_asiento VARCHAR(100), -- puede ser NULL si zona no es numerada
                                       precio_final DECIMAL(10,2) NOT NULL,
                                       estado VARCHAR(20) DEFAULT 'ACTIVA', -- ACTIVA, USADA, ANULADA
                                       FOREIGN KEY (id_compra) REFERENCES compra(id_compra) ON DELETE CASCADE,
                                       FOREIGN KEY (id_zona) REFERENCES zona(id_zona),
                                       FOREIGN KEY (id_asiento) REFERENCES asiento(id_asiento)
);

-- =====================================================
-- TABLA: servicio_adicional (catálogo)
-- =====================================================
CREATE TABLE IF NOT EXISTS servicio_adicional (
                                                  id_servicio VARCHAR(100) PRIMARY KEY AUTO_INCREMENT,
                                                  nombre VARCHAR(50) NOT NULL,
                                                  descripcion TEXT,
                                                  precio DECIMAL(10,2) NOT NULL,
                                                  tipo VARCHAR(30) -- VIP, SEGURO, MERCHANDISING, etc.
);

-- =====================================================
-- TABLA: compra_servicio (relación muchos a muchos)
-- =====================================================
CREATE TABLE IF NOT EXISTS compra_servicio (
                                               id_compra VARCHAR (100) NOT NULL,
                                               id_servicio VARCHAR(100) NOT NULL,
                                               PRIMARY KEY (id_compra, id_servicio),
                                               FOREIGN KEY (id_compra) REFERENCES compra(id_compra) ON DELETE CASCADE,
                                               FOREIGN KEY (id_servicio) REFERENCES servicio_adicional(id_servicio)
);

-- =====================================================
-- TABLA: incidencia
-- =====================================================
CREATE TABLE IF NOT EXISTS incidencia (
                                          id_incidencia VARCHAR(100) PRIMARY KEY AUTO_INCREMENT,
                                          tipo VARCHAR(30) NOT NULL, -- DOBLE_COMPRA, ERROR_PAGO, etc.
                                          descripcion TEXT NOT NULL,
                                          fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          entidad_afectada VARCHAR(100), -- "Compra:123" o "Evento:45"
                                          id_compra INT,
                                          id_evento INT,
                                          FOREIGN KEY (id_compra) REFERENCES compra(id_compra) ON DELETE SET NULL,
                                          FOREIGN KEY (id_evento) REFERENCES evento(id_evento) ON DELETE SET NULL
);

-- =====================================================
-- ÍNDICES para optimizar consultas frecuentes
-- =====================================================
CREATE INDEX idx_evento_fecha ON evento(fecha_hora);
CREATE INDEX idx_evento_estado ON evento(estado);
CREATE INDEX idx_evento_ciudad ON evento(ciudad);
CREATE INDEX idx_compra_usuario ON compra(id_usuario);
CREATE INDEX idx_compra_estado ON compra(estado);
CREATE INDEX idx_asiento_estado ON asiento(estado);