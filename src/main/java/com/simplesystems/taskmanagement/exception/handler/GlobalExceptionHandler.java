package com.simplesystems.taskmanagement.exception.handler;

import com.simplesystems.taskmanagement.dto.response.ErrorResponse;
import com.simplesystems.taskmanagement.exception.StatusNotFoundException;
import com.simplesystems.taskmanagement.exception.StatusValidationException;
import com.simplesystems.taskmanagement.exception.TaskValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(createErrorResponse(ex.getBindingResult()));
    }

    @ExceptionHandler(TaskValidationException.class)
    public ResponseEntity<Object> handleTaskValidationException(TaskValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(List.of(ex.getMessage())));
    }

    @ExceptionHandler(StatusValidationException.class)
    public ResponseEntity<Object> handleStatusValidationException(StatusValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(List.of(ex.getMessage())));
    }

    @ExceptionHandler(StatusNotFoundException.class)
    public ResponseEntity<Object> handleStatusNotFoundException(StatusNotFoundException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(List.of(ex.getMessage())));
    }

    private ErrorResponse createErrorResponse(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return new ErrorResponse(errors);
    }
}
