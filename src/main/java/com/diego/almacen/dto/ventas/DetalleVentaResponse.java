package com.diego.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

// Información de un producto que forma parte de una venta
@Schema(description = "Detalle de un producto dentro de una venta")
public record DetalleVentaResponse(

        // Identifica qué producto se vendió
        @Schema(description = "ID del producto", example = "1")
        Long idProducto,

        // Nombre del producto vendido
        @Schema(description = "Nombre del producto", example = "Laptop Gamer")
        String nombreProducto,

        // Número de unidades vendidas
        @Schema(description = "Cantidad del unidades vendidas", example = "10")
        Integer cantidadProducto,

        // Precio de una unidad del producto
        @Schema(description = "Precio unitario del producto", example = "1500.00")
        BigDecimal precioProducto,

        // Precio unitario × cantidad
        @Schema(description = "Subtotal del producto", example = "15000.00")
        BigDecimal subtotal
) {
}