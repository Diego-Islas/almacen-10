package com.diego.almacen.mappers;

import com.diego.almacen.dto.productos.ProductoRequest;
import com.diego.almacen.dto.productos.ProductoResponse;
import com.diego.almacen.entities.Producto;
import com.diego.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

// Convierte objetos entre DTOs y la Entity Producto
@Component
public class ProductoMapper {

    // Convierte el Request recibido por la API en una Entity
    public Producto requestAEntidad(
            ProductoRequest request,
            Categoria categoria) {

        return request == null
                ? null
                : Producto.crear(
                request.nombre(),
                categoria,
                request.precio(),
                request.cantidad());
    }

    // Convierte una Entity en el Response que devuelve la API
    public ProductoResponse entidadAResponse(Producto producto) {

        return producto == null
                ? null
                : new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getDescripcion(),
                producto.getPrecio(),
                producto.getCantidad());
    }
}