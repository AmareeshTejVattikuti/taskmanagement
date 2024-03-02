package com.simplesystems.taskmanagement.dto.response;

import com.simplesystems.taskmanagement.enums.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TaskDetails {

    private Long id;
    private String description;
    private TaskStatus status;
    private LocalDateTime creationDate;
    private LocalDate dueDate;
    private LocalDateTime completionDate;
    private LocalDateTime lastModifiedDate;
}

