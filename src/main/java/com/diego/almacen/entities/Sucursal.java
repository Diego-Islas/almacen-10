package com.diego.almacen.entities;

import com.diego.almacen.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Representa la tabla SUCURSALES
@Entity
@Table(name = "SUCURSALES")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Sucursal {

    // ID generado automáticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUCURSAL")
    private Long id;

    // Nombre único y obligatorio de la sucursal
    @Column(name = "NOMBRE", length = 50, unique = true, nullable = false)
    private String nombre;

    // Dirección obligatoria de la sucursal
    @Column(name = "DIRECCION", length = 150, nullable = false)
    private String direccion;

    // Valida los datos antes de crear o actualizar
    public static void validarDatos(String nombre, String direccion) {

        StringCustomUtils.validarTamanio(nombre, 5, 50,
                "El nombre es requerido y debe tener entre 5 y 50 caracteres");

        StringCustomUtils.validarTamanio(direccion, 10, 150,
                "La dirección es requerida y debe tener entre 10 y 150 caracteres");
    }

    // Actualiza los datos de una sucursal existente
    public void actualizar(String nombre, String direccion) {

        validarDatos(nombre, direccion);

        this.nombre = nombre.trim();
        this.direccion = direccion.trim();
    }

    // Crea una nueva sucursal después de validar sus datos
    public static Sucursal crear(String nombre, String direccion) {

        validarDatos(nombre, direccion);

        return Sucursal.builder()
                .nombre(nombre.trim())
                .direccion(direccion.trim())
                .build();
    }
}