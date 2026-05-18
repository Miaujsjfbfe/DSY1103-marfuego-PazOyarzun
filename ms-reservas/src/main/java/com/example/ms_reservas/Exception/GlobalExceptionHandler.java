package com.example.ms_reservas.Exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // RUNTIME
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> manejarRuntime(
            RuntimeException e){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    // VALID
    @ExceptionHandler(
            MethodArgumentNotValidException.class)

    public ResponseEntity<?> manejarValidaciones(
            MethodArgumentNotValidException e){

        Map<String,String> errores =
                new HashMap<>();

        for(FieldError error :
                e.getBindingResult()
                        .getFieldErrors()){

            errores.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errores);
    }

}