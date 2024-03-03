package com.simplesystems.taskmanagement.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.simplesystems.taskmanagement.deserializer.CustomLongDeserializer;
import com.simplesystems.taskmanagement.enums.TaskStatus;
import com.simplesystems.taskmanagement.enums.TaskStatusDeserializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class UpdateStatus {

    @NotNull(message = "Task id is required")
    @Positive(message = "Task id must be greater than 0")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long id;

    @NotNull(message = "Status is required")
    @JsonDeserialize(using = TaskStatusDeserializer.class)
    private TaskStatus status;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UpdateStatus that = (UpdateStatus) obj;
        return Objects.equals(id, that.id) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }
}
