package com.university.cleanarchitecture.infrastructure.adapter.in.web;

import com.university.cleanarchitecture.application.dto.command.CambiarDecanoCommand;
import com.university.cleanarchitecture.application.dto.command.RegisterFacultadCommand;
import com.university.cleanarchitecture.application.dto.command.UpdateFacultadCommand;
import com.university.cleanarchitecture.application.dto.query.FindAllFacultadesQuery;
import com.university.cleanarchitecture.application.dto.query.FindFacultadByIdQuery;
import com.university.cleanarchitecture.application.dto.query.FindFacultadByNombreQuery;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.dto.response.FacultadSummaryResponse;
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
@RequestMapping("/api/v1/facultades")
@Tag(name = "Facultades", description = "Gestión de facultades universitarias")
@CrossOrigin(origins = "*")
public class FacultadController {

    private final RegisterFacultadUseCase registerFacultadUseCase;
    private final UpdateFacultadUseCase updateFacultadUseCase;
    private final FindFacultadByIdUseCase findFacultadByIdUseCase;
    private final FindAllFacultadesUseCase findAllFacultadesUseCase;
    private final FindFacultadByNombreUseCase findFacultadByNombreUseCase;
    private final ActivateFacultadUseCase activateFacultadUseCase;
    private final DeactivateFacultadUseCase deactivateFacultadUseCase;
    private final CambiarDecanoUseCase cambiarDecanoUseCase;

    public FacultadController(RegisterFacultadUseCase registerFacultadUseCase,
                              UpdateFacultadUseCase updateFacultadUseCase,
                              FindFacultadByIdUseCase findFacultadByIdUseCase,
                              FindAllFacultadesUseCase findAllFacultadesUseCase,
                              FindFacultadByNombreUseCase findFacultadByNombreUseCase,
                              ActivateFacultadUseCase activateFacultadUseCase,
                              DeactivateFacultadUseCase deactivateFacultadUseCase,
                              CambiarDecanoUseCase cambiarDecanoUseCase) {
        this.registerFacultadUseCase = registerFacultadUseCase;
        this.updateFacultadUseCase = updateFacultadUseCase;
        this.findFacultadByIdUseCase = findFacultadByIdUseCase;
        this.findAllFacultadesUseCase = findAllFacultadesUseCase;
        this.findFacultadByNombreUseCase = findFacultadByNombreUseCase;
        this.activateFacultadUseCase = activateFacultadUseCase;
        this.deactivateFacultadUseCase = deactivateFacultadUseCase;
        this.cambiarDecanoUseCase = cambiarDecanoUseCase;
    }

    @PostMapping
    @Operation(summary = "Registrar nueva facultad", description = "Registra una nueva facultad en el sistema")
    @ApiResponse(responseCode = "201", description = "Facultad registrada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<FacultadResponse> registerFacultad(
            @Valid @RequestBody RegisterFacultadCommand command) {
        FacultadResponse response = registerFacultadUseCase.register(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las facultades", description = "Obtiene una lista de todas las facultades")
    @ApiResponse(responseCode = "200", description = "Lista de facultades encontradas")
    public ResponseEntity<List<FacultadSummaryResponse>> getAllFacultades(
            @RequestParam(required = false) Boolean activo) {
        FindAllFacultadesQuery query = new FindAllFacultadesQuery(activo);
        List<FacultadSummaryResponse> facultades = findAllFacultadesUseCase.findAll(query);
        return ResponseEntity.ok(facultades);
    }

    @GetMapping("/{facultadId}")
    @Operation(summary = "Obtener facultad por ID", description = "Obtiene los detalles de una facultad específica")
    @ApiResponse(responseCode = "200", description = "Facultad encontrada")
    @ApiResponse(responseCode = "404", description = "Facultad no encontrada")
    public ResponseEntity<FacultadResponse> getFacultadById(@PathVariable Long facultadId) {
        FindFacultadByIdQuery query = new FindFacultadByIdQuery(facultadId);
        FacultadResponse response = findFacultadByIdUseCase.findById(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar facultad por nombre", description = "Busca una facultad por su nombre")
    @ApiResponse(responseCode = "200", description = "Facultad encontrada")
    @ApiResponse(responseCode = "404", description = "Facultad no encontrada")
    public ResponseEntity<FacultadResponse> getFacultadByNombre(@RequestParam String nombre) {
        FindFacultadByNombreQuery query = new FindFacultadByNombreQuery(nombre);
        FacultadResponse response = findFacultadByNombreUseCase.findByNombre(query);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{facultadId}")
    @Operation(summary = "Actualizar facultad", description = "Actualiza la información de una facultad existente")
    @ApiResponse(responseCode = "200", description = "Facultad actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Facultad no encontrada")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<FacultadResponse> updateFacultad(
            @PathVariable Long facultadId,
            @Valid @RequestBody UpdateFacultadCommand command) {
        // Aseguramos que el ID del path coincida con el del comando
        UpdateFacultadCommand updatedCommand = new UpdateFacultadCommand(
                facultadId,
                command.getNombre(),
                command.getDescripcion(),
                command.getUbicacion()
        );
        FacultadResponse response = updateFacultadUseCase.update(updatedCommand);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{facultadId}/decano")
    @Operation(summary = "Cambiar decano", description = "Cambia el decano de una facultad")
    @ApiResponse(responseCode = "200", description = "Decano cambiado exitosamente")
    @ApiResponse(responseCode = "404", description = "Facultad no encontrada")
    public ResponseEntity<FacultadResponse> cambiarDecano(
            @PathVariable Long facultadId,
            @Valid @RequestBody CambiarDecanoCommand command) {
        CambiarDecanoCommand updatedCommand = new CambiarDecanoCommand(
                facultadId,
                command.getNuevoDecano()
        );
        FacultadResponse response = cambiarDecanoUseCase.cambiarDecano(updatedCommand);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{facultadId}/activar")
    @Operation(summary = "Activar facultad", description = "Activa una facultad previamente desactivada")
    @ApiResponse(responseCode = "200", description = "Facultad activada exitosamente")
    @ApiResponse(responseCode = "404", description = "Facultad no encontrada")
    public ResponseEntity<Void> activateFacultad(@PathVariable Long facultadId) {
        activateFacultadUseCase.activate(facultadId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{facultadId}/desactivar")
    @Operation(summary = "Desactivar facultad", description = "Desactiva una facultad existente")
    @ApiResponse(responseCode = "200", description = "Facultad desactivada exitosamente")
    @ApiResponse(responseCode = "404", description = "Facultad no encontrada")
    public ResponseEntity<Void> deactivateFacultad(@PathVariable Long facultadId) {
        deactivateFacultadUseCase.deactivate(facultadId);
        return ResponseEntity.ok().build();
    }
}

