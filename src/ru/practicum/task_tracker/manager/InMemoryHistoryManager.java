package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    // Список для хранения истории просмотренных задач:
    private final List<Task> taskHistory;

    public InMemoryHistoryManager() {
        taskHistory = new ArrayList<>();
    }

    // Помечаем задачи как просмотренные, максимум 10 последних:
    @Override
    public void addTask(Task task) {

        if (taskHistory.size() < 10) {
            taskHistory.add(task);
        } else {
            taskHistory.remove(0);
            taskHistory.add(task);
        }
    }

    // Возвращаем список просмотренных задач:
    @Override
    public List<Task> getTaskHistory() {

        return taskHistory;
    }
}
