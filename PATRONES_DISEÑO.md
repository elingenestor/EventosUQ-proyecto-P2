PATRONES  ELEGIDOS RF-049, RF-050, RF-051
PATRONES CREACIONALES (RF-049)
Singleton (obligatorio)
Requisitos que resuelve: RF-045 (Implementación técnica), RF-046 (Generador de reportes), RF-047 (DIP - Inversión de dependencias).
Problema que resuelve: La plataforma necesita una única instancia global para manejar la conexión a la base de datos y la generación de reportes. Si se crean múltiples instancias, se desperdician recursos (múltiples conexiones abiertas) y podría generar conflictos al escribir archivos de reportes simultáneamente.
Porque Singleton y no otra opción:
El patrón Factory crearía nuevas instancias cada vez, lo que es indeseado para conexiones con bases de datos.
El patrón Monostate (variables estáticas) no protege contra la creación de múltiples instancias.
Singleton garantiza una única instancia global con acceso controlado y lazy initialization..
	Aplicación al proyecto:
ConexionBD: Gestiona la conexión única a MySQL.
GestorReportes: Centraliza la generación de CSV/PDF.
SessionManager: Mantiene al usuario autenticado.
Builder
Requisitos que resuelve: RF-013 (Gestionar eventos), RF-034(Crear compras), RF-035(Modificar compras).
Problema que resuelve: Un Evento tiene muchos atributos opcionales (descripción, políticas de cancelación, etc) y una Compra puede tener múltiples entradas y servicios. Usar constructores con 10+ parámetros es propenso a errores y poco legible. Además se necesitan diferentes combinaciones de atributos según el contexto (ej: evento borrador vs evento publicado).
Porque Builder y no otra opción:
Factory method es útil cuando hay una jerarquía de productos, pero no resuelve el problema de muchos parámetros.
Abstract Factory es para familias de productos relacionados.
Builder permite construir objetos complejos paso a paso, es legible y permite objetos inmutables.
	Aplicación en el proyecto:
EventoBuilder: Permite construir eventos con fluent API: new EventoBuilder(). setNombre (“Rock FEst”).setCiudad(“Armenia”).setFecha(...).build()
CompraBuider: Similar para compras agregando entradas y servicios gradualmente.
Factory Method /Abstract Factory
Requisitos que resuelve: RF-044 (Diagrama de clases), RF-045 (Estructura del proyecto), RF-047 (DIP)
Problema que resuelve: La aplicación necesita crear diferentes tipos de objetos DAO (UsuarioDAO, EventoDAO, CompraDAO) según la capa de persistencia. Si se usara new directamente, el código quedaría acoplado a implementaciones concretas, violando el principio de inversión de dependencias (DIP). Además, las incidencias pueden ser diferentes tipos y su creación puede tener lógica específica.
Porque Factory y no otra opción:
Singleton no aplica porque se necesitan múltiples objetos.
Builder es para objetos complejos con muchos atributos, no para seleccionar implementaciones.
Factory Method encapsula la lógica de creación y permite cambiar fácilmente entre implementaciones (ej: cambiar de MySQL a PostgreSQL).
	Aplicación en el proyecto:
DAOFactory: devuelve la implementación concreta de cada DAO (ej: getUsuarioDAO() retorna UsurioDAOImpl).
IncidenciaFactory: crea incidencias con datos predefinidos según el tipo.

PATRONES ESTRUCTURALES (RF-050)
Decorator (obligatorio)
Requisitos que resuelve: RF-009 (Agregar servicios adicionales a la compra), RF-034 (Crear compras)
Problema que resuelve: Una compra puede tener múltiples servicios adicionales (VIP, seguro, merchandising, parqueadero). Si se usara herencia, se necesitan subclases para cada combinación: CompraConVIP, CompraConVIPySeguro, CompraConVIPySeguroyMerchandising, lo que explota conminatoriamente. Además, los servicios pueden agregarse o quitarse dinámicamente
Porque Decorator y no otra opción:
Strategy cambia algoritmos completos, no agrega responsabilidades.
Composite es para estructuras jerárquicas (parte-todo).
Decorator permite añadir responsabilidades dinámicamente sin modificar la clase original, siguiendo el principio Open/Closed.
Aplicación en el proyecto:
ComponenteCompra: Interfaz con getCosto() y getDescripcion()
CompraBase: implementación concreta
VIPDecorator, SeguroDecorator, MerchadisingDecorator: envuelven una compra y añaden su costo y descripción.
Adapter
Requisitos que resuelve: RF-046 (Generador de Reportes Operativos - exportar CSV/PDF)
Problema que resuelve: La lógica interna de la plataforma maneja objetos java (List<Compra>, List <Evento>) pero las librerías externas para generar CSV (Apache POI, OpensCSV) y PDF (PDFBox) esperan formatos específicos (arrays de string, fila de tablas). Sin un adaptador, el código de generación de reportes quedaría acoplado a formatos específicos, dificultando cambios futuros.
Porque Adapter y no otra opción:
Facade simplifica una interfaz compleja, pero no convierte formatos.
Bridge separa abstracción de implementación para variaciones independientes.
Adapter convierte una interfaz en otra que el cliente espera, permitiendo reutilizar librerías externas sin modificar el código interno.
	Aplicación en el proyecto:
