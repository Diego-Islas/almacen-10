package com.diego.almacen.services.ventas;

import com.diego.almacen.dto.ventas.VentaRequest;
import com.diego.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentaService {

    List<VentaResponse> listar();

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar(VentaRequest request);

    VentaResponse cancelar(Long id);
}
