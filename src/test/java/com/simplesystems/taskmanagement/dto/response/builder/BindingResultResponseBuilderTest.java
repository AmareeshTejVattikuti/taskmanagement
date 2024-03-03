package com.simplesystems.taskmanagement.dto.response.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BindingResultResponseBuilderTest {

    @Test
    @DisplayName("Test buildErrorResponse with binding result containing errors")
    void testBuildErrorResponseWithErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = new ArrayList<>();
        FieldError fieldError1 = new FieldError("objectName", "field1", "Error 1");
        FieldError fieldError2 = new FieldError("objectName", "field2", "Error 2");
        fieldErrors.add(fieldError1);
        fieldErrors.add(fieldError2);

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<Object> responseEntity = BindingResultResponseBuilder.buildErrorResponse(bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assert responseBody != null;
        var field1 = responseBody.get("field1");
        var message1 = field1 != null ? ((Map<?, ?>) field1).get("message") : null;
        var field2 = responseBody.get("field2");
        var message2 = field2 != null ? ((Map<?, ?>) field2).get("message") : null;

        assertEquals(2, responseBody.size());
        assertEquals("Error 1", message1);
        assertEquals("Error 2", message2);
    }

    @Test
    @DisplayName("Test buildErrorResponse with binding result containing no errors")
    void testBuildErrorResponseWithoutErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(new ArrayList<>());

        ResponseEntity<Object> responseEntity = BindingResultResponseBuilder.buildErrorResponse(bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(0, ((Map<?, ?>) Objects.requireNonNull(responseEntity.getBody())).size());
    }
}
