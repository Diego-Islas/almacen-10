package com.diego.almacen.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Indica que esta clase contiene configuraciones de Spring.
@Configuration
public class CorsConfig {

    // Registra el configurador para que Spring Boot pueda utilizarlo.
    @Bean
    public WebMvcConfigurer corsConfigurer() {

        // Permite personalizar la configuración de las peticiones web.
        return new WebMvcConfigurer() {

            // Define las reglas CORS para las peticiones del frontend.
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                // Aplica estas reglas a todos los endpoints de la API.
                registry.addMapping("/**")

                        // Permite peticiones desde el frontend Angular.
                        .allowedOrigins("http://localhost:4200")

                        // Permite utilizar estos métodos HTTP.
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                        // Permite enviar cualquier encabezado HTTP.
                        .allowedHeaders("*")

                        // Permite enviar cookies y otras credentials
                        .allowCredentials(true);
            }
        };
    }
}