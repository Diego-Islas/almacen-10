package com.diego.almacen.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Indica que esta clase contiene configuración de Spring
public class OpenApiConfig {

    @Bean // Registra este objeto como un componente administrado por Spring
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("API de Almacén") // Nombre que aparecerá en Swagger
                .version("1.0.0") // Versión de la API
                .description("API para la gestión del inventario de productos")); // Descripción de la API
    }
}