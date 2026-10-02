package com.diego.almacen.controllers;

import com.diego.almacen.docs.ProblemaDoc;
import com.diego.almacen.dto.reporte.ReporteVentasSucursalResponse;
import com.diego.almacen.services.ReporteVentaSucursal.ReporteVentasSucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes-ventas")
@RequiredArgsConstructor
@Tag(name = "Reportes Ventas Sucursales", description = "Gestion de reportes de ventas de las sucursales")
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class ReporteVentaSucursalController {
    private final ReporteVentasSucursalService reporteVentasSucursalService;

    @GetMapping
    @Operation(summary = "Obtener reporte financiero por sucursal")
    @ApiResponse(responseCode = "200", description = "Reporte generado correctamente")
    public ResponseEntity<List<ReporteVentasSucursalResponse>> obtenerReporte() {

        return ResponseEntity.ok(reporteVentasSucursalService.obtenerReportePorSucursal());
    }
}
