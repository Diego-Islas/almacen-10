package com.diego.almacen.repositories;

import com.diego.almacen.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    // SELECT COUNT(*) FROM SUCURSALES WHERE LOWER(NOMBRE) = LOWER(?);
    boolean existsByNombreIgnoreCase(String nombre);

    // SELECT COUNT(*) FROM SUCURSALES WHERE LOWER(NOMBRE) = LOWER(?) AND ID_SUCURSAL <> ?;
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
