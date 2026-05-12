Pensamiento computacional (RF-043)
¿QUÉ SE SOLICITA FINALMENTE?
Desarrollar una plataforma de gestión de eventos y venta de entradas que permita a usuarios finales explorar eventos, seleccionar zonas y asientos, comprar entradas, agregar servicios adicionales, pagar y recibir notificaciones. También debe permitir a un administrador gestionar el catálogo de eventos, recintos, zonas, asientos, compras, incidencias y visualizar métricas de ventas.

¿QUÉ INFORMACIÓN ES RELEVANTE?

Información de usuarios:
id, nombre, email, telefono, métodos de pago.
Información de eventos:
id, nombre, categoría, descripción, ciudad, fecha/hora, estado, políticas de cancelación, recinto asociado.
Información de recintos:
id, nombre, dirección, ciudad, zonas que lo componen.
Información de zonas:
id, nombre, capacidad, precio base, asientos que contiene.
Información de asientos:
id, fila, numero, estado (disponible, reservado, vendido, bloqueado).
Información de compras:
id, usuario, evento, fecha, total, estado de comprar, entradas, servicios adicionales, método de pago.
Información de servicios adicionales:
id, nombre, descripción, precio, tipo.
Información de incidencias:
id, tipo, descripción, fecha, entidad afectada.

¿CÓMO SE AGRUPA LA INFORMACIÓN RELEVANTE?
La información se agrupa en las siguientes clases principales:

Modelo (Entidades):
├── Usuario
├── Administrador (hereda de Usuario)
├── Evento
├── Recinto
├── Zona
├── Asiento
├── Compra
├── Entrada
├── ServicioAdicional
├── Incidencia
└── MetodoPago

Enums (Tipos fijos):
├── EstadoEvento
├── EstadoCompra
├── EstadoAsiento
├── EstadoEntrada
├── CategoriaEvento
├── TipoIncidencia
├── MetodoPagoSimulado
└── TipoServicio

Capas de acceso a datos (DAO):
├── Interfaces (contratos)
└── Implementaciones (JDBC)

Capas de servicio (lógica de negocio):
├── Interfaces
└── Implementaciones

Patrones de diseño:
├── Creacionales: Singleton, Builder, Factory
├── Estructurales: Decorator, Adapter, Facade
└── Comportamiento: Strategy, State, Observer

Controladores JavaFX (MVC):
└── Cada vista tiene su controlador

¿QUÉ FUNCIONALIDADES SE SOLICITAN?

Usuario:
Registro y Login
Gestionar perfil
Explorar eventos con filtros
Ver detalle de evento
Seleccionar entradas por zonas/asiento
Crear/modificar/cancelar compra
Pagar compra
Agregar servicios adicionales
Consultar historial de compras
Descargar reporte de compras
Administrador:
Gestionar usuarios(CRUD)
Gestionar eventos(CRUD + publicar/pausar/cancelar)
Gestionar recintos y zonas
Gestionar asientos y disponibilidad
Gestionar compras( consultar, cancelar, reembolsar)
Registrar incidencias
Visualizar metricas con graficos JavaFX
Generar reportes (CSV/PDF)



¿CÓMO SE DISTRIBUYEN LAS FUNCIONALIDADES?
Capa de Presentación (JavaFX)
├── Pantallas de usuario (login, eventos, detalle, carrito, historial)
├── Pantallas de administrador (dashboard, gestión, reportes)
└── Controladores (manejan eventos de UI)

         ↓ (llaman a)

Capa de Servicio (Lógica de Negocio)
├── UsuarioService
├── EventoService
├── CompraService
├── AdminService
└── ReporteService

         ↓ (usan)

Capa de Acceso a Datos (DAO)
├── UsuarioDAO, EventoDAO, CompraDAO, etc.
└── Implementaciones con JDBC

         ↓ (conectan a)

Base de Datos (MySQL)

Patrones de diseño aplicados:
├── Singleton: ConexionBD, GestorReportes
├── Builder: Construcción de Evento y Compra
├── Factory: Creación de DAOs e Incidencias
├── Decorator: Servicios adicionales en compra
├── Adapter: Conversión para reportes
├── Facade: Simplificar proceso de compra
├── Strategy: Cálculo de precios y cancelaciones
├── State: Estados de compra
└── Observer: Notificaciones de cambios

¿QUÉ DEBO HACER PARA PROBAR FUNCIONALIDADES?
Pruebas unitarias (JUnit):
Probar cada método de servicio con datos controlados.
Verificar que las reglas de negocio se cumplan
Pruebas de integración:
Probar la comunicación entre capas ( controlador -> servicio -> DAO -> BD)
Pruebas funcionales manuales:
Crear usuario e iniciar sesión
Buscar eventos por filtros
Seleccionar asientos y agregar servicios adicionales
Crear compra y pagar
Ver el historial de compras y descargar reporte
Iniciar sesion como admin
Crear un nuevo evento con recinto y zonas
Publicar evento y verificar que aparece para los usuarios
Cancelar evento y verificar notificaciones
Generar reporte de ventas CSV/PDF
Datos de pruebas iniciales:
3 usuarios (2 normales, 1 admin)
2 recintos con zonas y asientos
5 eventos en diferentes estados
10 compras en diferentes estados
4 servicios adicionales

¿QUE PUEDO REUTILIZAR?

Librerías externas:
MySQL Connector/J: Conexión a base de datos.
JavaFX: interfaz gráfica.
Apache POI / OpenCSV: Generación de reportes CSV.
Apache PDFBox: Generación de reportes PDF.
JUnit: pruebas unitarias.
Patrones de diseño ya implementados en otros contextos:
Singleton para recursos compartidos.
Factory para creación de objetos.
Strategy para algoritmos intercambiables.

¿COMO PRUEBO/ESCRIBO LA SOLUCIÓN EN JAVA?
Estructura de paquetes:
com.uiquindio.eventosUQ/
├── MainApp.java
├── model/
├── enums/
├── dao/interfaces/ e impl/
├── service/interfaces/ e impl/
├── controller/
├── patterns/
│   ├── creational/
│   ├── structural/
│   └── behavioral/
└── util/

Ejemplo de prueba unitaria (JUnit):
@Test
public void testRegistrarUsuario() {
    UsuarioService service = new UsuarioServiceImpl(mockDAO);
    Usuario usuario = new Usuario(0, "Test", "test@email.com", "123", "pass");
    service.registrar(usuario);
    verify(mockDAO).save(any(Usuario.class));
}
Ejemplo de prueba funcional:
Ejecutar MainApp.java
Crear usuario -> verificar en BD
Login -> verificar redirección a pantalla principal

