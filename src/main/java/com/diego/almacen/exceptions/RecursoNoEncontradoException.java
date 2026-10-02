package com.diego.almacen.exceptions;

// Excepción cuando no se encuentra un recurso solicitado
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}