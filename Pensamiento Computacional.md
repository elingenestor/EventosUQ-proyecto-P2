# Pensamiento Computacional (RF-043)

---

## 1. ¿Qué se solicita finalmente?

Se solicita desarrollar una plataforma de software orientada a objetos en Java, con interfaz gráfica JavaFX, que permita a los usuarios explorar eventos, seleccionar zonas y asientos, comprar entradas, agregar servicios adicionales, pagar y consultar el estado de sus compras.
También debe permitir a los administradores gestionar el catálogo de eventos, recintos, zonas, asientos, compras e incidencias, además de visualizar métricas de ventas.
El sistema debe generar reportes exportables en CSV y PDF, aplicar al menos nueve patrones de diseño y seguir principios SOLID. Además, el proyecto debe gestionarse con Git.

---

## 2. ¿Qué información es relevante?

La información relevante del sistema se concentra en las entidades principales del dominio:

- **Usuario:** idUsuario, nombre, correo, teléfono, métodos de pago y compras asociadas.
- **Evento:** idEvento, nombre, categoría, descripción, ciudad, fecha/hora, estado, políticas de cancelación y reembolso, recinto asociado y observadores.
- **Recinto:** idRecinto, nombre, dirección, ciudad y zonas.
- **Zona:** idZona, nombre, capacidad, precio base y asientos.
- **Asiento:** idAsiento, fila, número y estado.
- **Compra:** idCompra, usuario, evento, fecha de creación, total, estado, ítems y servicios adicionales.
- **Entrada:** idEntrada, zona, asiento, precio final y estado.
- **Pago:** idPago, monto, método, fecha, estado y estrategia de pago.
- **Tarifa:** tipo, porcentaje o monto, según corresponda a servicio, descuento o IVA.
- **Incidencia:** tipo, descripción, fecha y entidad afectada.
- **ServicioAdicional:** VIP, seguro, merchandising y parqueadero.

---

## 3. ¿Cómo se agrupa la información relevante?

La información puede agruparse en varias capas:

### Modelo / Dominio
Aquí van las entidades que representan el negocio:
Usuario, Evento, Recinto, Zona, Asiento, Compra, Entrada, Pago, Tarifa, Incidencia y ServicioAdicional. Estas clases concentran los atributos y el comportamiento básico del sistema.

---

### Enumeraciones
Para asegurar tipos seguros y evitar errores, se agrupan los estados y tipos en enums como:
EstadoEvento, EstadoCompra, EstadoAsiento, EstadoEntrada, TipoServicio y TipoPago.

---

### Repositorios
Se incluyen interfaces de acceso a datos y sus implementaciones concretas, para separar el contrato de la forma de guardar o consultar información.

---

### Servicios
La lógica de negocio se concentra en servicios como EventoService, CompraService, PagoService y ReporteService, que coordinan las reglas entre entidades.

---

### UI
Los controladores JavaFX se encargan de la interacción con el usuario y el administrador.

---

### Patrones de apoyo
Singleton, Builder, Factory, Strategy, Decorator, Observer, Adapter, Facade y Template Method, lo que encaja bien con las necesidades del dominio.

---

## 4. ¿Qué funcionalidades se solicitan?

### Funcionalidades del usuario
El usuario debe poder registrarse e iniciar sesión, gestionar su perfil, explorar eventos con filtros, consultar detalles de un evento, seleccionar zona o asientos, crear/modificar/cancelar una compra antes del pago, pagar con distintos métodos, ver el estado de compra e historial, agregar servicios adicionales y descargar comprobantes o reportes.

---

### Funcionalidades del administrador
El administrador debe poder gestionar usuarios, eventos, recintos, zonas y asientos, cambiar el estado de los eventos, administrar compras, registrar y consultar incidencias, y visualizar métricas como ventas por periodo, ocupación por zona, ingresos por servicios, tasa de cancelación y top eventos.

---

## 5. ¿Cómo se distribuyen las funcionalidades?
La distribución más clara es la siguiente:

### Capa de presentación
Contiene las vistas JavaFX y sus controladores para usuario y administrador.

---

### Capa de negocio
Contiene los servicios que coordinan las operaciones del sistema: eventos, compras, pagos, reportes e incidencias.

---

### Capa de acceso a datos
Contiene los repositorios o interfaces de acceso a datos y sus implementaciones, encargadas de persistir o recuperar la información.

---

### Clases de soporte
Aquí se ubican las clases de patrones: gestores Singleton, builders, factories, estrategias de pago, decoradores de servicios, observadores para notificaciones, adaptadores de reportes y la fachada para la compra.

---

## 6. ¿Qué debo hacer para probar las funcionalidades?
Se pueden preparar datos iniciales en memoria con al menos 3 usuarios, 5 eventos, 2 recintos, zonas con asientos numerados y varias compras en distintos estados. Luego se deben probar flujos completos, como:
- El usuario inicia sesión, busca un evento, selecciona zona/asientos, agrega servicios adicionales, paga y recibe confirmación.
- El administrador cambia el estado de un evento de borrador a publicado, luego a pausado y finalmente a cancelado.
- Se valida que el sistema registre incidencias cuando ocurre una doble compra de asiento.
- Se comprueba que las estrategias de pago calculan correctamente el resultado.
- Se verifica que los decoradores suman servicios de forma acumulable.
- Se exportan reportes CSV y PDF y se revisa que el contenido sea correcto.
- Se revisan las métricas del panel administrador.

---

## 7. ¿Qué puedo reutilizar?
Se puede reutilizar lo siguiente:
- Librerías Java para exportar reportes y generar gráficos.
- Patrones que sirvan como base para comportamiento dinámico o notificaciones.
- Builders reutilizables para eventos y compras.
- Una interfaz de exportación común para CSV y PDF.
- Enums de estados para validaciones y transiciones.

---

## 8. ¿Cómo pruebo/escribo la solución en Java?
La solución puede escribirse en Java 17+ con JavaFX 17+, usando una estructura modular (divide un sistema en partes autónomas y organizadas) con separación clara entre model, service, repository, factory, strategy, decorator, observer, report y ui.
Posible estructura:
- model: entidades del dominio.
- model.enums: estados y tipos.
- repository: contratos de acceso a datos.
- repository.impl: implementación concreta.
- service: lógica de negocio.
- factory, strategy, decorator, observer, report: clases de soporte y patrones.
- ui: controladores JavaFX.

---

### Enfoque de implementación
La idea es construir primero el dominio, luego la lógica de negocio y después la interfaz gráfica. Así se mantiene una separación clara entre datos, reglas y presentación.