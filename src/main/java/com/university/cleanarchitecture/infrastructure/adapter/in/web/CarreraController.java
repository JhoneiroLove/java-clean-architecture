package com.university.cleanarchitecture.infrastructure.adapter.in.web;

import com.university.cleanarchitecture.application.dto.command.RegisterCarreraCommand;
import com.university.cleanarchitecture.application.dto.command.UpdateCarreraCommand;
import com.university.cleanarchitecture.application.dto.query.FindCarreraByIdQuery;
import com.university.cleanarchitecture.application.dto.query.FindCarrerasByDuracionQuery;
import com.university.cleanarchitecture.application.dto.query.FindCarrerasByFacultadQuery;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;
import com.university.cleanarchitecture.application.dto.response.CarreraSummaryResponse;
import com.university.cleanarchitecture.application.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carreras")
@Tag(name = "Carreras", description = "Gestión de carreras universitarias")
@CrossOrigin(origins = "*")
public class CarreraController {

    private final RegisterCarreraUseCase registerCarreraUseCase;
    private final UpdateCarreraUseCase updateCarreraUseCase;
    private final FindCarreraByIdUseCase findCarreraByIdUseCase;
    private final FindCarrerasByFacultadUseCase findCarrerasByFacultadUseCase;
    private final FindCarrerasByDuracionUseCase findCarrerasByDuracionUseCase;
    private final ActivateCarreraUseCase activateCarreraUseCase;
    private final DeactivateCarreraUseCase deactivateCarreraUseCase;

    public CarreraController(RegisterCarreraUseCase registerCarreraUseCase,
                             UpdateCarreraUseCase updateCarreraUseCase,
                             FindCarreraByIdUseCase findCarreraByIdUseCase,
                             FindCarrerasByFacultadUseCase findCarrerasByFacultadUseCase,
                             FindCarrerasByDuracionUseCase findCarrerasByDuracionUseCase,
                             ActivateCarreraUseCase activateCarreraUseCase,
                             DeactivateCarreraUseCase deactivateCarreraUseCase) {
        this.registerCarreraUseCase = registerCarreraUseCase;
        this.updateCarreraUseCase = updateCarreraUseCase;
        this.findCarreraByIdUseCase = findCarreraByIdUseCase;
        this.findCarrerasByFacultadUseCase = findCarrerasByFacultadUseCase;
        this.findCarrerasByDuracionUseCase = findCarrerasByDuracionUseCase;
        this.activateCarreraUseCase = activateCarreraUseCase;
        this.deactivateCarreraUseCase = deactivateCarreraUseCase;
    }

    @PostMapping
    @Operation(summary = "Registrar nueva carrera", description = "Registra una nueva carrera en el sistema")
    @ApiResponse(responseCode = "201", description = "Carrera registrada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Facultad no encontrada")
    public ResponseEntity<CarreraResponse> registerCarrera(
            @Valid @RequestBody RegisterCarreraCommand command) {
        CarreraResponse response = registerCarreraUseCase.register(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{carreraId}")
    @Operation(summary = "Obtener carrera por ID", description = "Obtiene los detalles de una carrera específica")
    @ApiResponse(responseCode = "200", description = "Carrera encontrada")
    @ApiResponse(responseCode = "404", description = "Carrera no encontrada")
    public ResponseEntity<CarreraResponse> getCarreraById(@PathVariable Long carreraId) {
        FindCarreraByIdQuery query = new FindCarreraByIdQuery(carreraId);
        CarreraResponse response = findCarreraByIdUseCase.findById(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/facultad/{facultadId}")
    @Operation(summary = "Obtener carreras por facultad", description = "Obtiene todas las carreras de una facultad específica")
    @ApiResponse(responseCode = "200", description = "Lista de carreras encontradas")
    public ResponseEntity<List<CarreraSummaryResponse>> getCarrerasByFacultad(
            @PathVariable Long facultadId,
            @RequestParam(required = false) Boolean activo) {
        FindCarrerasByFacultadQuery query = new FindCarrerasByFacultadQuery(facultadId, activo);
        List<CarreraSummaryResponse> carreras = findCarrerasByFacultadUseCase.findByFacultad(query);
        return ResponseEntity.ok(carreras);
    }

    @GetMapping("/duracion/{duracion}")
    @Operation(summary = "Buscar carreras por duración", description = "Obtiene todas las carreras con una duración específica")
    @ApiResponse(responseCode = "200", description = "Lista de carreras encontradas")
    public ResponseEntity<List<CarreraSummaryResponse>> getCarrerasByDuracion(
            @PathVariable Integer duracion,
            @RequestParam(required = false) Boolean activo) {
        boolean soloActivas = activo != null ? activo : false;
        FindCarrerasByDuracionQuery query = new FindCarrerasByDuracionQuery(
                duracion, null, null, soloActivas);
        List<CarreraSummaryResponse> carreras = findCarrerasByDuracionUseCase.findByDuracion(query);
        return ResponseEntity.ok(carreras);
    }

    @PutMapping("/{carreraId}")
    @Operation(summary = "Actualizar carrera", description = "Actualiza la información de una carrera existente")
    @ApiResponse(responseCode = "200", description = "Carrera actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Carrera no encontrada")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<CarreraResponse> updateCarrera(
            @PathVariable Long carreraId,
            @Valid @RequestBody UpdateCarreraCommand command) {
        // Aseguramos que el ID del path coincida con el del comando
        UpdateCarreraCommand updatedCommand = new UpdateCarreraCommand(
                carreraId,
                command.getNombre(),
                command.getDescripcion(),
                command.getDuracionSemestres(),
                command.getTituloOtorgado()
        );
        CarreraResponse response = updateCarreraUseCase.update(updatedCommand);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{carreraId}/activar")
    @Operation(summary = "Activar carrera", description = "Activa una carrera previamente desactivada")
    @ApiResponse(responseCode = "200", description = "Carrera activada exitosamente")
    @ApiResponse(responseCode = "404", description = "Carrera no encontrada")
    public ResponseEntity<Void> activateCarrera(@PathVariable Long carreraId) {
        activateCarreraUseCase.activate(carreraId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{carreraId}/desactivar")
    @Operation(summary = "Desactivar carrera", description = "Desactiva una carrera existente")
    @ApiResponse(responseCode = "200", description = "Carrera desactivada exitosamente")
    @ApiResponse(responseCode = "404", description = "Carrera no encontrada")
    public ResponseEntity<Void> deactivateCarrera(@PathVariable Long carreraId) {
        deactivateCarreraUseCase.deactivate(carreraId);
        return ResponseEntity.ok().build();
    }
}

