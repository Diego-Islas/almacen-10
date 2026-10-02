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

        EstadoVenta estadoVenta = description == null
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

        Sucursal sucursal = obtenerSucursalOException(request.idSucursal());

        Venta venta = Venta.crearVenta(sucursal);

        request.productos().forEach(detalleProducto -> {
            Producto producto = obtenerProductoOException(detalleProducto.idProducto());
            DetalleVenta detalleVenta = DetalleVenta.crearVenta(producto, detalleProducto.cantidadProducto());
            venta.agregarDetalle(detalleVenta);
        });

        ventaRepository.save(venta);

        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse cancelar(Long id) {
        log.info("Cancelando venta...");

        Venta venta = obtenerVentaOException(id);

        venta.cancelar();

        log.info("Venta Cancelada con éxito");

        return ventaMapper.entidadAResponse(venta);
    }


    private Sucursal obtenerSucursalOException(Long id) {

        log.info("Buscando sucursal con id: {}", id);

        return sucursalRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: " + id));
    }

    private Producto obtenerProductoOException(Long id) {

        log.info("Buscando producto con id {}", id);

        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id));
    }

    private Venta obtenerVentaOException(Long id) {
        log.info("Obteniendo venta por id: {}", id);
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con id: " + id));
    }
}
