package com.simplesystems.taskmanagement.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.simplesystems.taskmanagement.exception.TaskValidationException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomLongDeserializerTest {

    private final JsonParser jsonParser = mock(JsonParser.class);
    private final DeserializationContext deserializationContext = mock(DeserializationContext.class);
    private final JsonDeserializer<Long> customLongDeserializer = new CustomLongDeserializer();

    @Test
    @DisplayName("Test deserialize with valid long value")
    void testDeserializeValidLongValue() throws IOException {
        String validLongValue = "12345";
        when(jsonParser.getValueAsString()).thenReturn(validLongValue);

        Long result = customLongDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(Long.parseLong(validLongValue), result);
    }

    @Test
    @DisplayName("Test deserialize with invalid long value")
    @SneakyThrows
    void testDeserializeInvalidLongValue() {
        String invalidLongValue = "abc";
        when(jsonParser.getValueAsString()).thenReturn(invalidLongValue);

        assertThrows(TaskValidationException.class, () ->
                customLongDeserializer.deserialize(jsonParser, deserializationContext));
    }
}
