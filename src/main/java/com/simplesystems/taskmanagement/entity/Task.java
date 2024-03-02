package com.simplesystems.taskmanagement.entity;

import com.simplesystems.taskmanagement.enums.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private TaskStatus status;
    private LocalDateTime creationDate;
    private LocalDate dueDate;
    private LocalDateTime completionDate;
    private LocalDateTime lastModifiedDate;

}
