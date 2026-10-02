package com.diego.almacen.repositories;

import com.diego.almacen.entities.Venta;
import com.diego.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Permite acceder a la tabla VENTAS
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Buscar ventas por su estado, ya que después necesitaremos REGISTRADA/CANCELADA
    // Spring Data JPA entiende automáticamente: WHERE ESTADO = ...?
    List<Venta> findByEstadoVenta(EstadoVenta estadoVenta);

    Optional<Venta> findByIdAndEstadoVenta(Long id, EstadoVenta estadoVenta);
}