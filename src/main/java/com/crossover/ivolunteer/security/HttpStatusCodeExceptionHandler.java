package com.crossover.ivolunteer.security;

import com.crossover.ivolunteer.presentation.dto.RespostaSimplesDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class HttpStatusCodeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {HttpStatusCodeException.class,})
    protected ResponseEntity<Object> handleHttpStatusCodeException(HttpStatusCodeException ex, HttpServletRequest request, WebRequest webRequest) {
        RespostaSimplesDto respostaSimplesDto = new RespostaSimplesDto(ex.getStatusCode(), request.getServletPath(), ex.getMessage());
        return handleExceptionInternal(ex, respostaSimplesDto, new HttpHeaders(), ex.getStatusCode(), webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        RespostaSimplesDto respostaSimplesDto = new RespostaSimplesDto(HttpStatus.BAD_REQUEST, request.getContextPath(), errors);
        return this.handleExceptionInternal(ex, respostaSimplesDto, headers, status, request);
    }
}