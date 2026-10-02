package com.diego.almacen.controllers;

import com.diego.almacen.docs.ProblemaDoc;
import com.diego.almacen.dto.ventas.VentaRequest;
import com.diego.almacen.dto.ventas.VentaResponse;
import com.diego.almacen.services.ventas.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Gestión de Ventas")

@ApiResponse(responseCode = "400", description = "Datos Inválidos", content = @Content(mediaType = "application/problem+json",
        schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    @Operation(summary = "Registrar una Venta")
    @ApiResponse(responseCode = "201", description = "Venta registrada")
    @ApiResponse(responseCode = "404", description = "Sucursal o Producto no encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> registrarVenta(@Valid @RequestBody VentaRequest ventaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ventaService.registrar(ventaRequest));
    }

    @DeleteMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una venta")
    @ApiResponse(responseCode = "200", description = "Venta cancelada")
    @ApiResponse(responseCode = "404", description = "La venta no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "La venta ya está cancelada",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> cancelarVenta(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(ventaService.cancelar(id));
    }

    @GetMapping
    @Operation(
            summary = "Consultar ventas por estado",
            description = "Filtro de listas Dinámico Registrada | Cancelada"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Listado obtenido con éxito"
    )
    @ApiResponse(
            responseCode = "409",
            description = "El estado de venta proporcionado no es válido",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class))
    )
    public ResponseEntity<List<VentaResponse>> listaDinamicoActivo(
            @Parameter(description = "Estado de la venta, valores: Registrada | Cancelada", example = "Registrada")
            @RequestParam(required = false) String estadoVenta
    ) {
        return ResponseEntity.ok(ventaService.listadoDinamico(estadoVenta));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una venta por ID")
    @ApiResponse(responseCode = "200", description = "Venta encontrada")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada o cancelada",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(ventaService.obtenerPorIdActiva(id));
    }
}
