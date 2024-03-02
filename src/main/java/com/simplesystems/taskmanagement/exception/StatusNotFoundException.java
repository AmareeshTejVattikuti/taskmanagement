package com.simplesystems.taskmanagement.exception;

public class StatusNotFoundException extends IllegalArgumentException {

    public StatusNotFoundException(String message) {
        super(message);
    }
}

