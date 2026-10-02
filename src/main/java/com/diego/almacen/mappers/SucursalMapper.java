package com.diego.almacen.mappers;

import com.diego.almacen.dto.sucursales.SucursalRequest;
import com.diego.almacen.dto.sucursales.SucursalResponse;
import com.diego.almacen.entities.Sucursal;
import org.springframework.stereotype.Component;

// Convierte entre DTOs y la Entity Sucursal
@Component
public class SucursalMapper {

    // Convierte el Request en una Entity
    public Sucursal requestAEntidad(SucursalRequest request) {

        return request == null
                ? null
                : Sucursal.crear(
                request.nombre(),
                request.direccion());
    }

    // Convierte la Entity en el Response
    public SucursalResponse entidadAResponse(Sucursal sucursal) {

        return sucursal == null
                ? null
                : new SucursalResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion());
    }
}