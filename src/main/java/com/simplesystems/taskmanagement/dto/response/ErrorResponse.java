package com.simplesystems.taskmanagement.dto.response;

import java.util.List;

public record ErrorResponse(List<String> message) {
}

