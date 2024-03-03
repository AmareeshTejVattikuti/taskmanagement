package com.simplesystems.taskmanagement.service.impl;

import com.simplesystems.taskmanagement.dto.request.TasksByStatus;
import com.simplesystems.taskmanagement.dto.request.UpdateStatus;
import com.simplesystems.taskmanagement.dto.request.UpdateDescription;
import com.simplesystems.taskmanagement.entity.Task;
import com.simplesystems.taskmanagement.enums.TaskStatus;
import com.simplesystems.taskmanagement.exception.StatusValidationException;
import com.simplesystems.taskmanagement.exception.TaskNotFoundException;
import com.simplesystems.taskmanagement.repository.TaskManagementRepository;
import com.simplesystems.taskmanagement.dto.request.CreateTask;
import com.simplesystems.taskmanagement.dto.response.TaskDetails;
import com.simplesystems.taskmanagement.service.TaskManagementService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskManagementRepository taskManagementRepository;
    private final ModelMapper modelMapper;

    @Override
    public TaskDetails createTask(CreateTask task) {
        var taskEntity = modelMapper.map(task, Task.class);
        taskEntity.setStatus(TaskStatus.NOT_DONE);
        taskEntity = taskManagementRepository.save(taskEntity);
        return modelMapper.map(taskEntity, TaskDetails.class);
    }

    @Override
    public TaskDetails updateTask(UpdateStatus updateStatus) {
        validateStatus(updateStatus.getStatus());
        var task = getTaskById(updateStatus.getId());
        isSameAsCurrentStatus(task, updateStatus.getStatus());
        setStatusAndCompletionDate(task, updateStatus.getStatus());
        return modelMapper.map(taskManagementRepository.save(task), TaskDetails.class);
    }

    @Override
    public TaskDetails updateDescription(UpdateDescription description) {
        var task = getTaskById(description.getId());
        forbidUpdatingPastDueTasks(task.getStatus());
        task.setDescription(description.getDescription());
        return modelMapper.map(taskManagementRepository.save(task), TaskDetails.class);
    }

    @Override
    public List<TaskDetails> getTasksByStatus(TasksByStatus tasksByStatus) {
        if(tasksByStatus.isFetchAllTasks()) {
            return taskManagementRepository.findAll().stream()
                    .map(task -> modelMapper.map(task, TaskDetails.class))
                    .toList();
        }
        return taskManagementRepository.findByStatus(tasksByStatus.getStatus()).stream()
                .map(task -> modelMapper.map(task, TaskDetails.class))
                .toList();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskManagementRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public void updatePastDueTasks() {
        List<Task> pastDueTasks = taskManagementRepository.findByDueDateBeforeAndStatus(LocalDate.now(), TaskStatus.NOT_DONE);

        for (Task task : pastDueTasks) {
            task.setStatus(TaskStatus.PAST_DUE);
            taskManagementRepository.save(task);
        }
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
        forbidUpdatingPastDueTasks(task.getStatus());
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
