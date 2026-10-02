package com.diego.almacen.services.productos;

import com.diego.almacen.dto.productos.ProductoRequest;
import com.diego.almacen.dto.productos.ProductoResponse;
import com.diego.almacen.entities.Producto;
import com.diego.almacen.enums.Categoria;
import com.diego.almacen.exceptions.RecursoNoEncontradoException;
import com.diego.almacen.mappers.ProductoMapper;
import com.diego.almacen.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

// Contiene la lógica de negocio relacionada con productos
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    // Acceso a la base de datos
    private final ProductoRepository productoRepository;

    // Conversión entre DTOs y Entity
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(
            String nombre,
            String categoria,
            BigDecimal precioMin,
            BigDecimal precioMax) {

        log.info("Listando productos con filtros");

        // Si no se envía categoría, se manda null.
        // Si se envía, se convierte de String a enum Categoria.
        Categoria categoriaEnum = (categoria == null || categoria.isBlank())
                ? null
                : Categoria.obtenerCategoriaPorDescripcion(categoria.trim());

        // Busca en la base de datos aplicando los filtros enviados.
        // Los filtros que sean null se ignoran.
        return productoRepository.buscarConFiltros(
                        nombre,
                        categoriaEnum,
                        precioMin,
                        precioMax
                )
                // Convierte cada Producto en ProductoResponse.
                .stream()
                .map(productoMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {

        // Busca la Entity y después la convierte a Response
        return productoMapper.entidadAResponse(
                obtenerProductoOException(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {

        log.info("Registrando nuevo producto...");

        // Convierte la categoría String del Request a enum Categoria
        // y después convierte el Request en Entity
        Producto producto = productoMapper.requestAEntidad(
                request,
                Categoria.obtenerCategoriaPorDescripcion(
                        request.categoria().trim())
        );

        // Guarda el producto en la base de datos
        productoRepository.save(producto);

        log.info("Nuevo producto {} registrado", producto.getNombre());

        // Convierte la Entity guardada en Response
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {

        // Primero verifica que el producto exista
        Producto producto = obtenerProductoOException(id);

        log.info("Actualizando producto con id {}", id);

        // Actualiza la Entity aplicando sus propias validaciones
        producto.actualizar(
                request.nombre(),
                Categoria.obtenerCategoriaPorDescripcion(
                        request.categoria().trim()),
                request.precio(),
                request.cantidad());

        // Guarda los cambios inmediatamente
        productoRepository.saveAndFlush(producto);

        log.info("Producto con id {} actualizado correctamente", id);

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {

        // Verifica que el producto exista
        Producto producto = obtenerProductoOException(id);

        log.info("Eliminando producto con id {}", id);

        // Elimina el producto
        productoRepository.delete(producto);
        productoRepository.flush();

        log.info("Producto con id {} eliminado correctamente", id);
    }

    // Busca un producto o lanza una excepción 404
    private Producto obtenerProductoOException(Long id) {

        log.info("Buscando producto con id {}", id);

        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id));
    }
}