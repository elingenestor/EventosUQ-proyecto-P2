-- =========================================================================================
-- NOTA ARQUITECTÓNICA DE PERSISTENCIA - PROYECTO EVENTOS UQ
-- =========================================================================================
-- MOTIVACIÓN DEL CAMBIO:
-- El sistema migró de una base de datos volátil en memoria RAM (jdbc:h2:mem:) a una base
-- de datos embebida persistente en archivo físico local (jdbc:h2:./database/).
--
-- EL PROBLEMA DETECTADO:
-- Al volverse permanente el archivo, un script de datos tradicional con comandos 'INSERT'
-- plano fallaría en el segundo arranque del programa debido a violaciones de integridad
-- por Claves Primarias y Únicas duplicadas (ej: intentar re-insertar el email del Admin).
--
-- INGENIERÍA APLICADA Y SOLUCIONES IMPLEMENTADAS EN ESTE SCRIPT:
-- 1. CONTROL DE USUARIOS (MERGE INTO): Se implementó la instrucción 'MERGE' nativa de H2,
--    la cual actúa de forma inteligente: si encuentra que el email ya existe en la tabla,
--    actualiza los campos; si no existe, lo inserta por primera vez. Esto blinda el Login.
--
-- 2. BLINDAJE DE TABLAS MAESTRAS (INSERT... SELECT... WHERE NOT EXISTS): Para las tablas de
--    Recintos, Zonas y Eventos, se estructuró un filtro de negación lógica. La base de datos
--    evalúa en milisegundos si ya existe el registro (ej: 'Rock Fest 2024') antes de permitir
--    su inserción, evitando duplicados en la interfaz del Administrador.
--
-- 3. DINAMISMO EN ASIENTOS (FOREIGN KEY DECOUPLING): Se removieron los IDs estáticos rígidos
--    ('1', '2') en las compras y entradas de prueba, dejándolos como 'NULL'. Esto se debe a
--    que la pantalla de administración ahora genera miles de asientos matemáticos automáticos
--    en lote (cascada SQL), lo que altera los autoincrementales reales y provocaría rupturas
--    de integridad por claves foráneas inexistentes.
-- =========================================================================================


-- =========================================================================================
-- 1. SECCIÓN: USUARIOS (SISTEMA DE AUTENTICACIÓN)
-- CAMBIO: Se realiza el MERGE inyectando todas las columnas obligatorias juntas.
-- MOTIVO: Evita la restricción de campos NOT NULL que provocaba el crash en H2.
-- =========================================================================================
INSERT INTO usuario (email, nombre_completo, telefono, password, es_administrador)
VALUES ('juan@email.com', 'Juan Pérez', '3001234567', 'password123', FALSE)
ON DUPLICATE KEY UPDATE
                     nombre_completo = VALUES(nombre_completo),
                     telefono = VALUES(telefono),
                     password = VALUES(password),
                     es_administrador = VALUES(es_administrador);

INSERT INTO usuario (email, nombre_completo,telefono, password, es_administrador)
VALUES('andres@email.com','Andres Rodriguez', '31008881798', 'andres1234', FALSE)
ON DUPLICATE KEY UPDATE
                     nombre_completo = VALUES(nombre_completo),
                     telefono = VALUES(telefono),
                     password = VALUES(password),
                     es_administrador = VALUES(es_administrador);

INSERT INTO usuario (email, nombre_completo,telefono, password, es_administrador)
VALUES ('admin@eventos.com', 'Admin Sistema', ' 3000000000', 'admin123',TRUE)
ON DUPLICATE KEY UPDATE
                     nombre_completo = VALUES(nombre_completo),
                     telefono = VALUES(telefono),
                     password = VALUES(password),
                     es_administrador = VALUES(es_administrador);


-- =============================================================================
-- 2. INSERCIÓN DE MÉTODOS DE PAGO DE PRUEBA
-- =============================================================================
INSERT INTO metodo_pago (id_usuario, tipo, numero, titular)
SELECT 1, 'TARJETA_CREDITO', '****1234', 'JUAN PEREZ' WHERE NOT EXISTS (SELECT 1 FROM metodo_pago WHERE numero = '****1234');

INSERT INTO metodo_pago (id_usuario, tipo, numero, titular)
SELECT 1, 'PSE', 'CUENTA123', 'JUAN PEREZ' WHERE NOT EXISTS (SELECT 1 FROM metodo_pago WHERE numero = 'CUENTA123');

INSERT INTO metodo_pago (id_usuario, tipo, numero, titular)
SELECT 2, 'TARJETA_DEBITO', '****5678', 'MARIA GARCIA' WHERE NOT EXISTS (SELECT 1 FROM metodo_pago WHERE numero = '****5678');

