package com.diego.almacen.dto.ventas;

import com.diego.almacen.dto.sucursales.SucursalResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

// Datos que la API devuelve sobre una venta
@Schema(description = "Datos de una venta")
public record VentaResponse(

        // ID de la venta
        @Schema(description = "Identificador de la venta", example = "1")
        Long id,

        // Fecha en que se realizó la venta
        @Schema(description = "Fecha de la venta", example = "02/09/2026")
        String fecha,

        // Estado actual de la venta
        @Schema(description = "Estado de la venta", example = "Registrada")
        String estado,

        // Sucursal donde se realizó la venta
        @Schema(description = "Sucursal donde se realizó la venta")
        SucursalResponse sucursal,

        // Productos que forman parte de la venta
        @Schema(description = "Lista de productos de la venta")
        List<DetalleVentaResponse> detalles,

        // Total de todos los productos de la venta
        @Schema(description = "Total de la venta", example = "1500.00")
        BigDecimal total
) {
}