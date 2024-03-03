package com.simplesystems.taskmanagement.service;

import com.simplesystems.taskmanagement.dto.request.CreateTask;
import com.simplesystems.taskmanagement.dto.request.TasksByStatus;
import com.simplesystems.taskmanagement.dto.request.UpdateStatus;
import com.simplesystems.taskmanagement.dto.request.UpdateDescription;
import com.simplesystems.taskmanagement.dto.response.TaskDetails;
import com.simplesystems.taskmanagement.entity.Task;
import java.util.List;

public interface TaskManagementService {
    TaskDetails createTask(CreateTask createTask);
    TaskDetails updateTask(UpdateStatus updateStatus);
    TaskDetails updateDescription(UpdateDescription description);
    List<TaskDetails> getTasksByStatus(TasksByStatus tasksByStatus);
    Task getTaskById(Long id);
    void updatePastDueTasks();
}
