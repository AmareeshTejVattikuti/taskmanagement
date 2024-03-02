package com.simplesystems.taskmanagement.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.simplesystems.taskmanagement.exception.StatusNotFoundException;

import java.io.IOException;

public class TaskStatusDeserializer extends JsonDeserializer<TaskStatus> {

    @Override
    public TaskStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();

        try {
            return TaskStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new StatusNotFoundException("Invalid task status: " + value);
        }
    }
}

