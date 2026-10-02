package com.diego.almacen.controllers;

import com.diego.almacen.docs.ProblemaDoc;
import com.diego.almacen.dto.productos.ProductoRequest;
import com.diego.almacen.dto.productos.ProductoResponse;
import com.diego.almacen.services.productos.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController // Indica que esta clase maneja peticiones HTTP
@RequestMapping("/api/productos") // Ruta base de todos los endpoints
@RequiredArgsConstructor // Genera el constructor para las dependencias final
@Tag(name = "Productos", description = "Gestión del inventario de productos")

// Errores generales que pueden ocurrir en los endpoints
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class ProductoController {

    // Service que contiene la lógica de productos
    private final ProductoService productoService;

    @GetMapping // GET /api/productos
    @Operation(summary = "Listar productos", description = "Todos los filtros son opcionales")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<ProductoResponse>> listar(

            @Parameter(description = "Búsqueda por nombre", example = "Laptop")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "Filtro por categoría", example = "Electrónica")
            @RequestParam(required = false) String categoria,

            @Parameter(description = "Precio mínimo", example = "1000")
            @RequestParam(required = false) BigDecimal precioMin,

            @Parameter(description = "Precio máximo", example = "20000")
            @RequestParam(required = false) BigDecimal precioMax
    ) {
        // Envía los filtros al Service
        return ResponseEntity.ok(
                productoService.listar(nombre, categoria, precioMin, precioMax)
        );
    }

    @GetMapping("/{id}") // GET /api/productos/{id}
    @Operation(summary = "Obtener producto por ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "El producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ProductoResponse> obtenerPorId(

            @Parameter(description = "Id del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping // POST /api/productos
    @Operation(summary = "Registrar un nuevo producto")
    @ApiResponse(responseCode = "201", description = "Producto creado")
    @ApiResponse(responseCode = "409", description = "Conflicto con datos del producto",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ProductoResponse> registrar(
            @Valid @RequestBody ProductoRequest request // Valida y recibe el JSON
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.registrar(request));
    }

    @PutMapping("/{id}") // PUT /api/productos/{id}
    @Operation(summary = "Actualizar un producto existente")
    @ApiResponse(responseCode = "200", description = "Producto actualizado")
    @ApiResponse(responseCode = "404", description = "El producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto con nuevos datos del producto",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ProductoResponse> actualizar(

            @Parameter(description = "Id del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,

            @Valid @RequestBody ProductoRequest request
    ) {
        return ResponseEntity.ok(productoService.actualizar(request, id));
    }

    @DeleteMapping("/{id}") // DELETE /api/productos/{id}
    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    @ApiResponse(responseCode = "404", description = "El producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> eliminar(

            @Parameter(description = "Id del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        productoService.eliminar(id);

        // 204 = operación exitosa sin contenido
        return ResponseEntity.noContent().build();
    }
}