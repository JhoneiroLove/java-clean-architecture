# APPLICATION LAYER - SERVICES & PORTS
## Documentación Técnica Completa

---

## TABLA DE CONTENIDOS

1. [Introducción](#introducción)
2. [Arquitectura General](#arquitectura-general)
3. [Componentes de la Capa de Aplicación](#componentes-de-la-capa-de-aplicación)
4. [Ports (Interfaces)](#ports-interfaces)
5. [Services (Implementaciones)](#services-implementaciones)
6. [Flujo de Ejecución Completo](#flujo-de-ejecución-completo)
7. [Patrones Implementados](#patrones-implementados)
8. [Integración con otras Capas](#integración-con-otras-capas)
9. [Casos de Uso Detallados](#casos-de-uso-detallados)
10. [Manejo de Errores](#manejo-de-errores)

---

## INTRODUCCIÓN

### Propósito de la Application Layer

La Application Layer (Capa de Aplicación) es responsable de orquestar la lógica de negocio definida en el Domain Layer. Esta capa actúa como intermediario entre los adaptadores de entrada (Controllers, CLI, etc.) y la lógica de negocio pura del dominio.

### Principios Fundamentales

**Separation of Concerns**
- La Application Layer NO contiene lógica de negocio
- Solo orquesta y coordina entre Domain y Infrastructure
- Traduce DTOs a objetos de dominio y viceversa

**Dependency Inversion**
- Depende de abstracciones (Ports), no de implementaciones
- Define contratos que la Infrastructure debe implementar
- Permite intercambio de implementaciones sin modificar la aplicación

**Single Responsibility**
- Cada Service implementa exactamente un caso de uso
- Cada Port define un contrato específico y cohesivo
- Separación clara entre Commands (escritura) y Queries (lectura)

---

## ARQUITECTURA GENERAL

### Hexagonal Architecture (Ports & Adapters)

```
┌─────────────────────────────────────────────────────────────────┐
│                     HEXAGONAL ARCHITECTURE                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌────────────────┐                        ┌────────────────┐  │
│  │   PRIMARY      │                        │   SECONDARY    │  │
│  │   ADAPTERS     │                        │   ADAPTERS     │  │
│  │   (Driving)    │                        │   (Driven)     │  │
│  │                │                        │                │  │
│  │  REST API      │                        │  JPA Adapter   │  │
│  │  GraphQL       │                        │  MongoDB       │  │
│  │  CLI           │                        │  Email Service │  │
│  │  Message Queue │                        │  External API  │  │
│  └────────┬───────┘                        └────────▲───────┘  │
│           │                                         │          │
│           │ invoca                                  │ impl.    │
│           ▼                                         │          │
│  ┌────────────────┐                        ┌────────────────┐  │
│  │   INPUT PORTS  │                        │  OUTPUT PORTS  │  │
│  │  (Use Cases)   │                        │ (Repositories) │  │
│  │                │                        │                │  │
│  │  Register      │                        │  Repository    │  │
│  │  Update        │                        │  Interfaces    │  │
│  │  Find          │                        │                │  │
│  └────────┬───────┘                        └────────▲───────┘  │
│           │                                         │          │
│           │ implementado por              usado por │          │
│           ▼                                         │          │
│  ┌──────────────────────────────────────────────────┴───────┐  │
│  │              APPLICATION SERVICES                        │  │
│  │                                                           │  │
│  │  - Orquestación de casos de uso                          │  │
│  │  - Coordinación entre Domain y Adapters                  │  │
│  │  - Mapeo de DTOs a Domain y viceversa                    │  │
│  │  - Gestión transaccional                                 │  │
│  │                                                           │  │
│  │  ┌────────────────┐         ┌──────────────────┐         │  │
│  │  │  Mapper        │         │  Domain Service  │         │  │
│  │  │  Utils         │         │  (Validaciones)  │         │  │
│  │  └────────────────┘         └──────────────────┘         │  │
│  └───────────────────────────────┬──────────────────────────┘  │
│                                  │ usa                         │
│                                  ▼                             │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    DOMAIN LAYER                          │  │
│  │                                                           │  │
│  │  - Entidades (Facultad, Carrera)                         │  │
│  │  - Value Objects (FacultadId, NombreAcademico, etc.)     │  │
│  │  - Domain Services (Validaciones complejas)              │  │
│  │  - Domain Events                                         │  │
│  │  - Business Rules                                        │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Flujo de Dependencias

```
Primary Adapters (Controllers)
        │
        │ depende de
        ▼
    Input Ports (Use Case Interfaces)
        ▲
        │ implementado por
        │
Application Services (Orquestación)
        │
        ├─ usa ──────► Domain Model
        │
        └─ usa ──────► Output Ports (Repository Interfaces)
                            ▲
                            │ implementado por
                            │
                    Secondary Adapters (JPA, MongoDB, etc.)
```

**Regla de Dependencia**: Las dependencias siempre apuntan hacia adentro (hacia el dominio).

---

## COMPONENTES DE LA CAPA DE APLICACIÓN

### Estructura de Directorios

```
application/
├── config/
│   └── DomainServiceConfig.java          # Configuración de beans de dominio
│
├── dto/
│   ├── command/                          # DTOs para operaciones de escritura
│   │   ├── RegisterFacultadCommand.java
│   │   ├── UpdateFacultadCommand.java
│   │   ├── CambiarDecanoCommand.java
│   │   ├── RegisterCarreraCommand.java
│   │   └── UpdateCarreraCommand.java
│   │
│   ├── query/                            # DTOs para operaciones de lectura
│   │   ├── FindFacultadByIdQuery.java
│   │   ├── FindFacultadByNombreQuery.java
│   │   ├── FindAllFacultadesQuery.java
│   │   ├── FindCarreraByIdQuery.java
│   │   ├── FindCarrerasByFacultadQuery.java
│   │   └── FindCarrerasByDuracionQuery.java
│   │
│   └── response/                         # DTOs para respuestas
│       ├── FacultadResponse.java
│       ├── FacultadSummaryResponse.java
│       ├── CarreraResponse.java
│       ├── CarreraSummaryResponse.java
│       └── OperationResponse.java
│
├── mapper/
│   ├── FacultadMapper.java               # DTO ↔ Domain
│   ├── CarreraMapper.java                # DTO ↔ Domain
│   └── MapperUtils.java                  # Utilidades comunes
│
├── port/
│   ├── in/                               # Input Ports (Use Cases)
│   │   ├── facultad/
│   │   │   ├── RegisterFacultadUseCase.java
│   │   │   ├── UpdateFacultadUseCase.java
│   │   │   ├── CambiarDecanoUseCase.java
│   │   │   ├── FindFacultadUseCase.java
│   │   │   └── ManageFacultadStatusUseCase.java
│   │   │
│   │   └── carrera/
│   │       ├── RegisterCarreraUseCase.java
│   │       ├── UpdateCarreraUseCase.java
│   │       ├── FindCarreraUseCase.java
│   │       └── ManageCarreraStatusUseCase.java
│   │
│   └── out/                              # Output Ports (Repository Interfaces)
│       ├── FacultadRepositoryPort.java
│       └── CarreraRepositoryPort.java
│
└── service/                              # Implementación de Use Cases
    ├── facultad/
    │   ├── RegisterFacultadService.java
    │   ├── UpdateFacultadService.java
    │   ├── CambiarDecanoService.java
    │   ├── FindFacultadService.java
    │   └── ManageFacultadStatusService.java
    │
    └── carrera/
        ├── RegisterCarreraService.java
        ├── UpdateCarreraService.java
        ├── FindCarreraService.java
        └── ManageCarreraStatusService.java
```

---

## PORTS (INTERFACES)

### Input Ports (Puertos de Entrada)

Los Input Ports definen los casos de uso de la aplicación. Son interfaces que representan las operaciones disponibles para los adaptadores primarios (Controllers, CLI, etc.).

#### Características

- **Uno por caso de uso**: Cada interfaz representa un caso de uso específico
- **Independientes del framework**: No tienen anotaciones de Spring
- **Inmutables**: Reciben DTOs inmutables (Java Records)
- **Type-safe**: Fuertemente tipadas con Value Objects del dominio

#### Patrón de Nomenclatura

```
[Verbo][Entidad]UseCase

Ejemplos:
- RegisterFacultadUseCase
- UpdateCarreraUseCase
- FindFacultadUseCase
- ManageCarreraStatusUseCase
```

#### Ejemplo: RegisterFacultadUseCase

```java
package com.university.cleanarchitecture.application.port.in.facultad;

import com.university.cleanarchitecture.application.dto.command.RegisterFacultadCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;

/**
 * Puerto de entrada para registrar una nueva Facultad.
 * 
 * Define el contrato para el caso de uso de creación de facultades.
 * Los adaptadores de entrada (Controllers, CLI, etc.) utilizan esta interfaz.
 * 
 * Patrón: Hexagonal Architecture - Input Port
 * CQRS: Command (operación de escritura)
 */
public interface RegisterFacultadUseCase {

    /**
     * Registra una nueva facultad en el sistema.
     * 
     * Proceso:
     * 1. Valida que no exista una facultad con el mismo nombre
     * 2. Crea la entidad de dominio
     * 3. Persiste en el repositorio
     * 4. Retorna la respuesta con los datos de la facultad creada
     * 
     * @param command datos para crear la facultad (inmutable)
     * @return FacultadResponse con la información de la facultad registrada
     * @throws IllegalArgumentException si el comando tiene datos inválidos
     * @throws DomainException si ya existe una facultad con el mismo nombre
     */
    FacultadResponse register(RegisterFacultadCommand command);
}
```

#### Ejemplo: FindFacultadUseCase

```java
package com.university.cleanarchitecture.application.port.in.facultad;

import com.university.cleanarchitecture.application.dto.query.FindAllFacultadesQuery;
import com.university.cleanarchitecture.application.dto.query.FindFacultadByIdQuery;
import com.university.cleanarchitecture.application.dto.query.FindFacultadByNombreQuery;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.dto.response.FacultadSummaryResponse;

import java.util.List;

/**
 * Puerto de entrada para consultas de Facultades.
 * 
 * Define el contrato para todos los casos de uso de lectura de facultades.
 * 
 * Patrón: Hexagonal Architecture - Input Port
 * CQRS: Query (operaciones de lectura)
 */
public interface FindFacultadUseCase {

    /**
     * Busca una facultad por su ID.
     * 
     * @param query query con el ID de la facultad
     * @return FacultadResponse con la información completa
     * @throws FacultadNotFoundException si no existe la facultad
     */
    FacultadResponse findById(FindFacultadByIdQuery query);

    /**
     * Busca una facultad por su nombre.
     * 
     * @param query query con el nombre de la facultad
     * @return FacultadResponse con la información completa
     * @throws FacultadNotFoundException si no existe la facultad
     */
    FacultadResponse findByNombre(FindFacultadByNombreQuery query);

    /**
     * Obtiene todas las facultades del sistema.
     * 
     * @param query query con filtros (incluir inactivas o no)
     * @return lista de FacultadSummaryResponse
     */
    List<FacultadSummaryResponse> findAll(FindAllFacultadesQuery query);
}
```

### Output Ports (Puertos de Salida)

Los Output Ports definen las operaciones que la aplicación necesita del mundo exterior (persistencia, servicios externos, etc.). Son interfaces que abstraen los detalles de implementación.

#### Características

- **Abstraen la persistencia**: No exponen detalles de JPA, MongoDB, etc.
- **Trabajan con entidades de dominio**: No con DTOs ni Entities de JPA
- **Extienden interfaces del dominio**: Mantienen compatibilidad
- **Independientes de tecnología**: Permiten cambiar de BD sin afectar la aplicación

#### Ejemplo: FacultadRepositoryPort

```java
package com.university.cleanarchitecture.application.port.out;

import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.domain.repository.FacultadRepository;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Facultades.
 * 
 * Define el contrato que los adaptadores de persistencia (JPA, MongoDB, etc.)
 * deben implementar para que la Application Layer pueda persistir facultades.
 * 
 * EXTIENDE la interfaz del dominio (FacultadRepository) para mantener
 * compatibilidad con el Domain Service que usa la interfaz del dominio.
 * 
 * Esto respeta Clean Architecture: el dominio define el contrato,
 * la aplicación puede extenderlo si necesita métodos adicionales.
 * 
 * Patrón: Hexagonal Architecture - Output Port
 * Patrón: Repository Pattern
 */
public interface FacultadRepositoryPort extends FacultadRepository {
    // Hereda todos los métodos de FacultadRepository del dominio:
    // - save(Facultad)
    // - findById(FacultadId)
    // - findByNombre(NombreAcademico)
    // - findAllActive()
    // - findAll()
    // - existsByNombre(NombreAcademico)
    // - existsByNombreAndIdNot(NombreAcademico, FacultadId)
    // - countCarrerasByFacultadId(FacultadId)
    // - deleteById(FacultadId)
    
    // Aquí se pueden agregar métodos adicionales específicos de la aplicación
    // Ejemplo: List<Facultad> findByUbicacion(String ubicacion);
}
```

#### Ejemplo: CarreraRepositoryPort

```java
package com.university.cleanarchitecture.application.port.out;

import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.Duracion;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.domain.repository.CarreraRepository;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Carreras.
 * 
 * EXTIENDE la interfaz del dominio (CarreraRepository) para mantener
 * compatibilidad con el Domain Service.
 * 
 * Patrón: Hexagonal Architecture - Output Port
 */
public interface CarreraRepositoryPort extends CarreraRepository {
    // Hereda todos los métodos de CarreraRepository del dominio
    
    /**
     * Método adicional específico de la aplicación:
     * Busca carreras dentro de un rango de duración.
     * 
     * @param minSemestres duración mínima (inclusive)
     * @param maxSemestres duración máxima (inclusive)
     * @return lista de carreras dentro del rango
     */
    List<Carrera> findByDuracionBetween(int minSemestres, int maxSemestres);
}
```

---

## SERVICES (IMPLEMENTACIONES)

Los Services son las implementaciones concretas de los Use Cases (Input Ports). Contienen la lógica de orquestación y coordinación.

### Responsabilidades de un Service

1. **Orquestación**: Coordina entre múltiples componentes
2. **Validación de Aplicación**: Verifica unicidad, existencia, etc.
3. **Mapeo**: Transforma DTOs a Domain y viceversa
4. **Transaccionalidad**: Gestiona las transacciones
5. **Coordinación**: Llama a Domain Services cuando es necesario

### Anatomía de un Service

```java
@Service                                    // 1. Anotación de Spring
@Transactional                              // 2. Gestión transaccional
public class RegisterFacultadService        // 3. Nombre descriptivo
        implements RegisterFacultadUseCase { // 4. Implementa Use Case

    // 5. Dependencias (Output Ports)
    private final FacultadRepositoryPort facultadRepository;
    private final CarreraRepositoryPort carreraRepository;
    
    // 6. Generador de IDs (temporal, en producción viene de BD)
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    // 7. Constructor Injection
    public RegisterFacultadService(
            FacultadRepositoryPort facultadRepository,
            CarreraRepositoryPort carreraRepository) {
        this.facultadRepository = facultadRepository;
        this.carreraRepository = carreraRepository;
    }

    // 8. Implementación del caso de uso
    @Override
    public FacultadResponse register(RegisterFacultadCommand command) {
        // Pasos de orquestación
        // ...
    }
}
```

### Ejemplo Completo: RegisterFacultadService

```java
package com.university.cleanarchitecture.application.service.facultad;

import com.university.cleanarchitecture.application.dto.command.RegisterFacultadCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.mapper.FacultadMapper;
import com.university.cleanarchitecture.application.port.in.facultad.RegisterFacultadUseCase;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación del caso de uso: Registrar Facultad.
 * 
 * Responsabilidades:
 * - Orquestar la creación de una facultad
 * - Validar reglas de negocio a nivel de aplicación (unicidad)
 * - Coordinar entre mappers, repositorios y dominio
 * - Gestionar la transacción
 * 
 * Patrón: Application Service
 * Patrón: Transaction Script (orquestación)
 */
@Service
@Transactional
public class RegisterFacultadService implements RegisterFacultadUseCase {

    private final FacultadRepositoryPort facultadRepository;
    private final CarreraRepositoryPort carreraRepository;
    
    // Simulador de generación de IDs (en producción vendría de la BD)
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public RegisterFacultadService(
            FacultadRepositoryPort facultadRepository,
            CarreraRepositoryPort carreraRepository) {
        this.facultadRepository = facultadRepository;
        this.carreraRepository = carreraRepository;
    }

    @Override
    public FacultadResponse register(RegisterFacultadCommand command) {
        // PASO 1: Validar comando
        // Bean Validation ya validó la estructura, pero verificamos nulidad
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo");
        }

        // PASO 2: Convertir nombre a Value Object
        // Esto valida el formato del nombre según reglas de dominio
        NombreAcademico nombre = NombreAcademico.of(command.nombre());

        // PASO 3: Validar unicidad (regla de aplicación)
        // Esta validación requiere acceso al repositorio
        if (facultadRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException(
                    "Ya existe una facultad con el nombre: " + nombre.getValue()
            );
        }

        // PASO 4: Generar nuevo ID
        // En producción, esto lo hace la base de datos con @GeneratedValue
        FacultadId facultadId = FacultadId.of(ID_GENERATOR.getAndIncrement());

        // PASO 5: Mapear Command → Domain Entity
        // El mapper delega la creación al constructor o factory method del dominio
        Facultad facultad = FacultadMapper.toDomain(command, facultadId);

        // PASO 6: Persistir en el repositorio
        // El repositorio implementa el Output Port
        Facultad savedFacultad = facultadRepository.save(facultad);

        // PASO 7: Obtener datos adicionales para la respuesta
        // Contar carreras asociadas (nueva facultad = 0 carreras)
        long carrerasCount = carreraRepository.countActiveByFacultadId(savedFacultad.getId());

        // PASO 8: Mapear Domain → Response DTO
        // El mapper convierte la entidad de dominio a DTO de respuesta
        return FacultadMapper.toResponse(savedFacultad, (int) carrerasCount);
    }
}
```

### Ejemplo: Service con Domain Service

```java
package com.university.cleanarchitecture.application.service.carrera;

import com.university.cleanarchitecture.application.dto.command.RegisterCarreraCommand;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;
import com.university.cleanarchitecture.application.mapper.CarreraMapper;
import com.university.cleanarchitecture.application.port.in.carrera.RegisterCarreraUseCase;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.domain.service.CarreraDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación del caso de uso: Registrar Carrera.
 * 
 * Responsabilidades:
 * - Orquestar la creación de una carrera
 * - Validar reglas de negocio usando Domain Service
 * - Coordinar entre mappers, repositorios y dominio
 * 
 * Nota: Este service usa CarreraDomainService para validaciones complejas
 * que involucran múltiples agregados (Facultad y Carrera).
 */
@Service
@Transactional
public class RegisterCarreraService implements RegisterCarreraUseCase {

    private final CarreraRepositoryPort carreraRepository;
    private final FacultadRepositoryPort facultadRepository;
    private final CarreraDomainService domainService;
    
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public RegisterCarreraService(
            CarreraRepositoryPort carreraRepository,
            FacultadRepositoryPort facultadRepository,
            CarreraDomainService domainService) {
        this.carreraRepository = carreraRepository;
        this.facultadRepository = facultadRepository;
        this.domainService = domainService;
    }

    @Override
    public CarreraResponse register(RegisterCarreraCommand command) {
        // PASO 1: Validar comando
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo");
        }

        // PASO 2: Convertir IDs a Value Objects
        FacultadId facultadId = FacultadId.of(command.facultadId());
        NombreAcademico nombre = NombreAcademico.of(command.nombre());

        // PASO 3: Validar reglas de negocio usando DOMAIN SERVICE
        // El Domain Service encapsula validaciones complejas que involucran
        // múltiples agregados:
        // - Verifica que la facultad exista y esté activa
        // - Verifica que no exista otra carrera con el mismo nombre
        domainService.validarCreacionCarrera(facultadId, nombre);

        // PASO 4: Generar nuevo ID
        CarreraId carreraId = CarreraId.of(ID_GENERATOR.getAndIncrement());

        // PASO 5: Mapear Command → Domain Entity
        Carrera carrera = CarreraMapper.toDomain(command, carreraId);

        // PASO 6: Persistir en el repositorio
        Carrera savedCarrera = carreraRepository.save(carrera);

        // PASO 7: Obtener nombre de la facultad para el response
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(); // No debería fallar porque ya se validó antes
        String facultadNombre = facultad.getNombre().getValue();

        // PASO 8: Mapear Domain → Response
        return CarreraMapper.toResponse(savedCarrera, facultadNombre);
    }
}
```

### Ejemplo: Query Service (Solo Lectura)

```java
package com.university.cleanarchitecture.application.service.facultad;

import com.university.cleanarchitecture.application.dto.query.FindAllFacultadesQuery;
import com.university.cleanarchitecture.application.dto.query.FindFacultadByIdQuery;
import com.university.cleanarchitecture.application.dto.query.FindFacultadByNombreQuery;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.dto.response.FacultadSummaryResponse;
import com.university.cleanarchitecture.application.mapper.FacultadMapper;
import com.university.cleanarchitecture.application.port.in.facultad.FindFacultadUseCase;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.exception.FacultadNotFoundException;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso: Consultas de Facultades.
 * 
 * Implementa todas las operaciones de lectura (Queries) según patrón CQRS.
 * 
 * Nota: Usa @Transactional(readOnly = true) para optimizar las lecturas.
 */
@Service
@Transactional(readOnly = true)
public class FindFacultadService implements FindFacultadUseCase {

    private final FacultadRepositoryPort facultadRepository;
    private final CarreraRepositoryPort carreraRepository;

    public FindFacultadService(
            FacultadRepositoryPort facultadRepository,
            CarreraRepositoryPort carreraRepository) {
        this.facultadRepository = facultadRepository;
        this.carreraRepository = carreraRepository;
    }

    @Override
    public FacultadResponse findById(FindFacultadByIdQuery query) {
        // PASO 1: Validar query
        if (query == null) {
            throw new IllegalArgumentException("El query no puede ser nulo");
        }

        // PASO 2: Buscar facultad por ID
        FacultadId facultadId = FacultadId.of(query.facultadId());
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(query.facultadId()));

        // PASO 3: Obtener datos adicionales
        long carrerasCount = carreraRepository.countActiveByFacultadId(facultadId);

        // PASO 4: Mapear Domain → Response
        return FacultadMapper.toResponse(facultad, (int) carrerasCount);
    }

    @Override
    public FacultadResponse findByNombre(FindFacultadByNombreQuery query) {
        // PASO 1: Validar query
        if (query == null) {
            throw new IllegalArgumentException("El query no puede ser nulo");
        }

        // PASO 2: Buscar por nombre
        NombreAcademico nombre = NombreAcademico.of(query.nombre());
        Facultad facultad = facultadRepository.findByNombre(nombre)
                .orElseThrow(() -> new FacultadNotFoundException(
                        "No se encontró facultad con nombre: " + nombre.getValue()
                ));

        // PASO 3: Obtener datos adicionales
        long carrerasCount = carreraRepository.countActiveByFacultadId(facultad.getId());

        // PASO 4: Mapear Domain → Response
        return FacultadMapper.toResponse(facultad, (int) carrerasCount);
    }

    @Override
    public List<FacultadSummaryResponse> findAll(FindAllFacultadesQuery query) {
        // PASO 1: Validar query
        if (query == null) {
            throw new IllegalArgumentException("El query no puede ser nulo");
        }

        // PASO 2: Obtener facultades según filtro
        List<Facultad> facultades = query.incluirInactivas()
                ? facultadRepository.findAll()
                : facultadRepository.findAllActive();

        // PASO 3: Mapear cada facultad a SummaryResponse
        return facultades.stream()
                .map(facultad -> {
                    // Obtener datos adicionales para cada facultad
                    long carrerasCount = carreraRepository
                            .countActiveByFacultadId(facultad.getId());
                    return FacultadMapper.toSummaryResponse(facultad, (int) carrerasCount);
                })
                .collect(Collectors.toList());
    }
}
```

---

## FLUJO DE EJECUCIÓN COMPLETO

### Caso de Uso: Registrar una Facultad

Este ejemplo muestra el flujo completo desde el Controller hasta la Base de Datos.

```
┌─────────────────────────────────────────────────────────────┐
│  PASO 1: REQUEST HTTP                                       │
│  POST /api/v1/facultades                                    │
│  Content-Type: application/json                             │
│                                                             │
│  {                                                          │
│    "nombre": "Facultad de Ingeniería",                      │
│    "descripcion": "Facultad de ciencias...",                │
│    "ubicacion": "Edificio A",                               │
│    "decano": "Dr. Juan Pérez"                               │
│  }                                                          │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  PASO 2: CONTROLLER (Primary Adapter)                      │
│  @RestController                                            │
│  public class FacultadController {                          │
│                                                             │
│      @PostMapping("/api/v1/facultades")                     │
│      public ResponseEntity<FacultadResponse> create(        │
│          @Valid @RequestBody                                │
│          RegisterFacultadCommand command                    │
│      ) {                                                    │
│          // Bean Validation valida el DTO                   │
│          FacultadResponse response =                        │
│              registerUseCase.register(command);             │
│          return ResponseEntity                              │
│              .status(201)                                   │
│              .body(response);                               │
│      }                                                      │
│  }                                                          │
└─────────────────────┬───────────────────────────────────────┘
                      │ invoca
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  PASO 3: INPUT PORT (Use Case Interface)                   │
│  public interface RegisterFacultadUseCase {                 │
│      FacultadResponse register(                             │
│          RegisterFacultadCommand command                    │
│      );                                                     │
│  }                                                          │
└─────────────────────┬───────────────────────────────────────┘
                      │ implementado por
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  PASO 4: APPLICATION SERVICE                                │
│  @Service                                                   │
│  @Transactional                                             │
│  public class RegisterFacultadService                       │
│         implements RegisterFacultadUseCase {                │
│                                                             │
│      public FacultadResponse register(Command cmd) {        │
│                                                             │
│          // 4.1: Convertir String → Value Object           │
│          NombreAcademico nombre =                           │
│              NombreAcademico.of(cmd.nombre());              │
│                                                             │
│          // 4.2: Validar unicidad (Output Port)            │
│          if (facultadRepo.existsByNombre(nombre)) {         │
│              throw new IllegalArgumentException(...);       │
│          }                                                  │
│                                                             │
│          // 4.3: Generar ID                                 │
│          FacultadId id = FacultadId.of(1L);                 │
│                                                             │
│          // 4.4: Mapper - Command → Domain                  │
│          Facultad facultad =                                │
│              FacultadMapper.toDomain(cmd, id);              │
│                                                             │
│          // 4.5: Persistir (Output Port)                    │
│          Facultad saved =                                   │
│              facultadRepo.save(facultad);                   │
│                                                             │
│          // 4.6: Obtener datos adicionales                  │
│          long count = carreraRepo                           │
│              .countActiveByFacultadId(id);                  │
│                                                             │
│          // 4.7: Mapper - Domain → Response                 │
│          return FacultadMapper                              │
│              .toResponse(saved, (int)count);                │
│      }                                                      │
│  }                                                          │
└─────────────────────┬───────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
        ▼             ▼             ▼
┌──────────────┐ ┌──────────┐ ┌──────────────┐
│  MAPPER      │ │  DOMAIN  │ │  OUTPUT PORT │
│  (Utils)     │ │  MODEL   │ │  (Interface) │
└──────────────┘ └──────────┘ └──────┬───────┘
                                     │ implementado por
                                     ▼
┌─────────────────────────────────────────────────────────────┐
│  PASO 5: SECONDARY ADAPTER (JPA Adapter)                   │
│  @Repository                                                │
│  public class FacultadJpaAdapter                            │
│         implements FacultadRepositoryPort {                 │
│                                                             │
│      private final FacultadJpaRepository jpaRepo;           │
│                                                             │
│      public Facultad save(Facultad domain) {                │
│          // 5.1: Domain → JPA Entity                        │
│          FacultadEntity entity =                            │
│              FacultadJpaMapper.toEntity(domain);            │
│                                                             │
│          // 5.2: Persistir en BD                            │
│          FacultadEntity saved =                             │
│              jpaRepo.save(entity);                          │
│                                                             │
│          // 5.3: JPA Entity → Domain                        │
│          return FacultadJpaMapper.toDomain(saved);          │
│      }                                                      │
│  }                                                          │
└─────────────────────┬───────────────────────────────────────┘
                      │ usa
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  PASO 6: SPRING DATA JPA REPOSITORY                        │
│  public interface FacultadJpaRepository                     │
│         extends JpaRepository<FacultadEntity, Long> {       │
│      // Spring Data implementa automáticamente              │
│  }                                                          │
└─────────────────────┬───────────────────────────────────────┘
                      │ persiste
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  PASO 7: DATABASE (PostgreSQL)                             │
│                                                             │
│  INSERT INTO facultades (nombre, descripcion, ...)          │
│  VALUES ('Facultad de Ingeniería', '...', ...);             │
│                                                             │
│  RETURNING id, nombre, descripcion, ...                     │
└─────────────────────┬───────────────────────────────────────┘
                      │ retorna datos
                      │
      ┌───────────────┴───────────────┐
      │    Flujo de retorno inverso    │
      │    (DB → JPA → Adapter →       │
      │     Service → Controller)      │
      └───────────────┬───────────────┘
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  PASO 8: HTTP RESPONSE                                      │
│  Status: 201 Created                                        │
│  Content-Type: application/json                             │
│                                                             │
│  {                                                          │
│    "facultadId": 1,                                         │
│    "nombre": "Facultad de Ingeniería",                      │
│    "descripcion": "Facultad de ciencias...",                │
│    "ubicacion": "Edificio A",                               │
│    "decano": "Dr. Juan Pérez",                              │
│    "activo": true,                                          │
│    "cantidadCarreras": 0,                                   │
│    "fechaCreacion": "2025-10-28T04:00:00Z"                  │
│  }                                                          │
└─────────────────────────────────────────────────────────────┘
```

### Flujo de Datos Detallado

```
JSON (Controller)
    ↓ Bean Validation
RegisterFacultadCommand (DTO - Immutable Record)
    ↓ Mapper
Facultad (Domain Entity)
    ↓ JPA Mapper
FacultadEntity (JPA Entity)
    ↓ SQL
DATABASE TABLE (facultades)
    ↓ retorno inverso
FacultadEntity (JPA Entity)
    ↓ JPA Mapper
Facultad (Domain Entity)
    ↓ Mapper
FacultadResponse (DTO - Immutable Record)
    ↓ Jackson
JSON (Controller Response)
```

---

## PATRONES IMPLEMENTADOS

### Command Query Responsibility Segregation (CQRS)

Separación clara entre operaciones de escritura (Commands) y lectura (Queries).

**Commands (Escritura)**
```
RegisterFacultadUseCase    → Crea nueva facultad
UpdateFacultadUseCase      → Actualiza facultad existente
CambiarDecanoUseCase       → Modifica solo el decano
ManageFacultadStatusUseCase → Activa/Desactiva/Elimina
```

**Queries (Lectura)**
```
FindFacultadUseCase
    ├─ findById()          → Busca por ID
    ├─ findByNombre()      → Busca por nombre
    └─ findAll()           → Lista todas
```

**Ventajas**:
- Optimización independiente (escritura vs lectura)
- Modelos diferentes para Command y Query
- Escalabilidad (replica de lectura separada)

### Repository Pattern

Abstracción de la capa de persistencia mediante interfaces (Output Ports).

```java
// Interfaz (Application Layer)
public interface FacultadRepositoryPort {
    Facultad save(Facultad facultad);
    Optional<Facultad> findById(FacultadId id);
}

// Implementación (Infrastructure Layer)
@Repository
public class FacultadJpaAdapter implements FacultadRepositoryPort {
    // Implementación con JPA
}
```

**Ventajas**:
- Independencia de la tecnología de persistencia
- Facilita el testing (mocks)
- Permite cambiar de JPA a MongoDB sin modificar la aplicación

### Dependency Inversion Principle (DIP)

Los módulos de alto nivel no dependen de los de bajo nivel. Ambos dependen de abstracciones.

```
RegisterFacultadService (alto nivel)
        ↓ depende de
FacultadRepositoryPort (abstracción)
        ↑ implementado por
FacultadJpaAdapter (bajo nivel)
```

### Mapper Pattern

Separación de responsabilidades entre DTOs y Domain Entities.

```java
public class FacultadMapper {
    
    // DTO → Domain
    public static Facultad toDomain(
            RegisterFacultadCommand command, 
            FacultadId id) {
        return Facultad.crear(
            id,
            NombreAcademico.of(command.nombre()),
            command.descripcion(),
            command.ubicacion(),
            command.decano()
        );
    }
    
    // Domain → DTO
    public static FacultadResponse toResponse(
            Facultad facultad, 
            int cantidadCarreras) {
        return new FacultadResponse(
            facultad.getId().getValue(),
            facultad.getNombre().getValue(),
            facultad.getDescripcion(),
            facultad.getUbicacion(),
            facultad.getDecano(),
            facultad.isActivo(),
            cantidadCarreras
        );
    }
}
```

### Transaction Script Pattern

Orquestación de la lógica de aplicación en Services.

```java
@Transactional
public FacultadResponse register(RegisterFacultadCommand command) {
    // Script de pasos secuenciales
    // 1. Validar
    // 2. Convertir
    // 3. Persistir
    // 4. Responder
}
```

---

## INTEGRACIÓN CON OTRAS CAPAS

### Integración con Domain Layer

La Application Layer usa el Domain Layer pero NO lo modifica.

**Uso de Entidades de Dominio**:
```java
// Application Service
public FacultadResponse register(RegisterFacultadCommand command) {
    // Crear entidad usando factory method del dominio
    Facultad facultad = Facultad.crear(
        id,
        NombreAcademico.of(command.nombre()),
        command.descripcion(),
        command.ubicacion(),
        command.decano()
    );
    
    // La entidad valida sus propias reglas de negocio
    // El service solo orquesta
}
```

**Uso de Domain Services**:
```java
// Application Service
public CarreraResponse register(RegisterCarreraCommand command) {
    FacultadId facultadId = FacultadId.of(command.facultadId());
    NombreAcademico nombre = NombreAcademico.of(command.nombre());
    
    // Delegar validaciones complejas al Domain Service
    domainService.validarCreacionCarrera(facultadId, nombre);
    
    // Continuar con la orquestación...
}
```

### Integración con Infrastructure Layer

La Application Layer define contratos (Output Ports) que Infrastructure implementa.

**Definición del Contrato (Application)**:
```java
// application/port/out/FacultadRepositoryPort.java
public interface FacultadRepositoryPort extends FacultadRepository {
    Facultad save(Facultad facultad);
    Optional<Facultad> findById(FacultadId id);
    // ...
}
```

**Implementación del Contrato (Infrastructure)**:
```java
// infrastructure/adapter/persistence/FacultadJpaAdapter.java
@Repository
public class FacultadJpaAdapter implements FacultadRepositoryPort {
    
    private final FacultadJpaRepository jpaRepository;
    
    @Override
    public Facultad save(Facultad facultad) {
        // Implementación con JPA
        FacultadEntity entity = mapper.toEntity(facultad);
        FacultadEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
```

**Inyección de Dependencias**:
```java
// Application Service
@Service
public class RegisterFacultadService implements RegisterFacultadUseCase {
    
    // Spring inyecta la implementación de Infrastructure
    private final FacultadRepositoryPort repository;
    
    public RegisterFacultadService(FacultadRepositoryPort repository) {
        this.repository = repository;
    }
}
```

### Integración con Presentation Layer

La Presentation Layer (Controllers) llama a los Use Cases (Input Ports).

**Controller (Presentation)**:
```java
@RestController
@RequestMapping("/api/v1/facultades")
@RequiredArgsConstructor
public class FacultadController {
    
    // Inyecta el Use Case (Input Port)
    private final RegisterFacultadUseCase registerUseCase;
    private final FindFacultadUseCase findUseCase;
    
    @PostMapping
    public ResponseEntity<FacultadResponse> create(
            @Valid @RequestBody RegisterFacultadCommand command) {
        
        FacultadResponse response = registerUseCase.register(command);
        return ResponseEntity.status(201).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FacultadResponse> getById(
            @PathVariable Long id) {
        
        FindFacultadByIdQuery query = new FindFacultadByIdQuery(id);
        FacultadResponse response = findUseCase.findById(query);
        return ResponseEntity.ok(response);
    }
}
```

---

## CASOS DE USO DETALLADOS

### Caso de Uso: Actualizar Facultad

**Flujo**:
1. Controller recibe `UpdateFacultadCommand`
2. Service busca la facultad existente
3. Service valida que el nuevo nombre no exista en otra facultad
4. Service aplica los cambios usando método de dominio
5. Service persiste los cambios
6. Service retorna la facultad actualizada

**Implementación**:
```java
@Service
@Transactional
public class UpdateFacultadService implements UpdateFacultadUseCase {

    private final FacultadRepositoryPort facultadRepository;
    private final CarreraRepositoryPort carreraRepository;

    @Override
    public FacultadResponse update(UpdateFacultadCommand command) {
        // 1. Buscar facultad existente
        FacultadId facultadId = FacultadId.of(command.facultadId());
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(command.facultadId()));

        // 2. Validar unicidad del nuevo nombre
        NombreAcademico nuevoNombre = NombreAcademico.of(command.nombre());
        if (facultadRepository.existsByNombreAndIdNot(nuevoNombre, facultadId)) {
            throw new IllegalArgumentException(
                    "Ya existe otra facultad con el nombre: " + nuevoNombre.getValue()
            );
        }

        // 3. Aplicar cambios (delega al dominio las validaciones)
        FacultadMapper.applyUpdateCommand(command, facultad);

        // 4. Persistir
        Facultad updatedFacultad = facultadRepository.save(facultad);

        // 5. Obtener datos adicionales y retornar
        long carrerasCount = carreraRepository.countActiveByFacultadId(updatedFacultad.getId());
        return FacultadMapper.toResponse(updatedFacultad, (int) carrerasCount);
    }
}
```

### Caso de Uso: Cambiar Decano

**Flujo**:
1. Controller recibe `CambiarDecanoCommand`
2. Service busca la facultad
3. Service invoca método de dominio `cambiarDecano()`
4. Service persiste el cambio
5. Service retorna confirmación

**Implementación**:
```java
@Service
@Transactional
public class CambiarDecanoService implements CambiarDecanoUseCase {

    private final FacultadRepositoryPort facultadRepository;

    @Override
    public OperationResponse cambiarDecano(CambiarDecanoCommand command) {
        // 1. Buscar facultad
        FacultadId facultadId = FacultadId.of(command.facultadId());
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(command.facultadId()));

        // 2. Obtener decano anterior (para mensaje)
        String decanoAnterior = facultad.getDecano();

        // 3. Cambiar decano (método de dominio con validaciones)
        facultad.cambiarDecano(command.nuevoDecano());

        // 4. Persistir
        facultadRepository.save(facultad);

        // 5. Retornar confirmación
        String mensaje = String.format(
                "Decano cambiado exitosamente en '%s'. Anterior: '%s', Nuevo: '%s'",
                facultad.getNombre().getValue(),
                decanoAnterior,
                command.nuevoDecano()
        );

        return OperationResponse.success(mensaje, facultadId.getValue());
    }
}
```

### Caso de Uso: Desactivar Facultad

**Flujo**:
1. Controller envía ID de facultad
2. Service busca la facultad
3. Service verifica con Domain Service que no tenga carreras activas
4. Service invoca método de dominio `desactivar()`
5. Service persiste el cambio
6. Service retorna confirmación

**Implementación**:
```java
@Service
@Transactional
public class ManageFacultadStatusService implements ManageFacultadStatusUseCase {

    private final FacultadRepositoryPort facultadRepository;
    private final CarreraDomainService carreraDomainService;

    @Override
    public OperationResponse deactivate(Long facultadId) {
        // 1. Buscar facultad
        FacultadId id = FacultadId.of(facultadId);
        Facultad facultad = facultadRepository.findById(id)
                .orElseThrow(() -> new FacultadNotFoundException(facultadId));

        // 2. Validar con Domain Service
        if (!carreraDomainService.puedeDesactivarFacultad(id)) {
            throw new IllegalStateException(
                    "No se puede desactivar la facultad porque tiene carreras activas"
            );
        }

        // 3. Desactivar (método de dominio)
        facultad.desactivar();

        // 4. Persistir
        facultadRepository.save(facultad);

        // 5. Retornar confirmación
        return OperationResponse.success(
                "Facultad desactivada exitosamente: " + facultad.getNombre().getValue(),
                facultadId
        );
    }
}
```

---

## MANEJO DE ERRORES

### Tipos de Excepciones

**Domain Exceptions (del Domain Layer)**:
```java
// Lanzadas por el dominio cuando se violan reglas de negocio
public class FacultadNotFoundException extends DomainException {
    public FacultadNotFoundException(Long id) {
        super("Facultad no encontrada con ID: " + id);
    }
}

public class InvalidNombreAcademicoException extends DomainException {
    public InvalidNombreAcademicoException(String message) {
        super(message);
    }
}
```

**Application Exceptions (de la Application Layer)**:
```java
// Lanzadas por el service cuando fallan validaciones de aplicación
throw new IllegalArgumentException(
    "Ya existe una facultad con el nombre: " + nombre.getValue()
);

throw new IllegalStateException(
    "No se puede desactivar la facultad porque tiene carreras activas"
);
```

### Propagación de Excepciones

```
Domain Layer
    ↓ lanza DomainException
Application Service
    ↓ propaga (sin catch)
Controller
    ↓ capturada por
Exception Handler (Global)
    ↓ convierte a
HTTP Response (con status code apropiado)
```

### Exception Handler (en Presentation Layer)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FacultadNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFacultadNotFound(
            FacultadNotFoundException ex) {
        
        ErrorResponse error = new ErrorResponse(
            "FACULTAD_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex) {
        
        ErrorResponse error = new ErrorResponse(
            "INVALID_REQUEST",
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
            IllegalStateException ex) {
        
        ErrorResponse error = new ErrorResponse(
            "INVALID_STATE",
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}
```

---

## CONCLUSIÓN

### Resumen de la Application Layer

La Application Layer es el corazón de la orquestación en Clean Architecture:

**Responsabilidades**:
- Implementar casos de uso como Services
- Coordinar entre Domain, Mappers y Repositories
- Gestionar transacciones
- Validar reglas de aplicación (unicidad, existencia)
- Traducir entre DTOs y Domain Entities

**No hace**:
- NO contiene lógica de negocio (está en Domain)
- NO conoce detalles de JPA/MongoDB (está en Infrastructure)
- NO maneja HTTP/JSON directamente (está en Presentation)

### Ventajas de esta Arquitectura

**Testabilidad**:
```java
// Test sin Spring, usando mocks
@Test
void shouldRegisterFacultad() {
    FacultadRepositoryPort mockRepo = mock(FacultadRepositoryPort.class);
    RegisterFacultadService service = new RegisterFacultadService(mockRepo);
    
    FacultadResponse response = service.register(command);
    
    verify(mockRepo).save(any(Facultad.class));
}
```

**Mantenibilidad**:
- Cada Service tiene una única responsabilidad
- Los cambios en Infrastructure no afectan Application
- Fácil de entender: flujo lineal de pasos

**Escalabilidad**:
- CQRS permite escalar lecturas y escrituras independientemente
- Ports permiten múltiples implementaciones (cache, réplicas, etc.)
- Services stateless: fácil de escalar horizontalmente

**Flexibilidad**:
- Cambiar de JPA a MongoDB: solo cambiar Infrastructure
- Agregar GraphQL: reutilizar Use Cases existentes
- Migrar a microservicios: Services son unidades independientes

---

## REFERENCIAS

**Patrones Arquitectónicos**:
- Clean Architecture (Robert C. Martin)
- Hexagonal Architecture (Alistair Cockburn)
- Domain-Driven Design (Eric Evans)

**Patrones de Diseño**:
- Repository Pattern
- Dependency Inversion Principle
- Command Query Responsibility Segregation (CQRS)
- Mapper Pattern
- Transaction Script

**Tecnologías**:
- Spring Framework 6.x
- Spring Boot 3.x
- Java 17+

---

**Versión**: 1.0.0
**Fecha**: 28 Octubre 2025
**Estado**: Documentación Completa