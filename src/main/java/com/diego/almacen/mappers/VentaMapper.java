package com.diego.almacen.mappers;

import com.diego.almacen.dto.ventas.DetalleVentaResponse;
import com.diego.almacen.dto.ventas.VentaResponse;
import com.diego.almacen.entities.DetalleVenta;
import com.diego.almacen.entities.Venta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

// Convierte entidades de venta en DTO de respuesta.
@Component
public class VentaMapper {

    // Mapper utilizado para convertir la sucursal.
    private final SucursalMapper sucursalMapper;

    public VentaMapper(SucursalMapper sucursalMapper) {
        this.sucursalMapper = sucursalMapper;
    }

    // Convierte una Venta Entity en VentaResponse.
    public VentaResponse entidadAResponse(Venta venta) {

        // Convierte cada detalle de la venta a su DTO.
        List<DetalleVentaResponse> detalles = venta.getDetalleVentas()
                .stream()
                .map(this::detalleAResponse)
                .toList();

        // Suma los subtotales para obtener el total de la venta.
        BigDecimal total = detalles.stream()
                .map(DetalleVentaResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Construye la respuesta de la venta.
        return new VentaResponse(
                venta.getId(),
                venta.getFecha().toString(),
                venta.getEstadoVenta().getDescripcion(),
                sucursalMapper.entidadAResponse(venta.getSucursal()),
                detalles,
                total
        );
    }

    // Convierte un DetalleVenta Entity en DetalleVentaResponse.
    private DetalleVentaResponse detalleAResponse(DetalleVenta detalle) {

        // Calcula el subtotal: precio × cantidad.
        BigDecimal subtotal = detalle.getPrecioProducto()
                .multiply(
                        BigDecimal.valueOf(
                                detalle.getCantidadProducto()
                        )
                );

        // Construye la respuesta del detalle.
        return new DetalleVentaResponse(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidadProducto(),
                detalle.getPrecioProducto(),
                subtotal
        );
    }
}