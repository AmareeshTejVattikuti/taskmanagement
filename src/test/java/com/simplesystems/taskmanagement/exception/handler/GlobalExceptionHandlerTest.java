package com.simplesystems.taskmanagement.exception.handler;

import com.simplesystems.taskmanagement.dto.response.ErrorResponse;
import com.simplesystems.taskmanagement.exception.ScheduleJobFailedException;
import com.simplesystems.taskmanagement.exception.StatusNotFoundException;
import com.simplesystems.taskmanagement.exception.StatusValidationException;
import com.simplesystems.taskmanagement.exception.TaskValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Test handleValidationExceptions method")
    void testHandleValidationExceptions() {
        String errorMessage = "Validation failed";
        BindingResult bindingResult = createBindingResult(errorMessage);

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleValidationException(
                new MethodArgumentNotValidException(mock(MethodParameter.class), bindingResult));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assert errorResponse != null;
        assertEquals(List.of(errorMessage).size(), errorResponse.message().size());
    }

    @Test
    @DisplayName("Test handleTaskValidationException method")
    void testHandleTaskValidationException() {
        String errorMessage = "Task validation failed";

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleTaskValidationException(
                new TaskValidationException(errorMessage));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assert errorResponse != null;
        assertEquals(List.of(errorMessage), errorResponse.message());
    }

    @Test
    @DisplayName("Test handleStatusValidationException method")
    void testHandleStatusValidationException() {
        String errorMessage = "Status validation failed";

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleStatusValidationException(
                new StatusValidationException(errorMessage));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assert errorResponse != null;
        assertEquals(List.of(errorMessage), errorResponse.message());
    }

    @Test
    @DisplayName("Test handleStatusNotFoundException method")
    void testHandleStatusNotFoundException() {
        String errorMessage = "Status not found";

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleStatusNotFoundException(
                new StatusNotFoundException(errorMessage));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assert errorResponse != null;
        assertEquals(List.of(errorMessage), errorResponse.message());
    }

    @Test
    @DisplayName("Test handleScheduleJobFailedExceptions method")
    void testHandleScheduleJobFailedException() {
        String errorMessage = "past due tasks updated Job failed with error";

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleScheduleJobFailedException(
                new ScheduleJobFailedException(errorMessage));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assert errorResponse != null;
        assertEquals(List.of(errorMessage), errorResponse.message());
    }

    private BindingResult createBindingResult(String errorMessage) {
        return new BeanPropertyBindingResult(null, "") {
            @Override
            public List<FieldError> getFieldErrors() {
                return List.of(new org.springframework.validation.FieldError(
                        "object", "field", errorMessage));
            }
        };
    }
}
