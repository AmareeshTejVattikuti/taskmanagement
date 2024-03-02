package com.simplesystems.taskmanagement.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.simplesystems.taskmanagement.enums.TaskStatus;
import com.simplesystems.taskmanagement.enums.TaskStatusDeserializer;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class CreateTask {

    @NotEmpty(message = "Description is required")
    private String description;

    @JsonDeserialize(using = TaskStatusDeserializer.class)
    private TaskStatus status;

    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDate dueDate;
}

