package com.crossover.ivolunteer.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationRequestException extends AuthenticationException {
    public InvalidAuthenticationRequestException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidAuthenticationRequestException(String msg) {
        super(msg);
    }
}
