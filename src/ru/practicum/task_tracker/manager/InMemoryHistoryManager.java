package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    // Список для хранения истории просмотренных задач:
    private final List<Task> taskHistory;
    // Константа для хранения максимального размера списка истории просмотров:
    private final static int MAX_SIZE_OF_TASK_HISTORY = 10;

    public InMemoryHistoryManager() {

        taskHistory = new ArrayList<>();
    }

    // Помечаем задачи как просмотренные, максимум 10 последних:
    @Override
    public void addTask(Task task) {

        // Проверим объект типа Task на null:
        if (task == null) {
            System.out.println("task = null");
            return;
        }

        if (taskHistory.size() < MAX_SIZE_OF_TASK_HISTORY) {
            taskHistory.add(task);
        } else {
            taskHistory.remove(0);
            taskHistory.add(task);
        }
    }

    // Возвращаем список просмотренных задач:
    @Override
    public List<Task> getTaskHistory() {

        // Возвращаем копию, передав коллекцию в конструктор:
        return new ArrayList<>(taskHistory);
    }
}