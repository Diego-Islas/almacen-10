package com.diego.almacen.entities;

import com.diego.almacen.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Representa la tabla DETALLES_VENTAS de la base de datos
@Entity
@Table(name = "DETALLES_VENTAS")
@AllArgsConstructor // Constructor con todos los campos
@NoArgsConstructor  // Constructor vacío requerido por JPA
@Builder            // Permite crear objetos usando el patrón Builder
@Getter             // Genera getters automáticamente
public class DetalleVenta {

    // ID único del detalle
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_VENTA")
    private Long id;

    // Cada detalle pertenece a una venta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENTA", nullable = false)
    private Venta venta;

    // Cada detalle corresponde a un producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    private Producto producto;

    // Cantidad de unidades vendidas
    @Column(name = "CANTIDAD_PRODUCTO", nullable = false)
    private Integer cantidadProducto;

    // Precio del producto al momento de la venta
    @Column(name = "PRECIO_PRODUCTO", nullable = false)
    private BigDecimal precioProducto;

    // Asigna la venta a este detalle
    public void asignarVenta(Venta venta) {

        // La venta es obligatoria
        if (venta == null)
            throw new DatoInvalidoException("La venta es requerida");

        this.venta = venta;
    }
}