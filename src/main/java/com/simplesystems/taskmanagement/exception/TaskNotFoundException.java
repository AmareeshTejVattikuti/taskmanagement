package com.simplesystems.taskmanagement.exception;

public class TaskNotFoundException extends IllegalArgumentException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}

