package com.christian.almacen.mappers;

import com.christian.almacen.dto.productos.ProductoRequest;
import com.christian.almacen.dto.productos.ProductoResponse;
import com.christian.almacen.entities.Producto;
import com.christian.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto requestAEntidad(ProductoRequest request, Categoria categoria) {

        return request == null
                ? null
                : Producto.crear(
                        request.nombre(),
                        categoria,
                        request.precio(),
                        request.cantidad());
    }

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
