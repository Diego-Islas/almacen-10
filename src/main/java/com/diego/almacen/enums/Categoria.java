package com.diego.almacen.enums;

import com.diego.almacen.exceptions.DatoInvalidoException;
import com.diego.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Define las categorías permitidas para los productos
@RequiredArgsConstructor // Genera constructor para los campos final
@Getter                 // Genera getDescripcion()
public enum Categoria {

    ALIMENTO("Alimento"),
    HIGIENE("Higiene"),
    JUGUETE("Juguete"),
    ELECTRONICA("Electrónica"),
    ROPA("Ropa"),
    ACCESORIO("Accesorio"),
    FARMACIA("Farmacia");

    // Texto descriptivo que se muestra al usuario
    private final String descripcion;

    // Busca una categoría a partir de su descripción
    public static Categoria obtenerCategoriaPorDescripcion(String descripcion) {

        // Verifica que la descripción tenga contenido
        StringCustomUtils.validarNoVacio(
                descripcion,
                "La descripción es requerida"
        );

        // Normaliza el texto para facilitar la comparación
        String descripcionNormalizada =
                StringCustomUtils.normalizarTexto(descripcion);

        // Recorre todas las categorías disponibles
        for (Categoria categoria : values()) {

            if (StringCustomUtils.normalizarTexto(categoria.descripcion)
                    .equalsIgnoreCase(descripcionNormalizada)) {

                return categoria;
            }
        }

        // La descripción no corresponde a ninguna categoría
        throw new DatoInvalidoException(
                "No existe una categoría con la descripción: " + descripcion
        );
    }
}