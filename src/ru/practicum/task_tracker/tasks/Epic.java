package ru.practicum.task_tracker.tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    // ID Сабтасков, на которые разделён Эпик:
    private final List<Integer> subtaskIds;

    public Epic(String name, String description) {

        super(name, description, TaskStatus.NEW);
        subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {

        return subtaskIds;
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

        result = result + ", status = '" + status;

        if (subtaskIds == null) {
            return result + "', subtaskIds = null}\n";
        }

        return result + "', subtaskIds.size = " + subtaskIds.size() + "}\n";
    }
}