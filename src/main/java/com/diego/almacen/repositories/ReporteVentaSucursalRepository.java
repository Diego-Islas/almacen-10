package com.diego.almacen.repositories;

import com.diego.almacen.dto.reporte.ReporteVentasSucursalResponse;
import com.diego.almacen.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteVentaSucursalRepository extends JpaRepository<Venta, Long> {

    @Query("""
                SELECT new com.diego.almacen.dto.reporte.ReporteVentasSucursalResponse(
                    v.sucursal.id,
                    v.sucursal.nombre,
                    SUM(d.cantidadProducto * d.precioProducto),
                    SUM(d.cantidadProducto)
                )
                FROM Venta v
                JOIN v.detalleVentas d
                WHERE v.estadoVenta = com.diego.almacen.enums.EstadoVenta.REGISTRADA
                GROUP BY v.sucursal.id, v.sucursal.nombre
            """)
    List<ReporteVentasSucursalResponse> obtenerReportePorSucursal();
}
