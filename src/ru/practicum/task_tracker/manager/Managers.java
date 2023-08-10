package ru.practicum.task_tracker.manager;

// Утилитарный класс для создания менеджеров:
public class Managers {

    public TaskManager getDefault() {

        return new InMemoryTaskManager();
    }

    public HistoryManager getDefaultHistory() {

        return new InMemoryHistoryManager();
    }
}
