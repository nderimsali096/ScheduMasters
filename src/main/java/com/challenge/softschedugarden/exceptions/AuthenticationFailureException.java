package com.challenge.softschedugarden.exceptions;

public class AuthenticationFailureException extends RuntimeException {
    public AuthenticationFailureException(String message) {
        super(message);
    }
}

