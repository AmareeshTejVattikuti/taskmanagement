package com.simplesystems.taskmanagement.config;

import com.simplesystems.taskmanagement.exception.ScheduleJobFailedException;
import com.simplesystems.taskmanagement.job.PastDueTasksUpdaterJob;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "job.enable-past-due-tasks-updater", havingValue = "true")
public class SchedulingConfig {

    private final PastDueTasksUpdaterJob pastDueTasksUpdaterJob;

    public SchedulingConfig(PastDueTasksUpdaterJob pastDueTasksUpdaterJob) {
        this.pastDueTasksUpdaterJob = pastDueTasksUpdaterJob;
    }

    @Scheduled(cron = "${spring.task.scheduling.cron.expression}")
    public void updatePastDueTasks() {
        try {
            pastDueTasksUpdaterJob.updatePastDueTasksStatus();
        }
        catch (Exception e) {
            throw new ScheduleJobFailedException("past due tasks updated Job failed with " + e.getMessage());
        }
    }
}