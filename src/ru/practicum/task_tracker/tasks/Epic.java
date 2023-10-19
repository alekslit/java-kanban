package ru.practicum.task_tracker.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    // ID Сабтасков, на которые разделён Эпик:
    private List<Integer> subtaskIds;

    public Epic(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.duration = 0;
        this.startTime = LocalDateTime.now();
        subtaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description, TaskStatus status,
                int duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    @Override
    public String toString() {
        String result =  "Epic{id = " + id + ", name = '" + name;
        if (description != null) {
            result = result + "', description.length = " + description.length();
        } else {
            result = result + ", description.length = null";
        }

        result = result + ", status = '" + status + "', duration = "
                + duration + ", startTime = " + startTime;

        if (subtaskIds == null) {
            return result + ", subtaskIds = null}\n";
        }

        return result + ", subtaskIds.size = " + subtaskIds.size() + "}\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Epic epic = (Epic) obj;
        return id == epic.id && Objects.equals(name, epic.name) &&
                Objects.equals(description, epic.description) && status == epic.status;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }
}