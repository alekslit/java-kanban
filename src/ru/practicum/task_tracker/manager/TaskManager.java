package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    int generateId();

    ArrayList<Task> getAllTasksList();

    ArrayList<Epic> getAllEpicsList();

    ArrayList<Subtask> getAllSubtasksList();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    Subtask getSubtaskById(int subtaskId);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    Integer addNewSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void deleteTaskById(int taskId);

    void deleteEpicById(int epicId);

    void deleteSubtaskById(Integer subtaskId);

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    void updateEpicStatus(int epicId);

    HistoryManager getHistoryManager();
}
