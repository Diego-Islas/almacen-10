package com.diego.almacen.repositories;

import com.diego.almacen.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Permite acceder a la tabla SUCURSALES
@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    // Verifica si ya existe una sucursal con ese nombre, ignorando mayúsculas/minúsculas
    boolean existsByNombreIgnoreCase(String nombre);

    // Verifica si existe otra sucursal con ese nombre,
    // excluyendo la sucursal que tiene el ID indicado
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}