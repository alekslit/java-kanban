package ru.practicum.task_tracker.tasks;

public class Subtask extends Task {
    // ID Эпика, в рамках которого выполняется Сабтаск:
    private final int epicId;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description, status);
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

        return result + ", status = '" + status + "', epicId = " + epicId + "}\n";
    }
}