package com.simplesystems.taskmanagement.exception;

public class ScheduleJobFailedException extends IllegalArgumentException {

    public ScheduleJobFailedException(String message) {
        super(message);
    }
}