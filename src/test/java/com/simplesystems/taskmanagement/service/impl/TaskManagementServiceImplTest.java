package com.simplesystems.taskmanagement.service.impl;

import com.simplesystems.taskmanagement.dto.request.CreateTask;
import com.simplesystems.taskmanagement.dto.request.TasksByStatus;
import com.simplesystems.taskmanagement.dto.request.UpdateDescription;
import com.simplesystems.taskmanagement.dto.request.UpdateStatus;
import com.simplesystems.taskmanagement.entity.Task;
import com.simplesystems.taskmanagement.enums.TaskStatus;
import com.simplesystems.taskmanagement.exception.TaskNotFoundException;
import com.simplesystems.taskmanagement.repository.TaskManagementRepository;
import com.simplesystems.taskmanagement.dto.response.TaskDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class TaskManagementServiceImplTest {

    @Mock
    private TaskManagementRepository taskManagementRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskManagementServiceImpl taskManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test createTask method")
    void testCreateTask() {
        CreateTask createTask = CreateTask.builder().description("Test").build();

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        taskEntity.setDescription("Test");
        taskEntity.setStatus(TaskStatus.NOT_DONE);

        TaskDetails expectedTaskDetails = new TaskDetails();
        expectedTaskDetails.setId(1L);
        expectedTaskDetails.setDescription("Test");
        expectedTaskDetails.setStatus(TaskStatus.NOT_DONE);

        when(modelMapper.map(createTask, Task.class)).thenReturn(taskEntity);
        when(taskManagementRepository.save(taskEntity)).thenReturn(taskEntity);
        when(modelMapper.map(taskEntity, TaskDetails.class)).thenReturn(expectedTaskDetails);

        TaskDetails result = taskManagementService.createTask(createTask);

        assertNotNull(result);
        assertEquals(expectedTaskDetails, result);
        verify(taskManagementRepository, times(1)).save(taskEntity);
    }

    @Test
    @DisplayName("Test updateTask method")
    void testUpdateTask() {
        UpdateStatus updateStatus = new UpdateStatus();
        updateStatus.setId(1L);
        updateStatus.setStatus(TaskStatus.DONE);

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setStatus(TaskStatus.NOT_DONE);

        Task updatedTaskEntity = new Task();
        updatedTaskEntity.setId(1L);
        updatedTaskEntity.setStatus(TaskStatus.DONE);

        TaskDetails expectedTaskDetails = new TaskDetails();
        expectedTaskDetails.setId(1L);
        expectedTaskDetails.setStatus(TaskStatus.DONE);

        when(taskManagementRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskManagementRepository.save(existingTask)).thenReturn(updatedTaskEntity);
        when(modelMapper.map(updatedTaskEntity, TaskDetails.class)).thenReturn(expectedTaskDetails);

        TaskDetails result = taskManagementService.updateTask(updateStatus);

        assertNotNull(result);
        assertEquals(TaskStatus.DONE, result.getStatus());

        verify(taskManagementRepository, times(1)).findById(1L);
        verify(taskManagementRepository, times(1)).save(existingTask);
        verify(modelMapper, times(1)).map(updatedTaskEntity, TaskDetails.class);
    }

    @Test
    @DisplayName("Test updateTask method - TaskNotFoundException")
    void testUpdateTaskTaskNotFoundException() {
        UpdateStatus updateStatus = new UpdateStatus();
        updateStatus.setId(1L);
        updateStatus.setStatus(TaskStatus.DONE);

        when(taskManagementRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskManagementService.updateTask(updateStatus));
        verify(taskManagementRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test getTaskById method")
    void testGetTaskById() {
        Task taskEntity = new Task();
        taskEntity.setId(1L);
        taskEntity.setDescription("Test");
        taskEntity.setStatus(TaskStatus.NOT_DONE);

        TaskDetails expectedTaskDetails = new TaskDetails();
        expectedTaskDetails.setId(1L);
        expectedTaskDetails.setDescription("Test");
        expectedTaskDetails.setStatus(TaskStatus.NOT_DONE);

        when(taskManagementRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(modelMapper.map(taskEntity, TaskDetails.class)).thenReturn(expectedTaskDetails);

        Task result = taskManagementService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(expectedTaskDetails.getId(), result.getId());
        assertEquals(expectedTaskDetails.getDescription(), result.getDescription());
        assertEquals(expectedTaskDetails.getStatus(), result.getStatus());
    }

    @Test
    @DisplayName("Test getTaskById method - TaskNotFoundException")
    void testGetTaskByIdTaskNotFoundException() {
        when(taskManagementRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskManagementService.getTaskById(1L));
    }

    @Test
    @DisplayName("Test updateDescription method")
    void testUpdateDescription() {
        UpdateDescription updateDescription = new UpdateDescription();
        updateDescription.setId(1L);
        updateDescription.setDescription("Updated description");

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setDescription("Old description");

        Task updatedTaskEntity = new Task();
        updatedTaskEntity.setId(1L);
        updatedTaskEntity.setDescription("Updated description");

        TaskDetails expectedTaskDetails = new TaskDetails();
        expectedTaskDetails.setId(1L);
        expectedTaskDetails.setDescription("Updated description");

        when(taskManagementRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskManagementRepository.save(existingTask)).thenReturn(updatedTaskEntity);
        when(modelMapper.map(updatedTaskEntity, TaskDetails.class)).thenReturn(expectedTaskDetails);

        TaskDetails result = taskManagementService.updateDescription(updateDescription);

        assertNotNull(result);
        assertEquals("Updated description", result.getDescription());

        verify(taskManagementRepository, times(1)).findById(1L);
        verify(taskManagementRepository, times(1)).save(existingTask);
        verify(modelMapper, times(1)).map(updatedTaskEntity, TaskDetails.class);
    }

    @Test
    @DisplayName("Test getTasksByStatus method - Fetch All Tasks")
    void testGetAllTasks() {
        TasksByStatus tasksByStatus = new TasksByStatus();
        tasksByStatus.setFetchAllTasks(true);

        Task task1 = new Task();
        task1.setId(1L);
        task1.setStatus(TaskStatus.NOT_DONE);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setStatus(TaskStatus.DONE);

        List<Task> tasks = Arrays.asList(task1, task2);

        TaskDetails taskDetails1 = new TaskDetails();
        taskDetails1.setId(1L);
        taskDetails1.setStatus(TaskStatus.NOT_DONE);

        TaskDetails taskDetails2 = new TaskDetails();
        taskDetails2.setId(2L);
        taskDetails2.setStatus(TaskStatus.DONE);

        List<TaskDetails> expectedTaskDetailsList = Arrays.asList(taskDetails1, taskDetails2);

        when(taskManagementRepository.findAll()).thenReturn(tasks);
        when(modelMapper.map(task1, TaskDetails.class)).thenReturn(taskDetails1);
        when(modelMapper.map(task2, TaskDetails.class)).thenReturn(taskDetails2);

        List<TaskDetails> result = taskManagementService.getTasksByStatus(tasksByStatus);

        assertNotNull(result);
        assertEquals(expectedTaskDetailsList.size(), result.size());

        verify(taskManagementRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(task1, TaskDetails.class);
        verify(modelMapper, times(1)).map(task2, TaskDetails.class);
    }

    @Test
    @DisplayName("Test getTasksByStatus method - Fetch Tasks by Status")
    void testGetTasksByStatus() {
        TasksByStatus tasksByStatus = new TasksByStatus();
        tasksByStatus.setStatus(TaskStatus.NOT_DONE);
        tasksByStatus.setFetchAllTasks(false);

        Task task1 = new Task();
        task1.setId(1L);
        task1.setStatus(TaskStatus.NOT_DONE);

        List<Task> tasks = List.of(task1);

        TaskDetails taskDetails1 = new TaskDetails();
        taskDetails1.setId(1L);
        taskDetails1.setStatus(TaskStatus.NOT_DONE);

        List<TaskDetails> expectedTaskDetailsList = List.of(taskDetails1);

        when(taskManagementRepository.findByStatus(TaskStatus.NOT_DONE)).thenReturn(tasks);
        when(modelMapper.map(task1, TaskDetails.class)).thenReturn(taskDetails1);

        List<TaskDetails> result = taskManagementService.getTasksByStatus(tasksByStatus);

        assertNotNull(result);
        assertEquals(expectedTaskDetailsList.size(), result.size());

        verify(taskManagementRepository, times(1)).findByStatus(TaskStatus.NOT_DONE);
        verify(modelMapper, times(1)).map(task1, TaskDetails.class);
    }

    @Test
    void testUpdatePastDueTasks() {
        Task pastDueTask = new Task();
        pastDueTask.setId(1L);
        pastDueTask.setDueDate(LocalDate.now().minusDays(1));
        pastDueTask.setStatus(TaskStatus.NOT_DONE);

        when(taskManagementRepository.findByDueDateBeforeAndStatus(any(LocalDate.class), eq(TaskStatus.NOT_DONE)))
                .thenReturn(List.of(pastDueTask));

        taskManagementService.updatePastDueTasks();

        verify(taskManagementRepository, times(1)).save(pastDueTask);
        assertEquals(TaskStatus.PAST_DUE, pastDueTask.getStatus());
    }

}
