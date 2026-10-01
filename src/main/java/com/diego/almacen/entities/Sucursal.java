package com.diego.almacen.entities;

import com.diego.almacen.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SUCURSALES")
@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUCURSAL")
    private Long id;

    @Column(name = "NOMBRE", length = 50, unique = true, nullable = false)
    private String nombre;

    @Column(name = "DIRECCION", length = 150, nullable = false)
    private String direccion;

    public static void validarDatos(String nombre, String direccion) {

        StringCustomUtils.validarTamanio(nombre, 5, 50,
                "El nombre es requerido y debe tener entre 5 y 50 caracteres");

        StringCustomUtils.validarTamanio(direccion, 10, 150,
                "La dirección es requerida y debe tener entre 10 y 150 caracteres");
    }

    public void actualizar(String nombre, String direccion) {

        validarDatos(nombre, direccion);

        this.nombre = nombre.trim();
        this.direccion = direccion.trim();
    }

    public static Sucursal crear(String nombre, String direccion) {

        validarDatos(nombre, direccion);

        return Sucursal.builder()
                .nombre(nombre.trim())
                .direccion(direccion.trim())
                .build();
    }
}
