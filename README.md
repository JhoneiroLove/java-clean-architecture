# Sistema de Gestión Universitaria

## Descripción

Sistema de gestión universitaria desarrollado con Spring Boot 3.2 y Java 21, implementando los principios de Clean Architecture y Domain-Driven Design. El sistema permite la administración de facultades y carreras académicas, proporcionando una API REST completa para operaciones CRUD.

## Tabla de Contenidos

- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Ejecución](#ejecución)
- [API Endpoints](#api-endpoints)
- [Base de Datos](#base-de-datos)
- [Pruebas](#pruebas)
- [Documentación](#documentación)

---

## Arquitectura

El proyecto implementa **Clean Architecture** (Arquitectura Limpia) siguiendo los principios SOLID, organizando el código en capas claramente definidas con responsabilidades específicas.

### Capas de la Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│                   (REST Controllers)                         │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                   Application Layer                          │
│            (Use Cases & Application Services)                │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                     Domain Layer                             │
│         (Business Logic, Entities, Value Objects)            │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                 Infrastructure Layer                         │
│        (Frameworks, Database, External Services)             │
└─────────────────────────────────────────────────────────────┘
```

### Principios Aplicados

- **Separation of Concerns**: Cada capa tiene una responsabilidad única y bien definida
- **Dependency Inversion**: Las dependencias apuntan hacia el dominio
- **SOLID Principles**:
    - Single Responsibility
    - Open/Closed
    - Liskov Substitution
    - Interface Segregation
    - Dependency Inversion
- **Hexagonal Architecture**: Puertos y adaptadores para abstraer dependencias externas

---

## Tecnologías

### Core

- **Java 21**: Lenguaje de programación
- **Spring Boot 3.2.0**: Framework principal
- **Spring Data JPA**: Capa de persistencia
- **Hibernate**: ORM (Object-Relational Mapping)
- **PostgreSQL 17**: Base de datos relacional

### Documentación

- **Swagger/OpenAPI 3**: Documentación interactiva de la API
- **SpringDoc OpenAPI**: Integración de Swagger con Spring Boot

### Observabilidad

- **Spring Boot Actuator**: Monitoreo y métricas
- **Micrometer**: Métricas de aplicación
- **Prometheus**: Exportación de métricas

### Herramientas

- **Maven**: Gestión de dependencias y construcción
- **Jackson**: Serialización/deserialización JSON
- **Bean Validation**: Validación de datos

---

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/university/cleanarchitecture/
│   │   ├── application/                    # Capa de Aplicación
│   │   │   ├── dto/
│   │   │   │   ├── command/               # DTOs para comandos (CREATE, UPDATE)
│   │   │   │   ├── query/                 # DTOs para consultas (GET)
│   │   │   │   └── response/              # DTOs para respuestas
│   │   │   ├── mapper/                    # Mappers entre capas
│   │   │   ├── port/
│   │   │   │   ├── in/                    # Puertos de entrada (Use Cases)
│   │   │   │   └── out/                   # Puertos de salida (Repositorios)
│   │   │   └── service/                   # Servicios de aplicación
│   │   │
│   │   ├── domain/                        # Capa de Dominio
│   │   │   ├── exception/                 # Excepciones de dominio
│   │   │   ├── model/                     # Entidades y Value Objects
│   │   │   │   └── valueobjects/         # Objetos de valor
│   │   │   ├── repository/                # Interfaces de repositorio (dominio)
│   │   │   └── service/                   # Servicios de dominio
│   │   │
│   │   └── infrastructure/                # Capa de Infraestructura
│   │       ├── adapter/
│   │       │   ├── in/
│   │       │   │   └── web/              # Controladores REST
│   │       │   └── out/
│   │       │       └── persistence/       # Adaptadores de persistencia
│   │       │           ├── adapter/       # Implementaciones de repositorio
│   │       │           ├── jpa/
│   │       │           │   ├── entity/    # Entidades JPA
│   │       │           │   └── repository/# Repositorios Spring Data
│   │       │           └── mapper/        # Mappers JPA
│   │       └── configuration/             # Configuración de Spring
│   │
│   └── resources/
│       └── application.properties         # Configuración de la aplicación
│
└── test/                                  # Tests unitarios e integración
```

---

## Requisitos Previos

- **Java Development Kit (JDK) 21** o superior
- **Maven 3.8+** para gestión de dependencias
- **PostgreSQL 17** instalado y en ejecución
- **Git** para control de versiones

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/cleanarchitecture.git
cd cleanarchitecture
```

### 2. Configurar PostgreSQL

Crear la base de datos:

```bash
psql -U postgres
```

```sql
CREATE DATABASE universidad_db;
\q
```

Ejecutar el script de inicialización:

```bash
psql -U postgres -d universidad_db -f init-database-fixed.sql
```

### 3. Instalar dependencias

```bash
./mvnw clean install
```

---

## Configuración

### Archivo `application.properties`

Ubicación: `src/main/resources/application.properties`

```properties
# Configuración de la aplicación
spring.application.name=cleanarchitecture

# Configuración de PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/universidad_db
spring.datasource.username=postgres
spring.datasource.password=tu_password
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración de JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

# Configuración del servidor
server.port=8080

# Configuración de Swagger/OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
```

### Variables de Entorno (Opcional)

Para producción, se recomienda usar variables de entorno:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/universidad_db
export DB_USERNAME=postgres
export DB_PASSWORD=tu_password_seguro
```

---

## Ejecución

### Modo desarrollo

```bash
./mvnw spring-boot:run
```

### Generar JAR ejecutable

```bash
./mvnw clean package
java -jar target/cleanarchitecture-0.0.1-SNAPSHOT.jar
```

### Verificar que la aplicación está corriendo

```bash
curl http://localhost:8080/actuator/health
```

Respuesta esperada:
```json
{
  "status": "UP"
}
```

---

## API Endpoints

### Facultades

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/facultades` | Listar todas las facultades |
| GET | `/api/v1/facultades/{id}` | Obtener facultad por ID |
| GET | `/api/v1/facultades/buscar?nombre={nombre}` | Buscar facultad por nombre |
| POST | `/api/v1/facultades` | Crear nueva facultad |
| PUT | `/api/v1/facultades/{id}` | Actualizar facultad |
| PUT | `/api/v1/facultades/{id}/decano` | Cambiar decano de facultad |
| PUT | `/api/v1/facultades/{id}/activar` | Activar facultad |
| PUT | `/api/v1/facultades/{id}/desactivar` | Desactivar facultad |

### Carreras

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/carreras/{id}` | Obtener carrera por ID |
| GET | `/api/v1/carreras/facultad/{facultadId}` | Listar carreras por facultad |
| GET | `/api/v1/carreras/duracion/{semestres}` | Buscar carreras por duración |
| POST | `/api/v1/carreras` | Crear nueva carrera |
| PUT | `/api/v1/carreras/{id}` | Actualizar carrera |
| PUT | `/api/v1/carreras/{id}/activar` | Activar carrera |
| PUT | `/api/v1/carreras/{id}/desactivar` | Desactivar carrera |

### Ejemplos de Uso

#### Crear una Facultad

```bash
curl -X POST http://localhost:8080/api/v1/facultades \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Facultad de Ingeniería",
    "descripcion": "Facultad de ciencias de la ingeniería",
    "ubicacion": "Edificio A",
    "decano": "Dr. Juan Pérez"
  }'
```

#### Listar Facultades

```bash
curl -X GET http://localhost:8080/api/v1/facultades
```

#### Crear una Carrera

```bash
curl -X POST http://localhost:8080/api/v1/carreras \
  -H "Content-Type: application/json" \
  -d '{
    "facultadId": 1,
    "nombre": "Ingeniería de Sistemas",
    "descripcion": "Carrera de ingeniería en sistemas computacionales",
    "duracionSemestres": 10,
    "tituloOtorgado": "Ingeniero de Sistemas"
  }'
```

---

## Base de Datos

### Modelo de Datos

#### Tabla: facultades

```sql
CREATE TABLE facultades (
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
```

#### Tabla: carreras

```sql
CREATE TABLE carreras (
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
```

### Diagrama Entidad-Relación

```
┌─────────────────────┐
│     FACULTADES      │
├─────────────────────┤
│ id (PK)             │
│ nombre              │
│ descripcion         │
│ ubicacion           │
│ decano              │
│ fecha_registro      │
│ activo              │
│ created_at          │
│ updated_at          │
└──────────┬──────────┘
           │
           │ 1:N
           │
┌──────────▼──────────┐
│      CARRERAS       │
├─────────────────────┤
│ id (PK)             │
│ facultad_id (FK)    │
│ nombre              │
│ descripcion         │
│ duracion_semestres  │
│ titulo_otorgado     │
│ fecha_registro      │
│ activo              │
│ created_at          │
│ updated_at          │
└─────────────────────┘
```

---

## Pruebas

### Ejecutar pruebas unitarias

```bash
./mvnw test
```

### Ejecutar pruebas de integración

```bash
./mvnw verify
```

### Cobertura de código

```bash
./mvnw clean test jacoco:report
```

Los reportes se generan en: `target/site/jacoco/index.html`

---

## Documentación

### Swagger UI

Una vez que la aplicación esté en ejecución, accede a la documentación interactiva de la API:

```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification

La especificación OpenAPI en formato JSON está disponible en:

```
http://localhost:8080/api-docs
```

### Actuator Endpoints

Información de salud y métricas de la aplicación:

- Health: `http://localhost:8080/actuator/health`
- Info: `http://localhost:8080/actuator/info`
- Metrics: `http://localhost:8080/actuator/metrics`

---

## Modelo de Dominio

### Entidades Principales

#### Facultad

Representa una facultad dentro de la universidad.

**Atributos:**
- `id`: Identificador único
- `nombre`: Nombre de la facultad (normalizado)
- `descripcion`: Descripción detallada
- `ubicacion`: Ubicación física
- `decano`: Nombre del decano actual
- `fechaRegistro`: Fecha de creación
- `activo`: Estado (activo/inactivo)

**Reglas de Negocio:**
- El nombre debe ser único en el sistema
- No se puede desactivar una facultad con carreras activas
- El nombre se normaliza automáticamente (capitalización)

#### Carrera

Representa una carrera académica asociada a una facultad.

**Atributos:**
- `id`: Identificador único
- `facultadId`: Referencia a la facultad
- `nombre`: Nombre de la carrera (normalizado)
- `descripcion`: Descripción detallada
- `duracion`: Duración en semestres (6-14)
- `tituloOtorgado`: Título profesional que otorga
- `fechaRegistro`: Fecha de creación
- `activo`: Estado (activo/inactivo)

**Reglas de Negocio:**
- El nombre debe ser único en el sistema
- La duración debe estar entre 6 y 14 semestres
- Solo se puede crear en facultades activas
- Se clasifica automáticamente (corta/estándar/larga)

### Value Objects

#### NombreAcademico
- Encapsula el nombre de facultades y carreras
- Normaliza automáticamente (capitalización adecuada)
- Valida longitud (3-100 caracteres)
- Genera iniciales automáticamente

#### Duracion
- Encapsula la duración de una carrera
- Valida rango (6-14 semestres)
- Calcula años automáticamente
- Clasifica carrera (corta/estándar/larga)

#### FacultadId, CarreraId
- Encapsulan identificadores
- Validan valores positivos
- Proporcionan tipo seguro

---

## Patrones de Diseño

### Repository Pattern
Abstracción de la capa de persistencia mediante interfaces en el dominio e implementaciones en infraestructura.

### Use Case Pattern
Cada operación de negocio se implementa como un caso de uso independiente con su interfaz.

### Mapper Pattern
Conversión entre objetos de diferentes capas (Domain, DTO, JPA Entity).

### Factory Pattern
Creación de entidades de dominio mediante métodos estáticos (`crear()`).

### DTO Pattern
Objetos de transferencia de datos para desacoplar la API de los modelos de dominio.

---

## Validaciones

### Validaciones de Entrada (Bean Validation)

**Facultad:**
- `nombre`: NotBlank, Size(3-100)
- `descripcion`: Size(max=500)
- `ubicacion`: Size(max=100)
- `decano`: NotBlank, Size(max=100)

**Carrera:**
- `nombre`: NotBlank, Size(3-100)
- `descripcion`: Size(max=500)
- `duracionSemestres`: NotNull, Min(6), Max(14)
- `tituloOtorgado`: NotBlank, Size(5-100)
- `facultadId`: NotNull, Positive

### Validaciones de Negocio (Dominio)

- Unicidad de nombres
- Estado de facultad al crear carreras
- Carreras activas al desactivar facultad
- Rango de duración de carreras

---

## Manejo de Errores

### Códigos de Estado HTTP

| Código | Significado | Cuándo se usa |
|--------|-------------|---------------|
| 200 | OK | Operación exitosa |
| 201 | Created | Recurso creado exitosamente |
| 400 | Bad Request | Datos de entrada inválidos |
| 404 | Not Found | Recurso no encontrado |
| 409 | Conflict | Conflicto de estado (ej: nombre duplicado) |
| 500 | Internal Server Error | Error interno del servidor |

### Estructura de Respuesta de Error

```json
{
  "code": "FACULTAD_NOT_FOUND",
  "message": "Facultad no encontrada con ID: 1",
  "timestamp": "2025-10-31T20:30:00",
  "validationErrors": {
    "nombre": "El nombre debe tener entre 3 y 100 caracteres"
  }
}
```

---

## Buenas Prácticas

### Clean Code
- Nombres descriptivos y significativos
- Métodos pequeños y con una sola responsabilidad
- Comentarios solo cuando es necesario
- Código autoexplicativo

### SOLID Principles
- Cada clase tiene una responsabilidad única
- Código abierto para extensión, cerrado para modificación
- Uso extensivo de interfaces para inversión de dependencias

### Domain-Driven Design
- Ubiquitous Language en el código
- Entidades ricas en comportamiento
- Value Objects inmutables
- Agregados con límites claros

---

## Consideraciones de Producción

### Seguridad
- Implementar autenticación y autorización (Spring Security)
- Validar y sanitizar todas las entradas
- Usar HTTPS en producción
- Proteger endpoints sensibles

### Performance
- Índices en columnas frecuentemente consultadas
- Paginación en listados grandes
- Cache de consultas frecuentes (Redis)
- Connection pooling configurado

### Escalabilidad
- Diseño stateless
- Base de datos puede ser replicada
- Preparado para contenedores (Docker)
- Compatible con Kubernetes

### Monitoreo
- Logs estructurados (JSON)
- Métricas de Prometheus
- Health checks configurados
- Tracing distribuido (opcional)

---

## Contribución

### Proceso de Desarrollo

1. Crear una rama desde `main`
2. Implementar cambios siguiendo los estándares del proyecto
3. Escribir tests unitarios
4. Asegurar que todos los tests pasen
5. Crear Pull Request con descripción detallada

### Estándares de Código

- Seguir convenciones de Java
- Mantener arquitectura limpia
- Documentar métodos públicos
- Tests con cobertura >80%

---

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.
