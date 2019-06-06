package com.crossover.ivolunteer.security;

import com.crossover.ivolunteer.presentation.dto.RespostaSimplesDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class HttpStatusCodeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {HttpStatusCodeException.class,})
    protected ResponseEntity<Object> handleHttpStatusCodeException(RuntimeException ex, HttpServletRequest request, WebRequest webRequest) {
        HttpClientErrorException httpEx = (HttpClientErrorException) ex;
        RespostaSimplesDto respostaSimplesDto = new RespostaSimplesDto(httpEx.getStatusCode(), request.getServletPath(), httpEx.getMessage());
        return handleExceptionInternal(ex, respostaSimplesDto, new HttpHeaders(), httpEx.getStatusCode(), webRequest);
    }
}