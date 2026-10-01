package com.diego.almacen.utils;

import com.diego.almacen.entities.Producto;
import com.diego.almacen.entities.Sucursal;
import com.diego.almacen.enums.Categoria;
import com.diego.almacen.repositories.ProductoRepository;
import com.diego.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    private final SucursalRepository sucursalRepository;

    @Override
    public void run(String... args) throws Exception {

        if (productoRepository.count() == 0) {

            productoRepository.saveAll(List.of(

                    new Producto(null,
                            "Laptop Gamer",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(1500),
                            10),

                    new Producto(null,
                            "Mouse Inalámbrico",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(25),
                            50),

                    new Producto(null,
                            "Camiseta Deportiva",
                            Categoria.ROPA,
                            BigDecimal.valueOf(20),
                            100)
            ));
            log.info("Productos de prueba cargados correctamente");
        }

        if (sucursalRepository.count() == 0) {

            sucursalRepository.saveAll(List.of(

                    new Sucursal(null,
                            "Sucursal Central",
                            "Av. Principal 123"),

                    new Sucursal(null,
                            "Sucursal Norte",
                            "Calle Norte 456"),

                    new Sucursal(null,
                            "Sucursal Sur",
                            "Calle Sur 789")
            ));
            log.info("Sucursales de prueba cargadas correctamente");
        }
    }
}
