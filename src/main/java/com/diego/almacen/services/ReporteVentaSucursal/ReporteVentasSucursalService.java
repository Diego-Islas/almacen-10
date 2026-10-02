package com.diego.almacen.services.ReporteVentaSucursal;

import com.diego.almacen.dto.reporte.ReporteVentasSucursalResponse;

import java.util.List;

public interface ReporteVentasSucursalService {
    List<ReporteVentasSucursalResponse> obtenerReportePorSucursal();
}