-- =============================================================================
-- 3. INSERCIÓN SEGURA DE RECINTOS (VERIFICA POR NOMBRE)
-- =============================================================================
INSERT INTO recinto (nombre, direccion, ciudad)
SELECT 'Estadio Nacional', 'Calle 50 # 20-30', 'Bogotá' WHERE NOT EXISTS (SELECT 1 FROM recinto WHERE nombre = 'Estadio Nacional');

INSERT INTO recinto (nombre, direccion, ciudad)
SELECT 'Teatro Colón', 'Calle 10 # 5-20', 'Bogotá' WHERE NOT EXISTS (SELECT 1 FROM recinto WHERE nombre = 'Teatro Colón');

INSERT INTO recinto (nombre, direccion, ciudad)
SELECT 'Centro de Convenciones', 'Carrera 40 # 15-10', 'Medellín' WHERE NOT EXISTS (SELECT 1 FROM recinto WHERE nombre = 'Centro de Convenciones');

-- =============================================================================
-- 4. INSERCIÓN SEGURA DE ZONAS (O LOCALIDADES)
-- =============================================================================
INSERT INTO zona (id_recinto, nombre, capacidad, precio_base)
SELECT 1, 'VIP', 500, 150000.00 WHERE NOT EXISTS (SELECT 1 FROM zona WHERE id_recinto = 1 AND nombre = 'VIP');

INSERT INTO zona (id_recinto, nombre, capacidad, precio_base)
SELECT 1, 'Preferencial', 2000, 80000.00 WHERE NOT EXISTS (SELECT 1 FROM zona WHERE id_recinto = 1 AND nombre = 'Preferencial');

INSERT INTO zona (id_recinto, nombre, capacidad, precio_base)
SELECT 1, 'General', 5000, 40000.00 WHERE NOT EXISTS (SELECT 1 FROM zona WHERE id_recinto = 1 AND nombre = 'General');

INSERT INTO zona (id_recinto, nombre, capacidad, precio_base)
SELECT 2, 'Platea', 300, 120000.00 WHERE NOT EXISTS (SELECT 1 FROM zona WHERE id_recinto = 2 AND nombre = 'Platea');

INSERT INTO zona (id_recinto, nombre, capacidad, precio_base)
SELECT 2, 'Balcón', 200, 80000.00 WHERE NOT EXISTS (SELECT 1 FROM zona WHERE id_recinto = 2 AND nombre = 'Balcón');

INSERT INTO zona (id_recinto, nombre, capacidad, precio_base)
SELECT 3, 'Sala Principal', 800, 100000.00 WHERE NOT EXISTS (SELECT 1 FROM zona WHERE id_recinto = 3 AND nombre = 'Sala Principal');

-- =============================================================================
-- 5. INSERCIÓN DE ASIENTOS BASE (PARA LAS ZONAS DE PRUEBA INITIAL)
-- =============================================================================
INSERT INTO asiento (id_zona, fila, numero, estado)
SELECT 1, 'A', 1, 'DISPONIBLE' WHERE NOT EXISTS (SELECT 1 FROM asiento WHERE id_zona = 1 AND fila = 'A' AND numero = 1);

INSERT INTO asiento (id_zona, fila, numero, estado)
SELECT 1, 'A', 2, 'DISPONIBLE' WHERE NOT EXISTS (SELECT 1 FROM asiento WHERE id_zona = 1 AND fila = 'A' AND numero = 2);

INSERT INTO asiento (id_zona, fila, numero, estado)
SELECT 1, 'A', 3, 'DISPONIBLE' WHERE NOT EXISTS (SELECT 1 FROM asiento WHERE id_zona = 1 AND fila = 'A' AND numero = 3);

INSERT INTO asiento (id_zona, fila, numero, estado)
SELECT 1, 'B', 1, 'DISPONIBLE' WHERE NOT EXISTS (SELECT 1 FROM asiento WHERE id_zona = 1 AND fila = 'B' AND numero = 1);

INSERT INTO asiento (id_zona, fila, numero, estado)
SELECT 1, 'B', 2, 'DISPONIBLE' WHERE NOT EXISTS (SELECT 1 FROM asiento WHERE id_zona = 1 AND fila = 'B' AND numero = 2);

-- =============================================================================
-- 6. INSERCIÓN SEGURA DE EVENTOS (MAREADOS CON EL RECINTO)
-- =============================================================================
INSERT INTO evento (nombre, categoria, descripcion, ciudad, fecha_hora, estado, politicas_cancelacion, id_recinto)
SELECT 'Rock Fest 2024', 'CONCIERTO', 'El mejor festival de rock del año', 'Bogotá', '2024-12-15 20:00:00', 'PUBLICADO', 'Cancelación hasta 48h antes con reembolso del 80%', 1
WHERE NOT EXISTS (SELECT 1 FROM evento WHERE nombre = 'Rock Fest 2024');

