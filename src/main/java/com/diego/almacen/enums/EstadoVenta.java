package com.diego.almacen.enums;

import com.diego.almacen.exceptions.DatoInvalidoException;
import com.diego.almacen.utils.StringCustomUtils;
import com.diego.almacen.utils.ValoresNumericosUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Genera el constructor de los campos final
@Getter                 // Genera los getters
public enum EstadoVenta {

    REGISTRADA(1L, "REGISTRADA"),
    CANCELADA(0L, "CANCELADA");

    // Código asociado al estado
    private final Long codigo;

    // Descripción que se muestra al usuario
    private final String descripcion;

    // Busca un estado utilizando su descripción
    public static EstadoVenta obtenerEstadoVentaPorDescripcion(String descripcion) {

        StringCustomUtils.validarNoVacio(
                descripcion,
                "La descripción es requerida"
        );

        // Normaliza el texto para facilitar la comparación
        String descripcionNormalizada =
                StringCustomUtils.normalizarTexto(descripcion);

        // Busca entre todos los estados disponibles
        for (EstadoVenta estadoVenta : values()) {

            if (StringCustomUtils.normalizarTexto(estadoVenta.descripcion)
                    .equalsIgnoreCase(descripcionNormalizada)) {

                return estadoVenta;
            }
        }

        throw new DatoInvalidoException(
                "No existe un estado de venta con la descripción: " + descripcion
        );
    }

    // Busca un estado utilizando su código
    public static EstadoVenta obtenerEstadoVentaPorCodigo(Long codigo) {

        ValoresNumericosUtils.validarNumeroRequerido(
                codigo,
                "El código es requerido"
        );

        // Busca entre todos los estados disponibles
        for (EstadoVenta estadoVenta : values()) {

            if (estadoVenta.codigo.equals(codigo)) {
                return estadoVenta;
            }
        }

        throw new DatoInvalidoException(
                "No existe un estado de venta con el código: " + codigo
        );
    }
}