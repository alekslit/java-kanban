package ru.practicum.task_tracker.manager;

// Класс для собственного непроверяемого исключения:
public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String message) {
        super(message);
    }
}
