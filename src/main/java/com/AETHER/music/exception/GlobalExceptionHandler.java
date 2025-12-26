package com.AETHER.music.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String ,String> handleNotFound(ResourceNotFoundException ex){
        return Map.of("error",ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private Map<String ,String > handleConflict(ConflictException e){
        return Map.of("error",e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private Map<String ,String > handleUnauthorized(UnauthorizedException e){
        return Map.of("error",e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String ,String> handleValidation(MethodArgumentNotValidException e){
        return Map.of(
                "error",
                e.getBindingResult().getFieldErrors().get(0).getDefaultMessage()
        );
    }
}
