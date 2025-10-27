# 📦 Application DTOs - Resumen Completo

## ✅ Paquete DTO Completado

**Total de archivos creados: 18 Records inmutables**

---

## 📊 Distribución de DTOs

### 📝 Commands (5 archivos) - Operaciones de Escritura
```
command/
├── RegisterFacultadCommand.java       ✅ Crear facultad
├── UpdateFacultadCommand.java         ✅ Actualizar facultad
├── CambiarDecanoCommand.java          ✅ Cambiar decano (operación específica)
├── RegisterCarreraCommand.java        ✅ Crear carrera
└── UpdateCarreraCommand.java          ✅ Actualizar carrera
```

**Características**:
- ✓ Records inmutables
- ✓ Validaciones con Bean Validation
- ✓ Constructor compacto con trimming
- ✓ Mensajes de error personalizados

---

### 🔍 Queries (6 archivos) - Operaciones de Lectura
```
query/
├── FindFacultadByIdQuery.java         ✅ Buscar facultad por ID
├── FindFacultadByNombreQuery.java     ✅ Buscar facultad por nombre
├── FindAllFacultadesQuery.java        ✅ Listar todas las facultades
├── FindCarreraByIdQuery.java          ✅ Buscar carrera por ID
├── FindCarrerasByFacultadQuery.java   ✅ Carreras de una facultad
└── FindCarrerasByDuracionQuery.java   ✅ Buscar por duración
```

**Características**:
- ✓ Queries inmutables
- ✓ Parámetros opcionales con múltiples constructores
- ✓ Métodos helper para lógica de consulta
- ✓ Soporte para filtros (activos/inactivos)

---

### 📤 Responses (7 archivos) - Objetos de Respuesta
```
response/
├── FacultadResponse.java              ✅ Respuesta completa de facultad
├── FacultadSummaryResponse.java       ✅ Resumen de facultad (para listas)
├── CarreraResponse.java               ✅ Respuesta completa de carrera
├── CarreraSummaryResponse.java        ✅ Resumen de carrera (para listas)
├── OperationResponse.java             ✅ Respuesta genérica de operaciones
├── PageResponse.java                  ✅ Respuesta paginada (genérica)
└── ErrorResponse.java                 ✅ Respuesta de errores
```

**Características**:
- ✓ Responses inmutables
- ✓ Serialización JSON con Jackson
- ✓ Métodos helper para lógica de presentación
- ✓ Factory methods para construcción simplificada
- ✓ Soporte para paginación

---

## 🎯 Patrón CQRS Implementado

```
┌──────────────────────────────────────────────────────────┐
│                    CQRS Pattern                          │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  ┌─────────────────┐         ┌─────────────────┐        │
│  │    COMMANDS     │         │     QUERIES     │        │
│  │   (Escritura)   │         │    (Lectura)    │        │
│  ├─────────────────┤         ├─────────────────┤        │
│  │ Register*       │         │ Find*ById       │        │
│  │ Update*         │         │ Find*By*        │        │
│  │ Cambiar*        │         │ FindAll*        │        │
│  └────────┬────────┘         └────────┬────────┘        │
│           │                           │                 │
│           ▼                           ▼                 │
│  ┌─────────────────────────────────────────────┐       │
│  │           Use Cases Layer                   │       │
│  │  (Aplicará lógica de negocio del dominio)   │       │
│  └─────────────────────────────────────────────┘       │
│           │                           │                 │
│           └───────────┬───────────────┘                 │
│                       ▼                                 │
│           ┌─────────────────────┐                       │
│           │     RESPONSES       │                       │
│           │   (DTOs de salida)  │                       │
│           └─────────────────────┘                       │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

---

## 💡 Ejemplos de Uso por Caso

### Caso 1: Registrar una Nueva Facultad

**Request JSON**:
```json
POST /api/v1/facultades
{
  "nombre": "facultad de ingeniería",
  "descripcion": "Facultad dedicada a las ingenierías",
  "ubicacion": "Edificio Central",
  "decano": "Dr. Juan Pérez"
}
```

**Command** (después de deserialización):
```java
RegisterFacultadCommand command = new RegisterFacultadCommand(
    "facultad de ingeniería",  // Se normalizará a "Facultad de Ingeniería"
    "Facultad dedicada a las ingenierías",
    "Edificio Central",
    "Dr. Juan Pérez"
);
```

**Response**:
```json
{
  "id": 1,
  "nombre": "Facultad de Ingeniería",
  "descripcion": "Facultad dedicada a las ingenierías",
  "ubicacion": "Edificio Central",
  "decano": "Dr. Juan Pérez",
  "fechaRegistro": "2025-10-24T21:00:00",
  "activo": true,
  "cantidadCarreras": 0
}
```

---

### Caso 2: Buscar Carreras de una Facultad

**Request**:
```
GET /api/v1/carreras?facultadId=1&soloActivas=true
```

**Query**:
```java
FindCarrerasByFacultadQuery query = 
    new FindCarrerasByFacultadQuery(1L, true);
