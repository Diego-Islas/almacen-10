package com.diego.almacen.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

// Maneja excepciones de todos los Controllers de la aplicación
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 404: el recurso solicitado no existe
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail handleNoEncontrado(RecursoNoEncontradoException e) {

        log.warn("No encontrado: {}", e.getMessage());

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }

    // 400: el dato enviado no es válido
    @ExceptionHandler(DatoInvalidoException.class)
    public ProblemDetail handleDatoInvalido(DatoInvalidoException e) {

        log.warn("Dato inválido: {}", e.getMessage());

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
    }

    // 409: la operación entra en conflicto con una regla de negocio
    @ExceptionHandler(ConflictoException.class)
    public ProblemDetail handleConflicto(ConflictoException e) {

        log.warn("Conflicto: {}", e.getMessage());

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                e.getMessage()
        );
    }

    // 409: la base de datos rechazó la operación
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleIntegridad(DataIntegrityViolationException e) {

        log.warn(
                "Integridad de datos: {}",
                e.getMostSpecificCause().getMessage()
        );

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "La operación viola una restricción de datos " +
                        "(duplicado o registro en uso)."
        );
    }

    // 400: falló @Valid en un @RequestBody
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errores = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Los datos enviados no son válidos."
        );

        // Agrega la lista de errores al JSON
        pd.setProperty("errores", errores);

        return handleExceptionInternal(
                e, pd, headers, status, request
        );
    }

    // 400: falló @Positive, @Min, etc. en parámetros de la URL
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errores = e.getAllErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Uno o más parámetros no son válidos."
        );

        pd.setProperty("errores", errores);

        return handleExceptionInternal(
                e, pd, headers, status, request
        );
    }

    // 500: captura errores que no fueron contemplados
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception e) {

        log.error("Error interno", e);

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor. Contacte al administrador."
        );
    }
}