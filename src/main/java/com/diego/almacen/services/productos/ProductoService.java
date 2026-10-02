package com.diego.almacen.services.productos;

import com.diego.almacen.dto.productos.ProductoRequest;
import com.diego.almacen.dto.productos.ProductoResponse;

import java.math.BigDecimal;
import java.util.List;

// Define las operaciones disponibles para trabajar con productos
public interface ProductoService {

    // Lista de productos aplicando filtros opcionales
    List<ProductoResponse> listar(
            String nombre,
            String categoria,
            BigDecimal precioMin,
            BigDecimal precioMax);

    // Busca un producto por su ID
    ProductoResponse obtenerPorId(Long id);

    // Registra un nuevo producto
    ProductoResponse registrar(ProductoRequest request);

    // Actualiza un producto existente
    ProductoResponse actualizar(ProductoRequest request, Long id);

    // Elimina un producto
    void eliminar(Long id);
}