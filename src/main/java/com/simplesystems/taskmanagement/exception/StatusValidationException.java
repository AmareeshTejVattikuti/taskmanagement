package com.simplesystems.taskmanagement.exception;

public class StatusValidationException extends IllegalArgumentException {

    public StatusValidationException(String message) {
        super(message);
    }
}

