package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    int generateId();

    List<Task> getAllTasksList();

    List<Epic> getAllEpicsList();

    List<Subtask> getAllSubtasksList();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    Subtask getSubtaskById(int subtaskId);

    Integer addNewTask(Task task);

    int addNewEpic(Epic epic);

    Integer addNewSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void deleteTaskById(int taskId);

    void deleteEpicById(int epicId);

    void deleteSubtaskById(Integer subtaskId);

    List<Subtask> getEpicSubtasks(int epicId);

    void updateEpicStatus(int epicId);

    HistoryManager getHistoryManager();

    Set<Task> getPrioritizedTasks();
}