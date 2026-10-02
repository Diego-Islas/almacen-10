package com.diego.almacen.entities;

import com.diego.almacen.enums.Categoria;
import com.diego.almacen.exceptions.DatoInvalidoException;
import com.diego.almacen.utils.StringCustomUtils;
import com.diego.almacen.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Representa la tabla PRODUCTOS
@Entity
@Table(name = "PRODUCTOS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Producto {

    // ID generado automáticamente por la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO")
    private Long id;

    // Nombre del producto
    @Column(name = "NOMBRE", nullable = false, length = 30)
    private String nombre;

    // Categoría almacenada como texto en la BD
    @Column(name = "CATEGORIA", nullable = false)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    // Precio del producto
    @Column(name = "PRECIO", nullable = false)
    private BigDecimal precio;

    // Stock disponible
    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    // Valida los datos antes de crear o actualizar un producto
    private static void validarDatos(String nombre, Categoria categoria,
                                     BigDecimal precio, Integer cantidad) {

        StringCustomUtils.validarTamanio(nombre, 5, 30,
                "El nombre es requerido y debe tener entre 5 y 30 caracteres");

        if (categoria == null)
            throw new DatoInvalidoException("La categoría es requerida");

        ValoresNumericosUtils.validarBigDecimalPositivo(precio,
                "El precio es requerido y debe ser positivo");

        ValoresNumericosUtils.validarEnteroPositivo(cantidad,
                "La cantidad es requerida y debe ser positiva");
    }

    // Actualiza los datos del producto después de validarlos
    public void actualizar(String nombre, Categoria categoria,
                           BigDecimal precio, Integer cantidad) {

        validarDatos(nombre, categoria, precio, cantidad);

        this.nombre = nombre.trim();
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Aumenta el stock disponible
    public void aumentarCantidad(int cantidad) {

        ValoresNumericosUtils.validarEnteroPositivo(
                cantidad,
                "La cantidad debe ser positiva");

        this.cantidad += cantidad;
    }

    // Disminuye el stock disponible
    public void descontarCantidad(int cantidad) {

        ValoresNumericosUtils.validarEnteroPositivo(
                cantidad,
                "La cantidad debe ser positiva");

        // No permite descontar más unidades de las disponibles
        if (cantidad > this.cantidad)
            throw new DatoInvalidoException(
                    "La cantidad debe ser menor " +
                            "o igual a la cantidad actual");

        this.cantidad -= cantidad;
    }

    // Crea un producto validando primero sus datos
    public static Producto crear(String nombre, Categoria categoria,
                                 BigDecimal precio, Integer cantidad) {

        validarDatos(nombre, categoria, precio, cantidad);

        return Producto.builder()
                .nombre(nombre.trim())
                .categoria(categoria)
                .precio(precio)
                .cantidad(cantidad)
                .build();
    }
}