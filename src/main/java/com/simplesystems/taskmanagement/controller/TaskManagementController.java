package com.simplesystems.taskmanagement.controller;

import com.simplesystems.taskmanagement.config.Constants;
import com.simplesystems.taskmanagement.dto.request.CreateTask;
import com.simplesystems.taskmanagement.dto.request.TasksByStatus;
import com.simplesystems.taskmanagement.dto.request.UpdateStatus;
import com.simplesystems.taskmanagement.dto.request.UpdateDescription;
import com.simplesystems.taskmanagement.dto.response.builder.BindingResultResponseBuilder;
import com.simplesystems.taskmanagement.dto.response.ErrorResponse;
import com.simplesystems.taskmanagement.dto.response.TaskDetails;
import com.simplesystems.taskmanagement.exception.StatusValidationException;
import com.simplesystems.taskmanagement.exception.TaskNotFoundException;
import com.simplesystems.taskmanagement.exception.TaskValidationException;
import com.simplesystems.taskmanagement.service.TaskManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.function.Supplier;


@RestController
@RequestMapping(Constants.TASK_API)
@RequiredArgsConstructor
public class TaskManagementController {

    private final TaskManagementService taskManagementService;
    private final ModelMapper modelMapper;

    @PostMapping(Constants.TASK_API_CREATE)
    public ResponseEntity<?> createTask(@Valid @RequestBody CreateTask task, BindingResult bindingResult) {
        return handleServiceMethod(() -> taskManagementService.createTask(task), bindingResult);
    }

    @PatchMapping(Constants.TASK_API_UPDATE_STATUS)
    public ResponseEntity<?> updateStatus(@Valid @RequestBody UpdateStatus updateStatus, BindingResult bindingResult) {
        return handleServiceMethod(() -> taskManagementService.updateTask(updateStatus), bindingResult);
    }

    @PutMapping(Constants.TASK_API_UPDATE_DESCRIPTION)
    public ResponseEntity<?> updateDescription(@Valid @RequestBody UpdateDescription description, BindingResult bindingResult) {
        return handleServiceMethod(() -> taskManagementService.updateDescription(description), bindingResult);
    }

    @GetMapping(Constants.TASK_API_FILTER_BY_STATUS)
    public ResponseEntity<?> getAllTasksByStatus(@Valid @RequestBody TasksByStatus tasksByStatus, BindingResult bindingResult) {
        return handleServiceMethod(() -> taskManagementService.getTasksByStatus(tasksByStatus), bindingResult, "No tasks found");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@NotNull @PathVariable Long id) {
        try{
            return ResponseEntity.ok(modelMapper.map(taskManagementService.getTaskById(id), TaskDetails.class));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(List.of(e.getMessage())));
        }
    }

    private ResponseEntity<?> handleServiceMethod(Supplier<Object> serviceMethod, BindingResult bindingResult) {
        return handleServiceMethod(serviceMethod, bindingResult, null);
    }

    private ResponseEntity<?> handleServiceMethod(Supplier<Object> serviceMethod, BindingResult bindingResult, String warningMessage) {
        if (bindingResult.hasErrors()) {
            return BindingResultResponseBuilder.buildErrorResponse(bindingResult);
        }

        try {
            Object result = serviceMethod.get();
            if (result instanceof List && ((List<?>) result).isEmpty()) {
                return ResponseEntity.noContent().header("Warning", warningMessage).build();
            }
            return ResponseEntity.ok(result);
        } catch (TaskValidationException | TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(List.of(e.getMessage())));
        } catch (StatusValidationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(List.of(e.getMessage())));
        }
    }
}

