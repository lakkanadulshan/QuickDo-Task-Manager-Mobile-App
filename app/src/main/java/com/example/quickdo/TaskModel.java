package com.example.quickdo;

public class TaskModel {
    private String taskId;
    private String title;
    private String description;
    private String status;
    private long timestamp;

    public TaskModel() {
        // Required for Firestore
    }

    public TaskModel(String taskId, String title, String description, String status, long timestamp) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
