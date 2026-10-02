package com.diego.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// Datos de un producto que forma parte de una venta
@Schema(description = "Detalle de un producto de la venta")
public record DetalleVentaRequest(

        // ID del producto que se quiere vender
        @Schema(description = "ID del producto", example = "1")
        @NotNull(message = "El ID del producto es requerido")
        @Positive(message = "El ID del producto debe ser positivo")
        Long idProducto,

        // Cantidad de unidades que se venden
        @Schema(description = "Cantidad del producto", example = "100")
        @NotNull(message = "La cantidad del producto es requerida")
        @Positive(message = "La cantidad del producto debe ser positiva")
        Integer cantidadProducto
) {
}