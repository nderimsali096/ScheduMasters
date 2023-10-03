package com.challenge.softschedugarden.exceptions;

public class RegisterValidationException extends RuntimeException {
    public RegisterValidationException(String message) {
        super(message);
    }

    public RegisterValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