INSERT INTO evento (nombre, categoria, descripcion, ciudad, fecha_hora, estado, politicas_cancelacion, id_recinto)
SELECT 'Hamlet - Teatro', 'TEATRO', 'Obra clásica de Shakespeare', 'Bogotá', '2024-11-20 19:30:00', 'PUBLICADO', 'No se aceptan cancelaciones', 2
WHERE NOT EXISTS (SELECT 1 FROM evento WHERE nombre = 'Hamlet - Teatro');

INSERT INTO evento (nombre, categoria, descripcion, ciudad, fecha_hora, estado, politicas_cancelacion, id_recinto)
SELECT 'Tech Conference 2024', 'CONFERENCIA', 'Innovación y tecnología', 'Medellín', '2024-10-10 09:00:00', 'PUBLICADO', 'Cancelación con reembolso total hasta 7 días antes', 3
WHERE NOT EXISTS (SELECT 1 FROM evento WHERE nombre = 'Tech Conference 2024');

-- =============================================================================
-- 7. SERVICIOS ADICIONALES
-- =============================================================================
INSERT INTO servicio_adicional (nombre, descripcion, precio, tipo)
SELECT 'Acceso VIP', 'Área exclusiva con bebidas y snacks', 50000.00, 'VIP' WHERE NOT EXISTS (SELECT 1 FROM servicio_adicional WHERE nombre = 'Acceso VIP');

INSERT INTO servicio_adicional (nombre, descripcion, precio, tipo)
SELECT 'Seguro de Cancelación', 'Reembolso total en caso de cancelación', 15000.00, 'SEGURO' WHERE NOT EXISTS (SELECT 1 FROM servicio_adicional WHERE nombre = 'Seguro de Cancelación');

INSERT INTO servicio_adicional (nombre, descripcion, precio, tipo)
SELECT 'Camiseta Oficial', 'Merchandising del evento', 60000.00, 'MERCHANDISING' WHERE NOT EXISTS (SELECT 1 FROM servicio_adicional WHERE nombre = 'Camiseta Oficial');

INSERT INTO servicio_adicional (nombre, descripcion, precio, tipo)
SELECT 'Parqueadero', 'Parqueadero preferencial', 20000.00, 'PARQUEADERO' WHERE NOT EXISTS (SELECT 1 FROM servicio_adicional WHERE nombre = 'Parqueadero');

-- =============================================================================
-- 8. COMPRAS HISTÓRICAS DE PRUEBA (PARA LOS REPORTES FINALES)
-- =============================================================================
INSERT INTO compra (id_usuario, id_evento, total, estado, id_metodo_pago)
SELECT 1, 1, 230000.00, 'CONFIRMADA', 1 WHERE NOT EXISTS (SELECT 1 FROM compra WHERE id_usuario = 1 AND id_evento = 1);

INSERT INTO compra (id_usuario, id_evento, total, estado, id_metodo_pago)
SELECT 2, 2, 120000.00, 'PAGADA', 3 WHERE NOT EXISTS (SELECT 1 FROM compra WHERE id_usuario = 2 AND id_evento = 2);

-- =============================================================================
-- 9. ENTRADAS DE PRUEBA (CON ID_ASIENTO EN NULL PARA EVITAR CRASH POR AUTOINCREMENTAL)
-- =============================================================================
INSERT INTO entrada (id_compra, id_zona, id_asiento, precio_final, estado)
SELECT 1, 1, NULL, 150000.00, 'ACTIVA' WHERE NOT EXISTS (SELECT 1 FROM entrada WHERE id_compra = 1 AND precio_final = 150000.00);

INSERT INTO entrada (id_compra, id_zona, id_asiento, precio_final, estado)
SELECT 2, 4, NULL, 120000.00, 'ACTIVA' WHERE NOT EXISTS (SELECT 1 FROM entrada WHERE id_compra = 2 AND id_zona = 4);

-- =============================================================================
-- 10. RELACIÓN DE COMPRAS Y SERVICIOS EXTRA
-- =============================================================================
INSERT INTO compra_servicio (id_compra, id_servicio)
SELECT 1, 1 WHERE NOT EXISTS (SELECT 1 FROM compra_servicio WHERE id_compra = 1 AND id_servicio = 1);

INSERT INTO compra_servicio (id_compra, id_servicio)
SELECT 1, 2 WHERE NOT EXISTS (SELECT 1 FROM compra_servicio WHERE id_compra = 1 AND id_servicio = 2);
