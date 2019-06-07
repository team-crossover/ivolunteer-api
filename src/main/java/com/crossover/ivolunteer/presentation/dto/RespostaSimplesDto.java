package com.crossover.ivolunteer.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespostaSimplesDto {

    private String timestamp;
    private int status;
    private String error;
    private Object message;
    private String path;

    public RespostaSimplesDto(int status, String path, Object message) {
        this.timestamp = (ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
        this.status = (status);
        this.error = null;
        this.message = (message);
        this.path = (path);
    }

    public RespostaSimplesDto(int status, String error, String path, Object message) {
        this.timestamp = (ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
        this.status = (status);
        this.error = (error);
        this.message = (message);
        this.path = (path);
    }

    public RespostaSimplesDto(int status, Exception exception, String path) {
        this.timestamp = (ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
        this.status = (status);
        this.error = (exception.toString());
        this.message = (exception.getMessage());
        this.path = (path);
    }

    public RespostaSimplesDto(HttpStatus status, String path) {
        this.timestamp = (ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
        this.status = (status.value());
        this.error = (status.getReasonPhrase());
        this.message = (status.getReasonPhrase());
        this.path = (path);
    }

    public RespostaSimplesDto(HttpStatus status, String path, Object message) {
        this.timestamp = (ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
        this.status = (status.value());
        this.error = (status.getReasonPhrase());
        this.message = (message);
        this.path = (path);
    }
}
