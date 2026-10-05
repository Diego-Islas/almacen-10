package com.diego.almacen.services.sucursales;

import com.diego.almacen.dto.sucursales.SucursalRequest;
import com.diego.almacen.dto.sucursales.SucursalResponse;
import com.diego.almacen.entities.Sucursal;
import com.diego.almacen.exceptions.ConflictoException;
import com.diego.almacen.exceptions.RecursoNoEncontradoException;
import com.diego.almacen.mappers.SucursalMapper;
import com.diego.almacen.repositories.SucursalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SucursalServiceImplTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private SucursalMapper sucursalMapper;

    @InjectMocks
    private SucursalServiceImpl sucursalService;

    @Test
    void registrar_debeLanzarExcepcion_cuandoYaExisteUnaSucursalConEseNombre() {
        SucursalRequest request = new SucursalRequest(
                "Sucursal Norte", "Av. Siempre Viva 123");

        when(sucursalRepository.existsByNombreIgnoreCase("Sucursal Norte"))
                .thenReturn(true);

        assertThatThrownBy(() -> sucursalService.registrar(request))
                .isInstanceOf(ConflictoException.class)
                .hasMessageContaining(
                        "Ya existe una sucursal con el nombre de: Sucursal Norte");

        verify(sucursalRepository, never()).save(any());
    }

    @Test
    void registrar_debeGuardarSucursal_cuandoElNombreNoExistePreviamente() {
        SucursalRequest request = new SucursalRequest(
                "Sucursal Sur", "Calle Falsa 456, Springfield");
        Sucursal sucursal = Sucursal.builder()
                .nombre("Sucursal Sur")
                .direccion("Calle Falsa 456, Springfield")
                .build();
        SucursalResponse response = new SucursalResponse(
                2L, "Sucursal Sur", "Calle Falsa 456, Springfield");

        when(sucursalMapper.requestAEntidad(request)).thenReturn(sucursal);
        when(sucursalRepository.existsByNombreIgnoreCase("Sucursal Sur"))
                .thenReturn(false);
        when(sucursalMapper.entidadAResponse(sucursal)).thenReturn(response);

        SucursalResponse resultado = sucursalService.registrar(request);

        assertThat(resultado).isEqualTo(response);
        verify(sucursalRepository).save(sucursal);
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoElNuevoNombreYaLoUsaOtraSucursal() {
        Sucursal sucursalExistente = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Vieja")
                .direccion("Dirección Vieja 123")
                .build();

        when(sucursalRepository.findById(1L))
                .thenReturn(Optional.of(sucursalExistente));

        SucursalRequest request = new SucursalRequest(
                "Sucursal Norte", "Nueva Dirección 456");
        when(sucursalRepository.existsByNombreIgnoreCaseAndIdNot(
                "Sucursal Norte", 1L)).thenReturn(true);

        assertThatThrownBy(() -> sucursalService.actualizar(request, 1L))
                .isInstanceOf(ConflictoException.class)
                .hasMessageContaining(
                        "Ya existe una sucursal con el nombre de: Sucursal Norte");

        verify(sucursalRepository, never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_debeLanzarExcepcion_cuandoLaSucursalNoExiste() {
        when(sucursalRepository.findById(50L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sucursalService.obtenerPorId(50L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Sucursal no encontrada con id: 50");
    }
}