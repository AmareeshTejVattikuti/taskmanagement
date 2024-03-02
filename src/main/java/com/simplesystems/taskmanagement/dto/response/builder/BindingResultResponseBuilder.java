package com.simplesystems.taskmanagement.dto.response.builder;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class BindingResultResponseBuilder {

    private BindingResultResponseBuilder() {
    }

    public static ResponseEntity<Object> buildErrorResponse(BindingResult bindingResult) {
        Map<String, Object> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("field", error.getField());
            errorDetails.put("message", error.getDefaultMessage());
            errorDetails.put("rejectedValue", error.getRejectedValue());

            errors.put(error.getField(), errorDetails);
        }
        return ResponseEntity.badRequest().body(errors);
    }
}

