package com.simplesystems.taskmanagement.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.simplesystems.taskmanagement.enums.TaskStatus;
import com.simplesystems.taskmanagement.enums.TaskStatusDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TasksByStatus {

    private boolean fetchAllTasks;

    @JsonDeserialize(using = TaskStatusDeserializer.class)
    private TaskStatus status;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TasksByStatus that = (TasksByStatus) obj;
        return fetchAllTasks == that.fetchAllTasks && status == that.status;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(fetchAllTasks) + status.hashCode();
    }
}
