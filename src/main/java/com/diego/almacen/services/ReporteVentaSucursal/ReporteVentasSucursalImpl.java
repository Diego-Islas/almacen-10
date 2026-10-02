package com.diego.almacen.services.ReporteVentaSucursal;

import com.diego.almacen.dto.reporte.ReporteVentasSucursalResponse;

import com.diego.almacen.repositories.ReporteVentaSucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReporteVentasSucursalImpl implements ReporteVentasSucursalService {

    private final ReporteVentaSucursalRepository reporteVentaSucursalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> obtenerReportePorSucursal() {
        log.info("Obteniendo reporte detallado con la ventas de sucursales...");
        return reporteVentaSucursalRepository.obtenerReportePorSucursal();
    }
}
