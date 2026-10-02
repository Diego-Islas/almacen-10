package com.diego.almacen.dto.reporte;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ReporteVentasSucursalResponse(
        @Schema(description = "Identificador de la sucursal", example = "1")
        Long idSucursal,

        @Schema(description = "Nombre de la sucursal", example = "Sucursal Norte")
        String nombreSucursal,

        @Schema(description = "Sumatoria económica de todas las ventas activas, sin contemplar ventas canceladas", example = "1")
        BigDecimal totalFacturado,

        @Schema(description = "Cantidad de Productos Vendidos:", example = "1")
        Long CantidadProductosVendidos
) {
}