package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    /*---Основные методы Тасков---*/
    List<Task> getAllTasksList();

    Task getTaskById(int taskId);

    Integer addNewTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int taskId);

    void deleteAllTasks();


    /*---Основные методы Сабтасков--*/
    List<Subtask> getAllSubtasksList();

    Subtask getSubtaskById(int subtaskId);

    Integer addNewSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(Integer subtaskId);

    void deleteAllSubtasks();

    /*---Основные методы Эпиков---*/
    List<Epic> getAllEpicsList();

    Epic getEpicById(int epicId);

    int addNewEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int epicId);

    void deleteAllEpics();

    /*---Основные общие методы---*/
    List<Subtask> getEpicSubtasks(int epicId);

    HistoryManager getHistoryManager();

    Set<Task> getPrioritizedTasks();

    void deleteAll();

    /*---Прочие/вспомогательные методы---*/
    int generateId();

    void updateEpicStatus(int epicId);

    void updateEpicTimeDuration(int epicId);

    boolean isIntersectionsTasksByTime(Task task);
}