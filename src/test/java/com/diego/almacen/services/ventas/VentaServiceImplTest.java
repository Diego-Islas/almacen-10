package com.diego.almacen.services.ventas;

import com.diego.almacen.dto.ventas.DetalleVentaRequest;
import com.diego.almacen.dto.ventas.VentaRequest;
import com.diego.almacen.entities.Producto;
import com.diego.almacen.entities.Sucursal;
import com.diego.almacen.entities.Venta;
import com.diego.almacen.enums.Categoria;
import com.diego.almacen.enums.EstadoVenta;
import com.diego.almacen.exceptions.ConflictoException;
import com.diego.almacen.exceptions.DatoInvalidoException;
import com.diego.almacen.mappers.VentaMapper;
import com.diego.almacen.repositories.ProductoRepository;
import com.diego.almacen.repositories.SucursalRepository;
import com.diego.almacen.repositories.VentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private VentaMapper ventaMapper;

    @InjectMocks
    private VentaServiceImpl ventaService;

    @Test
    void registrar_debeLanzarExcepcion_cuandoNoHayStockSuficiente() {
        Sucursal sucursal = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Norte")
                .direccion("Av. Siempre Viva 123")
                .build();
        Producto producto = Producto.builder()
                .id(10L)
                .nombre("Mouse Gamer")
                .categoria(Categoria.ELECTRONICA)
                .precio(new BigDecimal("299.99"))
                .cantidad(3)
                .build();
        VentaRequest request = new VentaRequest(
                1L,
                List.of(new DetalleVentaRequest(10L, 5))
        );

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        assertThatThrownBy(() -> ventaService.registrar(request))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("La cantidad debe ser menor o igual a la cantidad actual");

        verify(ventaRepository, never()).save(any(Venta.class));
        assertThat(producto.getCantidad()).isEqualTo(3);
    }

    @Test
    void cancelar_debeLanzarExcepcion_cuandoLaVentaYaEstaCancelada() {
        Venta ventaCancelada = Venta.builder()
                .id(20L)
                .estadoVenta(EstadoVenta.CANCELADA)
                .build();
        when(ventaRepository.findById(20L))
                .thenReturn(Optional.of(ventaCancelada));

        assertThatThrownBy(() -> ventaService.cancelar(20L))
                .isInstanceOf(ConflictoException.class)
                .hasMessage("La venta ya está cancelada");

        verify(ventaMapper, never()).entidadAResponse(any(Venta.class));
    }
}
