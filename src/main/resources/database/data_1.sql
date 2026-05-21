-- =====================================================
-- DATOS DE EJEMPLO - Eventos UQ
-- =====================================================

INSERT INTO usuario (nombre_completo, email, telefono, password, es_admin) VALUES
('Juan Pérez', 'juan@email.com', '3001234567', 'password123', FALSE),
('María García', 'maria@email.com', '3007654321', 'password123', FALSE),
('Admin Sistema', 'admin@eventos.com', '3000000000', 'admin123', TRUE);

INSERT INTO metodo_pago (id_usuario, tipo, numero, titular) VALUES
(1, 'TARJETA_CREDITO', '****1234', 'JUAN PEREZ'),
(1, 'PSE', 'CUENTA123', 'JUAN PEREZ'),
(2, 'TARJETA_DEBITO', '****5678', 'MARIA GARCIA');

INSERT INTO recinto (nombre, direccion, ciudad) VALUES
('Estadio Nacional', 'Calle 50 # 20-30', 'Bogotá'),
('Teatro Colón', 'Calle 10 # 5-20', 'Bogotá'),
('Centro de Convenciones', 'Carrera 40 # 15-10', 'Medellín');

INSERT INTO zona (id_recinto, nombre, capacidad, precio_base) VALUES
(1, 'VIP', 500, 150000.00),
(1, 'Preferencial', 2000, 80000.00),
(1, 'General', 5000, 40000.00),
(2, 'Platea', 300, 120000.00),
(2, 'Balcón', 200, 80000.00),
(3, 'Sala Principal', 800, 100000.00);

INSERT INTO asiento (id_zona, fila, numero, estado) VALUES
(1, 'A', 1, 'DISPONIBLE'),
(1, 'A', 2, 'DISPONIBLE'),
(1, 'A', 3, 'DISPONIBLE'),
(1, 'B', 1, 'DISPONIBLE'),
(1, 'B', 2, 'DISPONIBLE');

INSERT INTO evento (nombre, categoria, descripcion, ciudad, fecha_hora, estado, politicas_cancelacion, id_recinto) VALUES
('Rock Fest 2024', 'CONCIERTO', 'El mejor festival de rock del año', 'Bogotá', '2024-12-15 20:00:00', 'PUBLICADO', 'Cancelación hasta 48h antes con reembolso del 80%', 1),
('Hamlet - Teatro', 'TEATRO', 'Obra clásica de Shakespeare', 'Bogotá', '2024-11-20 19:30:00', 'PUBLICADO', 'No se aceptan cancelaciones', 2),
('Tech Conference 2024', 'CONFERENCIA', 'Innovación y tecnología', 'Medellín', '2024-10-10 09:00:00', 'PUBLICADO', 'Cancelación con reembolso total hasta 7 días antes', 3);

INSERT INTO servicio_adicional (nombre, descripcion, precio, tipo) VALUES
('Acceso VIP', 'Área exclusiva con bebidas y snacks', 50000.00, 'VIP'),
('Seguro de Cancelación', 'Reembolso total en caso de cancelación', 15000.00, 'SEGURO'),
('Camiseta Oficial', 'Merchandising del evento', 60000.00, 'MERCHANDISING'),
('Parqueadero', 'Parqueadero preferencial', 20000.00, 'PARQUEADERO');

INSERT INTO compra (id_usuario, id_evento, total, estado, id_metodo_pago) VALUES
(1, 1, 230000.00, 'CONFIRMADA', 1),
(2, 2, 120000.00, 'PAGADA', 3);

INSERT INTO entrada (id_compra, id_zona, id_asiento, precio_final, estado) VALUES
(1, 1, 1, 150000.00, 'ACTIVA'),
(1, 1, 2, 150000.00, 'ACTIVA'),
(2, 4, NULL, 120000.00, 'ACTIVA');

INSERT INTO compra_servicio (id_compra, id_servicio) VALUES
(1, 1),
(1, 2);
