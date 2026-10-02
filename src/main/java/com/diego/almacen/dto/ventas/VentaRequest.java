package com.diego.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

// Datos que recibimos para crear una venta
@Schema(description = "Datos necesarios para crear una venta")
public record VentaRequest(

        // Sucursal donde se realiza la venta
        @Schema(description = "ID de la sucursal", example = "1")
        @NotNull(message = "El ID de la sucursal es requerido")
        @Positive(message = "El ID de la sucursal debe ser positivo")
        Long idSucursal,

        // Productos que forman parte de la venta
        @Schema(description = "Lista de productos de la venta")
        @NotEmpty(message = "La lista de productos es requerida y no debe estar vacía")
        List<@Valid DetalleVentaRequest> productos
) {
}