ReporteAdapter: métodos cómo convertirCompraAFilaCSV(Compra compra) que devuelve String[] para el generador CSV.
Facade
Requisitos que resuelve: RF-007 (Pagar la compra), RF-038 (Generar entradas asociadas a una compra pagada)
Problema que resuelve: El proceso de compra es complejo: verificar disponibilidad de asientos, crear la compra, reservar asientos, calcular total con servicios, procesar pago, actualizar estados, generar entradas y enviar notificaciones. El controlador JavaFX no debería conocer toda esa complejidad. Sin una fachada, el controlador tendría que coordinar múltiples servicios, violando el Principio de Responsabilidad Única (SRP).
Porque Facade y no otra opción:
Mediator COordina interacciones entre objetos, pero es mas adecuado para sistemas con muchos objetos que se comunican bidireccionalmente.
Proxy controla el acceso, no simplifica interfaces complejas.
Facade proporciona una interfaz unificada y simplificada para un subsistema complejo, ideal para puntos de entrada como “realizar compras”.
	Aplicación en el proyecto:
ProcesadorCompraFacade: método realizarCompra(usuario, evento, asientos, servicios, metodoPago) que internamente orquesta todas las llamadas a servicios y DAOs

PATRONES DE COMPORTAMIENTO (RF-051)
Strategy (Obligatorio)
Requisitos que resuelve: RF-025 (Consultar disponibilidad por zonas y asientos - precios variables), RF-036 (Cancelar una compra según reglas/políticas)

Problema que resuelve: El cálculo del precio de una entrada puede variar según múltiples factores (preventa, descuento estudiante, promociones especiales). Las políticas de cancelación también varían según el evento (cancelación gratuita antes de 48h, reembolso parcial, sin reembolso). Si se usaran condicionales (if/else), el código sería difícil de mantener y extender, violando OCP.

Por qué Strategy y no otra opción:

State cambia el comportamiento según el estado interno del objeto, no según algoritmos intercambiables.

Template Method define el esqueleto de un algoritmo pero deja que subclases implementen pasos, lo que requiere herencia.

Strategy permite intercambiar algoritmos en tiempo de ejecución sin modificar el cliente, perfecto para políticas de precio y cancelación.


Aplicación en el proyecto:

CalculadoraPrecio: interfaz con calcularPrecio()

PrecioBaseStrategy, PrecioPreventaStrategy, PrecioEstudianteStrategy

PoliticaCancelacion: interfaz con aplicarCancelacion()

CancelacionAntesDePagoStrategy, CancelacionConReembolsoStrategy
State
Requisitos que resuelve: RF-008 (Visualizar estado de la compra), RF-036 (Cancelar una compra según reglas/políticas), RF-040 (Anular entradas por cancelación/reembolso)

Problema que resuelve: Una Compra puede estar en varios estados: CREADA, PAGADA, CONFIRMADA, CANCELADA, REEMBOLSADA, INCIDENCIA. El comportamiento de métodos como pagar(), cancelar(), reembolsar() depende completamente del estado actual. Si se usaran condicionales (switch o if), el código sería propenso a errores (olvidar un estado, comportamientos inconsistentes) y difícil de extender con nuevos estados.

Por qué State y no otra opción:

Strategy intercambia algoritmos completos, no depende del estado interno.

Machine State (enum con switch) es más propenso a errores y menos OOP.

State encapsula el comportamiento específico de cada estado en su propia clase, permitiendo añadir nuevos estados sin modificar los existentes (OCP) y eliminando condicionales complejos.

Aplicación en el proyecto:

EstadoCompra: interfaz con métodos pagar(), cancelar(), confirmar(), reembolsar()

EstadoCreada, EstadoPagada, EstadoConfirmada, EstadoCancelada, EstadoReembolsada: cada una implementa los métodos según corresponda. Ej: EstadoPagada permite cancelar con reembolso y confirmar; EstadoCancelada no permite ninguna acción posterior.
Observer
Requisitos que resuelve: Contexto inicial (recibir notificaciones sobre cambios de estado del evento y de sus compras), RF-017 (Registrar incidencias y cambios de estado), RF-041 (Registrar incidencias y asociarlas)

Problema que resuelve: Cuando un Evento cambia de estado (ej: se cancela), todos los usuarios que compraron entradas para ese evento deben ser notificados. Cuando una Compra cambia de estado (ej: se confirma), el usuario debe recibir una notificación. Sin un patrón, la clase Evento tendría que conocer a todos los Usuario que compraron, acoplando las clases y violando SRP y DIP.

Por qué Observer y no otra opción:

Mediator centraliza la comunicación entre objetos, pero es más pesado cuando solo se necesita notificación unidireccional.

Publish-Subscribe es similar pero normalmente asíncrono y con canales (más complejo).

Observer establece una dependencia uno-a-muchos donde el sujeto no necesita conocer los detalles de los observadores, solo que implementan la interfaz Observer. Es el patrón estándar para sistemas de notificación y eventos.

Aplicación en el proyecto:

Observable: interfaz con agregarObserver(), removerObserver(), notificarObservers()

Observer: interfaz con actualizar(Observable, Object)

Evento y Compra implementan Observable

NotificacionService implementa Observer y envía correos/notificaciones

Cuando un evento se cancela, llama a notificarObservers(), y NotificacionService envía notificaciones a todos los usuarios que compraron entradas para ese evento


