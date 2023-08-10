package ru.practicum.task_tracker.manager;

// Утилитарный класс для создания менеджеров:
public class Managers {

    private Managers() {

    }

    public static TaskManager getDefault() {

        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {

        return new InMemoryHistoryManager();
    }
}