package com.simplesystems.taskmanagement.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String TASK_API = "/api/v1/task";
    public static final String TASK_API_CREATE = "/create";
    public static final String TASK_API_UPDATE_STATUS = "/update-status";
    public static final String TASK_API_UPDATE_DESCRIPTION = "/update-description";
    public static final String TASK_API_FILTER_BY_STATUS = "/filter-by-status";
}
