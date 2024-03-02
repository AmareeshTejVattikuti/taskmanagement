package com.simplesystems.taskmanagement.enums;

import java.util.HashMap;
import java.util.Map;

public enum TaskStatus {
    NOT_DONE("Not Done"),
    DONE("Done"),
    PAST_DUE("Past Due");

    private final String label;
    private static final Map<String, TaskStatus> labelMap;

    TaskStatus(String label) {
        this.label = label;
    }

    static {
        labelMap = new HashMap<>();
        for(TaskStatus task: TaskStatus.values()) {
            labelMap.put(task.label, task);
        }
    }

    public static TaskStatus findByLabel(String label) {
        var status = labelMap.get(label);
        if(status == null) {
            throw new IllegalArgumentException("No enum constant with label: " + label);
        }
        return status;
    }

}
