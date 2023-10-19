package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.manager.http_task_manager.HttpTaskManager;

// Утилитарный класс для создания менеджеров:
public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}