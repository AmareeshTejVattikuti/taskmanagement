package com.simplesystems.taskmanagement.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.simplesystems.taskmanagement.exception.StatusNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskStatusDeserializerTest {

    private final JsonParser jsonParser = mock(JsonParser.class);
    private final DeserializationContext deserializationContext = mock(DeserializationContext.class);
    private final JsonDeserializer<TaskStatus> taskStatusDeserializer = new TaskStatusDeserializer();

    @Test
    @DisplayName("Test deserialize with valid task status")
    void testDeserializeValidTaskStatus() throws IOException {
        String validTaskStatus = "DONE";
        when(jsonParser.getValueAsString()).thenReturn(validTaskStatus);

        TaskStatus result = taskStatusDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(TaskStatus.valueOf(validTaskStatus), result);
    }

    @Test
    @DisplayName("Test deserialize with invalid task status")
    @SneakyThrows
    void testDeserializeInvalidTaskStatus() {
        String invalidTaskStatus = "INVALID_STATUS";
        when(jsonParser.getValueAsString()).thenReturn(invalidTaskStatus);

        assertThrows(StatusNotFoundException.class, () ->
                taskStatusDeserializer.deserialize(jsonParser, deserializationContext));
    }
}
