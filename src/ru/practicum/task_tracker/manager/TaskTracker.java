package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskTracker {
    final private HashMap<Integer, Task> tasks = new HashMap<>();
    final private HashMap<Integer, Epic> epics = new HashMap<>();
    final private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int currentIdNumber = 0;

    // Генерируем новый ID:
    private int generateId() {

        return currentIdNumber++;
    }

    // Получение списка всех Тасков:
    public ArrayList<Task> getAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("tasks = null");
            return new ArrayList<>();
        }

        return new ArrayList<>(tasks.values());
    }

    // Получение списка всех Эпиков:
    public ArrayList<Epic> getAllEpics() {
        if (epics.isEmpty()) {
            System.out.println("epics = null");
            return new ArrayList<>();
        }

        return new ArrayList<>(epics.values());
    }

    // Получение списка всех Сабтасков:
    public ArrayList<Subtask> getAllSubtasks() {
        if (subtasks.isEmpty()) {
            System.out.println("subtasks = null");
            return new ArrayList<>();
        }

        return new ArrayList<>(subtasks.values());
    }

    // Удаление всех Тасков:
    public void deleteAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("tasks = null");
            return;
        }

        tasks.clear();
    }

    // Удаление всех Эпиков:
    public void deleteAllEpics() {
        if (epics.isEmpty()) {
            System.out.println("epics = null");
            return;
        }
        epics.clear();

        // Дополнительно удаляем все Сабтаски, т.к. без Эпиков не должно быть и их Сабтасков:
        if (subtasks.isEmpty()) {
            System.out.println("subtasks = null");
            return;
        }
        deleteAllSubtasks();
    }

    // Удаление всех Сабтасков:
    public void deleteAllSubtasks() {
        if (subtasks.isEmpty()) {
            System.out.println("subtasks = null");
            return;
        }
        subtasks.clear();

        // Удаляем ID всех Сабтасков из Эпиков, и проверяем статус всех Эпиков:
        for (Integer key: epics.keySet()) {
            epics.get(key).getSubtaskIds().clear();
            updateEpicStatus(key);
        }
    }

    // Получение по ID определённого Таска:
    public Task getTaskById(int taskId) {
        if (tasks.get(taskId) == null) {
            System.out.println("Нет Task с id = " + taskId);
            return null;
        }

        return tasks.get(taskId);
    }

    // Получение по ID определённого Эпика:
    public Epic getEpicById(int epicId) {
        if (epics.get(epicId) == null) {
            System.out.println("Нет Epic с id = " + epicId);
            return null;
        }

        return epics.get(epicId);
    }

    // Получение по ID определённого Сабтаска:
    public Subtask getSubtaskById(int subtaskId) {
        if (subtasks.get(subtaskId) == null) {
            System.out.println("Нет Subtask с id = " + subtaskId);
            return null;
        }

        return subtasks.get(subtaskId);
    }

    // Добавляем новый Таск:
    public int addNewTask(Task task) {
        // Генерируем новый ID:
        task.setId(generateId());
        // Записываем Таск в хеш-таблицу:
        tasks.put(task.getId(), task);

        return task.getId();
    }

    // Добавляем новый Эпик:
    public int addNewEpic(Epic epic) {
        // Генерируем новый ID:
        epic.setId(generateId());
        // Записываем Эпик в хеш-таблицу:
        epics.put(epic.getId(), epic);

        return epic.getId();
    }

    // Добавляем новый Сабтаск:
    public Integer addNewSubtask(Subtask subtask) {
        // Находим нужный Эпик:
        Epic epic = epics.get(subtask.getEpicId());
        // Проверяем на null:
        if (epic == null) {
            System.out.println("Нет Epic c id = " + subtask.getEpicId());
            return null;
        }

        // Генерируем новый ID:
        subtask.setId(generateId());
        // Записываем Сабтаск в хеш-таблицу:
        subtasks.put(subtask.getId(), subtask);

        // Добавляем Эпику ID его Сабтаска:
        epic.addSubtaskId(subtask.getId());
        // Проверяем статус Эпика:
        updateEpicStatus(subtask.getEpicId());

        return subtask.getId();
    }

    // Обновление Таска:
    public void updateTask(Task task) {
        // Проверим, существует ли Таск, который мы хотим обновить:
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            System.out.println("Нет Task с id = " + task.getId());
            return;
        }

        // Обновляем Таск:
        tasks.put(task.getId(), task);
    }

    // Обновление Сабтаска:
    public void updateSubtask(Subtask subtask) {
        // Проверим, существует ли Сабтаск, который мы хотим обновить:
        Subtask savedSubtask = subtasks.get(subtask.getId());
        if (savedSubtask == null) {
            System.out.println("Нет Subtask с id = " + subtask.getId());
            return;
        }

        // Проверить, что существует Эпик, с которым Сабтаски пришли:
        Epic savedEpic = epics.get(subtask.getEpicId());
        if (savedEpic == null) {
            System.out.println("Нет Epic с id = " + subtask.getEpicId());
            return;
        }

        // Обновляем Сабтаск:
        subtasks.put(subtask.getId(), subtask);

        // Проверяем статус Эпика:
        updateEpicStatus(subtask.getEpicId());
    }

    // Обновление Эпика:
    public void updateEpic(Epic epic) {
        // Проверим, существует ли Эпик, который мы хотим обновить:
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            System.out.println("Нет Epic с id = " + epic.getId());
            return;
        }

        // Обновляем только Имя и Описание:
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    // Удаление Таска по ID:
    public void deleteTaskById(int taskId) {
        // Проверяем, существует ли Таск:
        if (tasks.get(taskId) == null) {
            System.out.println("Нет Task с id = " + taskId);
            return;
        }

        // Удаляем Таск:
        tasks.remove(taskId);
    }

    // Удаление Эпика по ID:
    public void deleteEpicById(int epicId) {
        // Проверяем, существует ли Эпик:
        if (epics.get(epicId) == null) {
            System.out.println("Нет Epic с id = " + epicId);
            return;
        }

        // Удаляем Сабтаски Эпика, который мы хотим удалить:
        if (epics.get(epicId).getSubtaskIds() != null) {
            for (int key: epics.get(epicId).getSubtaskIds()) {
                subtasks.remove(key);
            }
        } else {
            System.out.println("subtaskIds = null");
        }

        // Удаляем Эпик:
        epics.remove(epicId);
    }

    // Удаление Сабтаска по ID:
    public void deleteSubtaskById(Integer subtaskId) {
        // Проверяем, существует ли Сабтаск:
        if (subtasks.get(subtaskId) == null) {
            System.out.println("Нет Subtask с id = " + subtaskId);
            return;
        }

        // Записываем в переменную Эпик ID, из Сабтаска:
        int epicId = epics.get(subtasks.get(subtaskId).getEpicId()).getId();

        // Удаляем сам Сабтаск, а потом его ID из эпика:
        subtasks.remove(subtaskId);
        epics.get(epicId).getSubtaskIds().remove(subtaskId);

        // Проверяем статус Эпика:
        updateEpicStatus(epicId);
    }

    // Получение списка Сабтасков определённого эпика:
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        // Создаём список Сабтасков для наполнения:
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();

        // Проверяем, существует ли эпик, и есть ли у него Сабтаски:
        if (!epics.containsKey(epicId) || epics.get(epicId).getSubtaskIds().isEmpty()) {
            System.out.println("Epic = null, или subTaskIds = null");
            return null;
        }

        // Наполняем список Сабтасками:
        for (Subtask subtask: subtasks.values()) {
            for (int subtaskId: epics.get(epicId).getSubtaskIds()) {
                if (subtask.getId() == subtaskId) {
                    epicSubtasks.add(subtask);
                }
            }
        }

        return epicSubtasks;
    }

    // Проверка статуса Эпика:
    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();

        // Проверяем на пустоту:
        if (subtaskIds.isEmpty()) {
            epic.setStatus("NEW");
            return;
        }

        String status = null;
        for (int subtaskId: subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);

            // Если первый проход, то сохраняем статус первой подзадачи:
            if (status == null) {
                status = subtask.getStatus();
                continue;
            }

            // Проверяем на равенство и IN_PROGRESS:
            if (status.equals(subtask.getStatus()) && !status.equals("IN_PROGRESS")) {
                continue;
            }

            epic.setStatus("IN_PROGRESS");
            return;
        }

        epic.setStatus(status);
    }
}