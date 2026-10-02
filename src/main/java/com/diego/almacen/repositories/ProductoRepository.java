package com.diego.almacen.repositories;

import com.diego.almacen.entities.Producto;
import com.diego.almacen.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

// Permite acceder a la tabla PRODUCTOS mediante JPA
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // LOWER() convierte el texto a minúsculas.
    // LIKE permite buscar coincidencias parciales.
    // CONCAT('%', texto, '%') busca el texto en cualquier posición.
    // IS NULL permite ignorar el filtro cuando no se envía.
    // >= busca precios mayores o iguales al mínimo.
    // <= busca precios menores o iguales al máximo.

    // Busca productos aplicando únicamente los filtros enviados.
    @Query("""
                SELECT p
                FROM Producto p
                WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
                  AND (:categoria IS NULL OR p.categoria = :categoria)
                  AND (:precioMin IS NULL OR p.precio >= :precioMin)
                  AND (:precioMax IS NULL OR p.precio <= :precioMax)
            """)
    List<Producto> buscarConFiltros(
            @Param("nombre") String nombre,
            @Param("categoria") Categoria categoria,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );
}