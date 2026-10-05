package com.diego.almacen.controllers;

import com.diego.almacen.dto.productos.ProductoRequest;
import com.diego.almacen.dto.productos.ProductoResponse;
import com.diego.almacen.exceptions.RecursoNoEncontradoException;
import com.diego.almacen.services.productos.ProductoService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoService productoService;

    @Test
    void registrar_debeRetornar201_cuandoDatosSonValidos() throws Exception {
        ProductoRequest request = new ProductoRequest(
                "Mouse Inalámbrico", "Electrónica", new BigDecimal("299.99"), 50);
        ProductoResponse response = new ProductoResponse(
                1L, "Mouse Inalámbrico", "Electrónica",
                new BigDecimal("299.99"), 50);

        when(productoService.registrar(any(ProductoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Mouse Inalámbrico"));
    }

    @Test
    void registrar_debeRetornar400_cuandoElNombreEsMuyCorto() throws Exception {
        ProductoRequest request = new ProductoRequest(
                "Ab", "Electrónica", new BigDecimal("299.99"), 50);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(productoService, never()).registrar(any(ProductoRequest.class));
    }

    @Test
    void obtenerPorId_debeRetornar404_cuandoElProductoNoExiste() throws Exception {
        when(productoService.obtenerPorId(99L))
                .thenThrow(new RecursoNoEncontradoException(
                        "Producto no encontrado con id: 99"));

        mockMvc.perform(get("/api/productos/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail")
                        .value("Producto no encontrado con id: 99"));
    }

    @Test
    void obtenerPorId_debeRetornar400_cuandoElIdEsNegativo() throws Exception {
        mockMvc.perform(get("/api/productos/{id}", -1L))
                .andExpect(status().isBadRequest());

        verify(productoService, never()).obtenerPorId(any());
    }
}