```

**Response**:
```json
[
  {
    "id": 1,
    "nombre": "Ingeniería de Sistemas",
    "duracionSemestres": 10,
    "tituloOtorgado": "Ingeniero de Sistemas",
    "activo": true,
    "facultadId": 1,
    "facultadNombre": "Facultad de Ingeniería"
  },
  {
    "id": 2,
    "nombre": "Ingeniería Industrial",
    "duracionSemestres": 10,
    "tituloOtorgado": "Ingeniero Industrial",
    "activo": true,
    "facultadId": 1,
    "facultadNombre": "Facultad de Ingeniería"
  }
]
```

---

### Caso 3: Buscar Carreras Cortas

**Request**:
```
GET /api/v1/carreras/search?duracionMin=6&duracionMax=9
```

**Query**:
```java
FindCarrerasByDuracionQuery query = 
    new FindCarrerasByDuracionQuery(null, 6, 9, true);

// O usando el constructor simplificado:
FindCarrerasByDuracionQuery query = 
    new FindCarrerasByDuracionQuery(6, 9);
```

**Response**:
```json
[
  {
    "id": 5,
    "nombre": "Técnico en Sistemas",
    "duracionSemestres": 8,
    "tituloOtorgado": "Técnico Profesional",
    "activo": true,
    "facultadId": 1,
    "facultadNombre": "Facultad de Ingeniería"
  }
]
```

---

### Caso 4: Listar con Paginación

**Request**:
```
GET /api/v1/facultades?page=0&size=10
```

**Response** (usando PageResponse):
```json
{
  "content": [
    {
      "id": 1,
      "nombre": "Facultad de Ingeniería",
      "ubicacion": "Edificio Central",
      "decano": "Dr. Juan Pérez",
      "activo": true,
      "cantidadCarreras": 5
    },
    {
      "id": 2,
      "nombre": "Facultad de Medicina",
      "ubicacion": "Edificio Médico",
      "decano": "Dra. María García",
      "activo": true,
      "cantidadCarreras": 3
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 15,
  "totalPages": 2,
  "isFirst": true,
  "isLast": false,
  "hasNext": true,
  "hasPrevious": false
}
```

---

### Caso 5: Error de Validación

**Request** (con datos inválidos):
```json
POST /api/v1/carreras
{
  "facultadId": -1,
  "nombre": "In",
  "duracionSemestres": 20
}
```

**Response** (ErrorResponse):
```json
{
  "code": "VALIDATION_ERROR",
  "message": "Errores de validación en los datos de entrada",
  "timestamp": "2025-10-24T21:00:00",
  "validationErrors": {
    "facultadId": "El ID debe ser un número positivo",
    "nombre": "El nombre debe tener entre 3 y 100 caracteres",
    "duracionSemestres": "La duración máxima es de 14 semestres",
    "tituloOtorgado": "El título otorgado es obligatorio"
  }
}
```

---

## 🌟 Características Destacadas

### 1. Inmutabilidad con Records
```java
// ✅ Inmutable por diseño
RegisterFacultadCommand command = new RegisterFacultadCommand(...);
// command.nombre = "otro"; // ❌ Error de compilación

// ✅ Para "modificar", crear nueva instancia
RegisterFacultadCommand updated = new RegisterFacultadCommand(
    "Nuevo nombre",
    command.descripcion(),  // Reutilizar valores
    command.ubicacion(),
    command.decano()
);
```

### 2. Validación Declarativa
```java
public record RegisterCarreraCommand(
    @NotNull(message = "Facultad obligatoria")
    @Positive(message = "ID debe ser positivo")
    Long facultadId,
    
    @NotBlank(message = "Nombre obligatorio")
    @Size(min = 3, max = 100, message = "Entre 3 y 100 caracteres")
    String nombre,
    
    @Min(value = 6, message = "Mínimo 6 semestres")
    @Max(value = 14, message = "Máximo 14 semestres")
    Integer duracionSemestres
) {}
```

### 3. Constructor Compacto
```java
public record RegisterFacultadCommand(String nombre, ...) {
    // Constructor compacto: ejecuta antes de asignar
    public RegisterFacultadCommand {
        // Transformación automática
        if (nombre != null) {
            nombre = nombre.trim();
        }
    }
}
```

### 4. Múltiples Constructors (Sobrecarga)
```java
public record FindCarrerasByFacultadQuery(
    Long facultadId,
    boolean soloActivas
) {
    // Constructor simplificado (solo activas por defecto)
    public FindCarrerasByFacultadQuery(Long facultadId) {
        this(facultadId, true);
    }
}

// Uso:
new FindCarrerasByFacultadQuery(1L);           // Solo activas
new FindCarrerasByFacultadQuery(1L, false);    // Todas
```

### 5. Métodos Helper
```java
public record CarreraSummaryResponse(...) {
    public int getDuracionAnios() {
        return (int) Math.ceil(duracionSemestres / 2.0);
    }
    
    public boolean isEstandar() {
        return duracionSemestres == 10;
    }
}

// Uso:
CarreraSummaryResponse carrera = ...;
if (carrera.isEstandar()) {
    System.out.println("Carrera de 5 años");
}
```

### 6. Factory Methods
```java
public record OperationResponse(...) {
    public static OperationResponse success(String message, Long id) {
        return new OperationResponse(true, message, LocalDateTime.now(), id);
    }
}

// Uso elegante:
return OperationResponse.success("Facultad creada", 1L);
```

---

## 📋 Checklist de Integración

### ✅ DTOs Completados
- [x] 5 Commands para operaciones de escritura
- [x] 6 Queries para operaciones de lectura
- [x] 7 Responses para diferentes necesidades
- [x] Validaciones Bean Validation en todos los DTOs
- [x] Constructor compacto con trimming
- [x] Métodos helper útiles
- [x] Documentación completa

### 🔄 Próximos Pasos (No implementados aún)
- [ ] Mappers (Domain ↔ DTO)
- [ ] Use Cases (Casos de uso de aplicación)
- [ ] Port Adapters (Controllers REST)

---

## 📚 Archivos Generados

### Documentación
1. **APPLICATION_DTO_README.md** - Documentación completa (12KB)
    - Descripción de cada DTO
    - Ejemplos de uso
    - Mejores prácticas
    - Diagramas CQRS

### Código Java
**18 archivos Record** totalmente funcionales:
- 5 Commands
- 6 Queries
- 7 Responses

---

## 🎯 Validaciones Implementadas

### Facultad
- ✓ Nombre: 3-100 caracteres, obligatorio
- ✓ Descripción: máx 500 caracteres, opcional
- ✓ Ubicación: máx 100 caracteres, opcional
- ✓ Decano: obligatorio, máx 100 caracteres

### Carrera
- ✓ Nombre: 3-100 caracteres, obligatorio
- ✓ Duración: 6-14 semestres, obligatorio
- ✓ Título: 5-100 caracteres, obligatorio
- ✓ Facultad ID: positivo, obligatorio

---

## 💻 Tecnologías Utilizadas

- **Java 17+** (Records feature)
- **Jakarta Validation** (Bean Validation)
- **Jackson** (JSON serialization)
- **Spring Boot** (Validation framework)

---

## 🚀 Ventajas de esta Implementación

### 1. Type Safety
```java
// ✅ BIEN: Tipos específicos
RegisterFacultadCommand command = ...;
FacultadResponse response = service.register(command);

// ❌ MAL: Map genérico
Map<String, Object> data = ...;
Map<String, Object> result = service.register(data);
```

### 2. Inmutabilidad
```java
// ✅ Records son inmutables
// No hay setters, solo getters
// Thread-safe por diseño
```

### 3. Validación Automática
```java
@RestController
public class FacultadController {
    @PostMapping
    public ResponseEntity<FacultadResponse> create(
        @Valid @RequestBody RegisterFacultadCommand command
    ) {
        // Spring valida automáticamente
        // Si hay errores, lanza MethodArgumentNotValidException
    }
}
```

### 4. Separación de Concerns (CQRS)
```java
// Escritura: Commands específicos
RegisterFacultadCommand
UpdateFacultadCommand

// Lectura: Queries específicos
FindFacultadByIdQuery
FindAllFacultadesQuery

// Respuesta: DTOs optimizados
FacultadResponse          // Completo
FacultadSummaryResponse   // Resumen
```

---

## 🎓 Principios Aplicados

- ✅ **CQRS** (Command Query Responsibility Segregation)
- ✅ **Immutability** (Records inmutables)
- ✅ **Validation First** (Bean Validation)
- ✅ **Type Safety** (DTOs tipados)
- ✅ **Single Responsibility** (Un DTO = Un propósito)
- ✅ **Clean Architecture** (DTOs en Application Layer)

---

**🎉 DTO Package Completo y Listo para Integrar!**

*Java Records | Bean Validation | CQRS | Clean Architecture*