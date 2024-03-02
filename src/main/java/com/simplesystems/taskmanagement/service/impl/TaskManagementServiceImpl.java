package com.simplesystems.taskmanagement.service.impl;

import com.simplesystems.taskmanagement.dto.request.TasksByStatus;
import com.simplesystems.taskmanagement.dto.request.UpdateStatus;
import com.simplesystems.taskmanagement.dto.request.UpdateDescription;
import com.simplesystems.taskmanagement.entity.Task;
import com.simplesystems.taskmanagement.enums.TaskStatus;
import com.simplesystems.taskmanagement.exception.StatusValidationException;
import com.simplesystems.taskmanagement.exception.TaskNotFoundException;
import com.simplesystems.taskmanagement.repository.TaskRepository;
import com.simplesystems.taskmanagement.dto.request.CreateTask;
import com.simplesystems.taskmanagement.dto.response.TaskDetails;
import com.simplesystems.taskmanagement.service.TaskManagementService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Override
    public TaskDetails createTask(CreateTask task) {
        task.setStatus(TaskStatus.NOT_DONE);
        var taskEntity = modelMapper.map(task, Task.class);
        taskEntity = taskRepository.save(taskEntity);
        return modelMapper.map(taskEntity, TaskDetails.class);
    }

    @Override
    public TaskDetails updateTask(UpdateStatus updateStatus) {
        validateStatus(updateStatus.getStatus());
        var task = getTaskById(updateStatus.getId());
        isSameAsCurrentStatus(task, updateStatus.getStatus());
        setStatusAndCompletionDate(task, updateStatus.getStatus());
        return modelMapper.map(taskRepository.save(task), TaskDetails.class);
    }

    @Override
    public TaskDetails updateDescription(UpdateDescription description) {
        var task = getTaskById(description.getId());
        forbidUpdatingPastDueTasks(task.getStatus());
        task.setDescription(description.getDescription());
        return modelMapper.map(taskRepository.save(task), TaskDetails.class);
    }

    @Override
    public List<TaskDetails> getTasksByStatus(TasksByStatus tasksByStatus) {
        if(tasksByStatus.isFetchAllTasks()) {
            return taskRepository.findAll().stream()
                    .map(task -> modelMapper.map(task, TaskDetails.class))
                    .toList();
        }
        return taskRepository.findByStatus(tasksByStatus.getStatus()).stream()
                .map(task -> modelMapper.map(task, TaskDetails.class))
                .toList();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    private void setStatusAndCompletionDate(Task task, TaskStatus status) {
        task.setStatus(status);
        if (status.equals(TaskStatus.DONE)) {
            task.setCompletionDate(LocalDateTime.now());
        } else {
            task.setCompletionDate(null);
        }
    }

    private void isSameAsCurrentStatus(Task task, TaskStatus status) {
        if (task.getStatus().equals(status)) {
            throw new StatusValidationException("Task is already in " + status + " status");
        }
    }

    private void validateStatus(TaskStatus status) {
        if(status == TaskStatus.PAST_DUE) {
            throw new StatusValidationException("Status cannot be set to " + status);
        }
    }

    private void forbidUpdatingPastDueTasks(TaskStatus status) {
        if(status == TaskStatus.PAST_DUE) {
            throw new StatusValidationException("Cannot update tasks with status " + status);
        }
    }
}
