package com.diego.almacen.dto.productos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

// Datos que la API devuelve al cliente sobre un producto
@Schema(description = "Información de un producto")
public record ProductoResponse(

        // ID generado para identificar el producto
        @Schema(description = "ID único del producto", example = "1")
        Long id,

        // Nombre del producto
        @Schema(description = "Nombre del producto", example = "Laptop Gamer")
        String nombre,

        // Categoría del producto
        @Schema(description = "Categoría del producto", example = "Electrónica")
        String categoria,

        // Precio del producto
        @Schema(description = "Precio del producto", example = "15999.99")
        BigDecimal precio,

        // Cantidad disponible
        @Schema(description = "Cantidad disponible del producto", example = "300")
        Integer cantidad
) {
}