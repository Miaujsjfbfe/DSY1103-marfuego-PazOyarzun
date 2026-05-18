package com.example.ms_pedidos.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // VALIDACIONES DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> manejarValidaciones(MethodArgumentNotValidException e){

        Map<String, String> errores = new HashMap<>();

        for(FieldError error : e.getBindingResult().getFieldErrors()){

            errores.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }


    // REGLAS DE NEGOCIO
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> manejarRuntime(
            RuntimeException e){

        Map<String, Object> error = new HashMap<>();

        //error.put("timestamp", LocalDateTime.now());

        //error.put("status", HttpStatus.BAD_REQUEST.value());

        error.put("mensaje", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    // ERROR GENERAL
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarGeneral(Exception e){

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());

        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        error.put("mensaje", "Error interno del servidor.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}