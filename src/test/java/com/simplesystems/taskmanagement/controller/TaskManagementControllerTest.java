package com.simplesystems.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.simplesystems.taskmanagement.dto.request.CreateTask;
import com.simplesystems.taskmanagement.dto.request.TasksByStatus;
import com.simplesystems.taskmanagement.dto.request.UpdateDescription;
import com.simplesystems.taskmanagement.dto.request.UpdateStatus;
import com.simplesystems.taskmanagement.dto.response.TaskDetails;
import com.simplesystems.taskmanagement.entity.Task;
import com.simplesystems.taskmanagement.enums.TaskStatus;
import com.simplesystems.taskmanagement.exception.TaskNotFoundException;
import com.simplesystems.taskmanagement.exception.TaskValidationException;
import com.simplesystems.taskmanagement.service.TaskManagementService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaskManagementControllerTest {

    @Mock
    private TaskManagementService taskManagementService;

    @InjectMocks
    private TaskManagementController taskManagementController;

    @Mock
    private ModelMapper modelMapper;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private CreateTask createTask;
    private UpdateStatus updateStatus;
    private UpdateDescription updateDescription;
    private TasksByStatus tasksByStatus;
    private static final String TASK_API = "/api/v1/task";
    private static final String TASK_API_CREATE = "/api/v1/task/create";
    private static final String TASK_API_UPDATE_STATUS = "/api/v1/task/update-status";
    private static final String TASK_API_UPDATE_DESCRIPTION = "/api/v1/task/update-description";
    private static final String TASK_API_FILTER_BY_STATUS = "/api/v1/task/filter-by-status";

    @Nested
    @DisplayName("test task creation api")
    class createTask {

        @BeforeEach
        void setUp() {
            createTask = CreateTask.builder()
                    .description("Test")
                    .dueDate(LocalDate.now())
                    .build();
        }

        @Test
        @SneakyThrows
        void createTask_validInput() {
            TaskDetails createdTaskDetails = mock(TaskDetails.class);
            when(createdTaskDetails.getDescription()).thenReturn("Test");
            when(taskManagementService.createTask(createTask)).thenReturn(createdTaskDetails);

            ResultActions result = performPost(TASK_API_CREATE, createTask);
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.description").exists());
            verify(taskManagementService, times(1)).createTask(createTask);
        }

        @Test
        @SneakyThrows
        void createTask_invalidInput_returnsBadRequestWithValidationError() {
            createTask.setDescription("");
            ResultActions result = performPost(TASK_API_CREATE, createTask);

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.description.message").value("Description is required"));
            verifyNoInteractions(taskManagementService);
        }

        @Test
        @SneakyThrows
        void createTask_taskValidationException_returnsBadRequestWithErrorMessage() {
            createTask.setDescription("Test");
            when(taskManagementService.createTask(createTask))
                    .thenThrow(new TaskValidationException("Validation failed"));

            ResultActions result = performPost(TASK_API_CREATE, createTask);
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Validation failed"));
            verify(taskManagementService, times(1)).createTask(createTask);
        }

    }

    @Nested
    @DisplayName("test task status update api")
    class updateStatus {
        @BeforeEach
        void setUp() {
            updateStatus = new UpdateStatus();
            updateStatus.setId(1L);
            updateStatus.setStatus(TaskStatus.DONE);
        }

        @Test
        @SneakyThrows
        void updateStatus_validInput() {
            TaskDetails updatedTaskDetails = new TaskDetails();
            updatedTaskDetails.setId(1L);
            updatedTaskDetails.setDescription("Test");
            updatedTaskDetails.setStatus(TaskStatus.DONE);

            when(taskManagementService.updateTask(updateStatus)).thenReturn(updatedTaskDetails);

            ResultActions result = performPatch(TASK_API_UPDATE_STATUS, updateStatus);
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.description").exists());
            verify(taskManagementService, times(1)).updateTask(updateStatus);
        }

        @Test
        @SneakyThrows
        void updateStatus_invalidInput_returnsBadRequestWithValidationError() {
            UpdateStatus updateStatus = new UpdateStatus();
            updateStatus.setId(null);

            ResultActions result = performPatch(TASK_API_UPDATE_STATUS, updateStatus);
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.id.message").value("Task id is required"))
                    .andExpect(jsonPath("$.status.message").value("Status is required"));
            verifyNoInteractions(taskManagementService);
        }

        @Test
        @SneakyThrows
        void updateStatus_TaskNotFoundException_ReturnsBadRequestWithErrorMessage(){
            when(taskManagementService.updateTask(updateStatus))
                    .thenThrow(new TaskNotFoundException("Task not found"));

            ResultActions result = performPatch(TASK_API_UPDATE_STATUS, updateStatus);
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Task not found"));
            verify(taskManagementService, times(1)).updateTask(updateStatus);
        }
    }

    @Nested
    @DisplayName("test task description update api")
    class updateDescription {

        @BeforeEach
        void setUp() {
            updateDescription = new UpdateDescription();
            updateDescription.setId(1L);
            updateDescription.setDescription("Test");
        }

        @Test
        @SneakyThrows
        void updateDescription_validInput() {
            TaskDetails updatedTaskDetails = new TaskDetails();
            updatedTaskDetails.setId(1L);
            updatedTaskDetails.setDescription("Test");
            updatedTaskDetails.setStatus(TaskStatus.NOT_DONE);

            when(taskManagementService.updateDescription(updateDescription)).thenReturn(updatedTaskDetails);

            ResultActions result = performPut(TASK_API_UPDATE_DESCRIPTION, updateDescription);
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.description").exists());
            verify(taskManagementService, times(1)).updateDescription(updateDescription);
        }

        @Test
        @SneakyThrows
        void updateDescription_invalidInput_returnsBadRequestWithValidationError() {
            updateDescription.setId(null);

            ResultActions result = performPut(TASK_API_UPDATE_DESCRIPTION, updateDescription);
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.id.message").value("Task id is required"));
            verifyNoInteractions(taskManagementService);
        }

        @Test
        @SneakyThrows
        void updateDescription_TaskNotFoundException_ReturnsBadRequestWithErrorMessage() {
            when(taskManagementService.updateDescription(updateDescription))
                    .thenThrow(new TaskNotFoundException("Task not found"));

            ResultActions result = performPut(TASK_API_UPDATE_DESCRIPTION, updateDescription);
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Task not found"));
            verify(taskManagementService, times(1)).updateDescription(updateDescription);
        }
    }

    @Nested
    @DisplayName("test task retrieval api")
    class getTaskById {

        @Test
        @SneakyThrows
        void getTaskById_validInput() {
            Task task1 = mock(Task.class);
            TaskDetails task = mock(TaskDetails.class);
            when(task.getDescription()).thenReturn("Test");
            when(task.getStatus()).thenReturn(TaskStatus.NOT_DONE);
            when(task.getDueDate()).thenReturn(LocalDate.now());
            when(task.getCompletionDate()).thenReturn(null);
            when(task.getId()).thenReturn(1L);
            when(task.getCreationDate()).thenReturn(LocalDateTime.now());
            when(task.getLastModifiedDate()).thenReturn(LocalDateTime.now());
            when(taskManagementService.getTaskById(1L)).thenReturn(task1);
            when(modelMapper.map(task1, TaskDetails.class)).thenReturn(task);

            ResultActions result = mockMvcBuilder().perform(MockMvcRequestBuilders.get(TASK_API + "/1"));
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.description").exists())
                    .andExpect(jsonPath("$.status").value("NOT_DONE"))
                    .andExpect(jsonPath("$.dueDate").exists())
                    .andExpect(jsonPath("$.completionDate").doesNotExist())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.creationDate").exists());
            verify(taskManagementService, times(1)).getTaskById(1L);
        }

        @Test
        @SneakyThrows
        void getTaskById_TaskNotFoundException_ReturnsBadRequestWithErrorMessage() {
            when(taskManagementService.getTaskById(1L))
                    .thenThrow(new TaskNotFoundException("Task not found"));

            ResultActions result = mockMvcBuilder().perform(MockMvcRequestBuilders.get(TASK_API + "/1"));
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Task not found"));
            verify(taskManagementService, times(1)).getTaskById(1L);
        }
    }

    @Nested
    @DisplayName("test task retrieval by status api")
    class getTasksByStatus {

        @BeforeEach
        void setUp() {
            tasksByStatus = new TasksByStatus();
            tasksByStatus.setFetchAllTasks(true);
            tasksByStatus.setStatus(TaskStatus.NOT_DONE);
        }
        @Test
        @SneakyThrows
        void getTasksByStatus_fetchAllTasks() {
            when(taskManagementService.getTasksByStatus(tasksByStatus))
                    .thenReturn(List.of(getTaskDetails(1L, TaskStatus.NOT_DONE), getTaskDetails(2L, TaskStatus.DONE)));

            ResultActions result = performGet(TASK_API_FILTER_BY_STATUS, tasksByStatus);
            result.andExpect(status().isOk());
            assertEquals(2, objectMapper.readTree(result.andReturn().getResponse().getContentAsString()).size());
            result.andExpect(jsonPath("$[0].description").value("Test"))
                    .andExpect(jsonPath("$[0].status").value("NOT_DONE"))
                    .andExpect(jsonPath("$[0].dueDate").exists())
                    .andExpect(jsonPath("$[0].completionDate").doesNotExist())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].creationDate").exists())
                    .andExpect(jsonPath("$[1].description").value("Test"))
                    .andExpect(jsonPath("$[1].status").value("DONE"))
                    .andExpect(jsonPath("$[1].dueDate").exists())
                    .andExpect(jsonPath("$[1].completionDate").exists())
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].creationDate").exists());
            verify(taskManagementService, times(1)).getTasksByStatus(tasksByStatus);
        }

        @Test
        @SneakyThrows
        void getTasksByStatus_fetchByStatus() {
            tasksByStatus.setFetchAllTasks(false);
            when(taskManagementService.getTasksByStatus(tasksByStatus)).thenReturn(List.of(getTaskDetails(1L, TaskStatus.NOT_DONE)));

            ResultActions result = performGet(TASK_API_FILTER_BY_STATUS, tasksByStatus);
            result.andExpect(status().isOk());
            System.out.print(result.andReturn().getResponse().getContentAsString());
            assertEquals(1, objectMapper.readTree(result.andReturn().getResponse().getContentAsString()).size());
            result.andExpect(jsonPath("$[0].description").value("Test"))
                    .andExpect(jsonPath("$[0].status").value("NOT_DONE"))
                    .andExpect(jsonPath("$[0].dueDate").exists())
                    .andExpect(jsonPath("$[0].completionDate").doesNotExist())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].creationDate").exists());
            verify(taskManagementService, times(1)).getTasksByStatus(tasksByStatus);
        }
    }

    @SneakyThrows
    private ResultActions performPost(String url, Object requestBody) {
        return mockMvcBuilder().perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));
    }

    @SneakyThrows
    private ResultActions performPatch(String url, Object requestBody) {
        return mockMvcBuilder().perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));
    }

    @SneakyThrows
    private ResultActions performPut(String url, Object requestBody) {
        return mockMvcBuilder().perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));
    }

    @SneakyThrows
    private ResultActions performGet(String url, Object requestBody) {
        return mockMvcBuilder().perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));
    }

    private MockMvc mockMvcBuilder() {
        return MockMvcBuilders.standaloneSetup(taskManagementController).build();
    }

    private TaskDetails getTaskDetails(Long id, TaskStatus status) {
        TaskDetails taskDetails = new TaskDetails();
        taskDetails.setId(id);
        taskDetails.setDescription("Test");
        taskDetails.setStatus(status);
        taskDetails.setDueDate(LocalDate.now());
        taskDetails.setCompletionDate(status.equals(TaskStatus.DONE) ? LocalDateTime.now() : null);
        taskDetails.setCreationDate(LocalDateTime.now());
        taskDetails.setLastModifiedDate(LocalDateTime.now());
        return taskDetails;
    }
}
