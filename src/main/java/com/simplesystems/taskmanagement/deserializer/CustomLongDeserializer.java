package com.simplesystems.taskmanagement.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.simplesystems.taskmanagement.exception.TaskValidationException;

import java.io.IOException;

public class CustomLongDeserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new TaskValidationException("Invalid long value: " + value);
        }
    }
}

