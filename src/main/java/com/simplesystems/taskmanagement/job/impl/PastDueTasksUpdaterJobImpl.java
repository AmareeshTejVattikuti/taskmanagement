package com.simplesystems.taskmanagement.job.impl;

import com.simplesystems.taskmanagement.job.PastDueTasksUpdaterJob;
import com.simplesystems.taskmanagement.service.TaskManagementService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PastDueTasksUpdaterJobImpl implements PastDueTasksUpdaterJob {

    private final TaskManagementService taskManagementService;

    public PastDueTasksUpdaterJobImpl(TaskManagementService taskManagementService) {
        this.taskManagementService = taskManagementService;
    }

    @Override
    @Transactional
    public void updatePastDueTasksStatus() {
        taskManagementService.updatePastDueTasks();
    }
}