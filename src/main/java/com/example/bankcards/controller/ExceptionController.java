package com.example.bankcards.controller;

import com.example.bankcards.exception.CardNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return new ErrorResponse("Ошибка валидации", LocalDateTime.now(), errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotReadableException(HttpMessageNotReadableException ex) {
        return new ErrorResponse("Ошибка чтения запроса", LocalDateTime.now(),
                List.of(ex.getMessage(), ex.getCause().toString()));
    }

    @ExceptionHandler(CardNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCardNotFoundException(CardNotFoundException ex) {
        return new ErrorResponse("Ошибка поиска карты", LocalDateTime.now(),
                List.of(ex.getMessage(), ex.getCause().toString()));
    }

    public record ErrorResponse(String message, LocalDateTime timestamp, List<String> details) {}
}
