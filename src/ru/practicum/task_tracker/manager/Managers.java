package ru.practicum.task_tracker.manager;

import java.nio.file.Paths;

// Утилитарный класс для создания менеджеров:
public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(Paths.get("./resources/managerData.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}