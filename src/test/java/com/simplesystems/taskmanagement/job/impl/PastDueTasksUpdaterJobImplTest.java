package com.simplesystems.taskmanagement.job.impl;

import com.simplesystems.taskmanagement.job.PastDueTasksUpdaterJob;
import com.simplesystems.taskmanagement.service.TaskManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PastDueTasksUpdaterJobImplTest {

    @Mock
    private TaskManagementService taskManagementService;

    @Test
    void testUpdatePastDueTasksStatus() {
        PastDueTasksUpdaterJob pastDueTasksUpdaterJob = new PastDueTasksUpdaterJobImpl(taskManagementService);
        pastDueTasksUpdaterJob.updatePastDueTasksStatus();
        verify(taskManagementService, times(1)).updatePastDueTasks();
    }
}