package com.diego.almacen.controllers;

import com.diego.almacen.docs.ProblemaDoc;
import com.diego.almacen.dto.sucursales.SucursalRequest;
import com.diego.almacen.dto.sucursales.SucursalResponse;
import com.diego.almacen.services.sucursales.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
@Tag(name = "Sucursales", description = "Gestión de Sucursales")
// Errores que pueden ocurrir en cualquier endpoint:
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class SucursalController {

    public final SucursalService sucursalService;

    @GetMapping
    @Operation(summary = "Listar sucursales", description = "Obtiene todas las sucursales registradas.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<SucursalResponse>> listar() {
        return ResponseEntity.ok(sucursalService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID")
    @ApiResponse(responseCode = "200", description = "Sucursal encontrada")
    @ApiResponse(responseCode = "404", description = "La sucursal no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<SucursalResponse> obtenerPorId(
            @Parameter(description = "Id de la sucursal", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(sucursalService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva sucursal")
    @ApiResponse(responseCode = "201", description = "Sucursal creada")
    @ApiResponse(responseCode = "409", description = "Conflicto con datos de la sucursal",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<SucursalResponse> registrar(
            @Valid @RequestBody SucursalRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sucursalService.registrar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sucursal existente")
    @ApiResponse(responseCode = "200", description = "Sucursal actualizada")
    @ApiResponse(responseCode = "404", description = "La sucursal no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto con nuevos datos de la sucursal",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<SucursalResponse> actualizar(
            @Parameter(description = "Id de la sucursal", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @Valid @RequestBody SucursalRequest request
    ) {
        return ResponseEntity.ok(sucursalService.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sucursal")
    @ApiResponse(responseCode = "204", description = "Sucursal eliminada")
    @ApiResponse(responseCode = "404", description = "La sucursal no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "La sucursal está en uso y no puede eliminarse",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Id de la sucursal", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}