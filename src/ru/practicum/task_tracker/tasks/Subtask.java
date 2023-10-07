package ru.practicum.task_tracker.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    // ID Эпика, в рамках которого выполняется Сабтаск:
    private final int epicId;

    public Subtask(String name, String description, TaskStatus status,
                   int duration, LocalDateTime startTime, int epicId) {
        super(name, description, status, duration, startTime);
        this.endTime = startTime.plusMinutes(duration);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus status,
                   int duration, LocalDateTime startTime, int epicId) {
        super(id, name, description, status, duration, startTime);
        this.endTime = startTime.plusMinutes(duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String result =  "Subtask{id = " + id + ", name = '" + name;
        if (description != null) {
            result = result + "', description.length = " + description.length();
        } else {
            result = result + "', description.length = null";
        }

        return result + ", status = '" + status + "', duration = "
                + duration + ", startTime = " + startTime + ", epicId = " + epicId + "}\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Subtask subtask = (Subtask) obj;
        return id == subtask.id && Objects.equals(name, subtask.name) &&
                Objects.equals(description, subtask.description) && status == subtask.status
                && Objects.equals(duration, subtask.duration) && startTime.equals(subtask.startTime)
                && endTime.equals(subtask.endTime);
    }
}