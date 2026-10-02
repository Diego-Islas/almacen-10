package com.diego.almacen.services.ventas;

import com.diego.almacen.dto.ventas.VentaRequest;
import com.diego.almacen.dto.ventas.VentaResponse;
import com.diego.almacen.entities.DetalleVenta;
import com.diego.almacen.entities.Producto;
import com.diego.almacen.entities.Sucursal;
import com.diego.almacen.entities.Venta;
import com.diego.almacen.enums.EstadoVenta;
import com.diego.almacen.exceptions.RecursoNoEncontradoException;
import com.diego.almacen.mappers.VentaMapper;
import com.diego.almacen.repositories.ProductoRepository;
import com.diego.almacen.repositories.SucursalRepository;
import com.diego.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService {
    private final VentaRepository ventaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;
    private final VentaMapper ventaMapper;

    @Override
    public List<VentaResponse> listadoDinamico(String description) {
        log.info("Obteniendo Lista de ventas...");

        EstadoVenta estadoVenta = (description == null || description.isBlank())
                ? EstadoVenta.REGISTRADA
                : EstadoVenta.obtenerEstadoVentaPorDescripcion(description);

        return ventaRepository.findByEstadoVenta(estadoVenta).stream()
                .map(ventaMapper::entidadAResponse).toList();
    }

    @Override
    public VentaResponse obtenerPorIdActiva(Long id) {
        log.info("Obteniendo venta con id: {}", id);

        Venta venta = ventaRepository.findByIdAndEstadoVenta(id, EstadoVenta.REGISTRADA)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("No se encontró una venta registrada con el id: " + id + ". La venta puede no existir o encontrarse cancelada.")
                );

        log.info("Venta obtenida, con éxito!");

        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse registrar(VentaRequest request) {
        log.info("Registrando venta...");

        Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Sucursal no encontrada con id: " + request.idSucursal()));

        Venta venta = Venta.builder()
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .sucursal(sucursal)
                .build();

        request.productos().forEach(detalleVentaRequest -> {
            Producto producto = productoRepository.findById(detalleVentaRequest.idProducto())
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Producto no encontrado con id: " + detalleVentaRequest.idProducto())
                    );

            BigDecimal precioActual = producto.getPrecio();

            producto.descontarCantidad(
                    detalleVentaRequest.cantidadProducto()
            );

            DetalleVenta detalleVenta = DetalleVenta.builder()
                    .producto(producto)
                    .cantidadProducto(detalleVentaRequest.cantidadProducto())
                    .precioProducto(precioActual)
                    .build();

            venta.agregarDetalle(detalleVenta);
        });

        ventaRepository.save(venta);

        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse cancelar(Long id) {
        log.info("Cancelando venta...");
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Venta no encontrada con id: " + id));

        venta.cancelar();

        venta.getDetalleVentas()
                .forEach(detalleVenta ->
                        detalleVenta.getProducto().aumentarCantidad(detalleVenta.getCantidadProducto()));

        ventaRepository.save(venta);

        log.info("Venta Cancelada con éxito");

        return ventaMapper.entidadAResponse(venta);
    }

}
