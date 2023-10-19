package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTasksManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Set<Task> sortedTaskList = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected int currentIdNumber = 0;

    // Генерируем новый ID:
    @Override
    public int generateId() {
        return ++currentIdNumber;
    }

    // Получение списка всех Тасков:
    @Override
    public List<Task> getAllTasksList() {
        if (tasks.isEmpty()) {
            System.out.println("tasks = null");
            return new ArrayList<>();
        }

        return new ArrayList<>(tasks.values());
    }

    // Получение списка всех Эпиков:
    @Override
    public List<Epic> getAllEpicsList() {
        if (epics.isEmpty()) {
            System.out.println("epics = null");
            return new ArrayList<>();
        }

        return new ArrayList<>(epics.values());
    }

    // Получение списка всех Сабтасков:
    @Override
    public List<Subtask> getAllSubtasksList() {
        if (subtasks.isEmpty()) {
            System.out.println("subtasks = null");
            return new ArrayList<>();
        }

        return new ArrayList<>(subtasks.values());
    }

    // Удаление всех Тасков:
    @Override
    public void deleteAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("tasks = null");
            return;
        }

        // Очищаем историю просмотров от Тасков + чистим отсортированный список:
        for (Integer id: tasks.keySet()) {
            historyManager.remove(id);
            sortedTaskList.remove(tasks.get(id));
        }

        tasks.clear();
    }

    // Удаление всех Эпиков:
    @Override
    public void deleteAllEpics() {
        if (epics.isEmpty()) {
            System.out.println("epics = null");
            return;
        }

        // Очищаем историю просмотров от Эпиков + удаляем сами Эпики:
        for (Integer id: epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();

        // Дополнительно удаляем все Сабтаски, т.к. без Эпиков не должно быть и их Сабтасков:
        if (subtasks.isEmpty()) {
            System.out.println("subtasks = null");
            return;
        }

        // Очищаем историю просмотров от Сабтасков + удаляем сами Сабтаски + чистим отсортированный список:
        for (Integer id: subtasks.keySet()) {
            historyManager.remove(id);
            sortedTaskList.remove(subtasks.get(id));
        }
        deleteAllSubtasks();
    }

    // Удаление всех Сабтасков:
    @Override
    public void deleteAllSubtasks() {
        if (subtasks.isEmpty()) {
            System.out.println("subtasks = null");
            return;
        }

        // Очищаем историю просмотров от Сабтасков и удаляем сами Сабтаски + чистим отсортированный список:
        for (Integer id: subtasks.keySet()) {
            historyManager.remove(id);
            sortedTaskList.remove(subtasks.get(id));
        }
        subtasks.clear();

        // Удаляем ID всех Сабтасков из Эпиков, и проверяем статус всех Эпиков + проверка полей времени Эпиков:
        for (Integer key: epics.keySet()) {
            epics.get(key).getSubtaskIds().clear();
            updateEpicStatus(key);
            updateEpicTimeDuration(key);
        }
    }

    // Получение по ID определённого Таска + сохранение Таска в истории просмотров:
    @Override
    public Task getTaskById(int taskId) {
        if (tasks.get(taskId) == null) {
            System.out.println("Нет Task с id = " + taskId);
            return null;
        }

        // Добавляем Таск в историю просмотров:
        historyManager.addTask(tasks.get(taskId));

        return tasks.get(taskId);
    }

    // Получение по ID определённого Эпика + сохранение Эпика в истории просмотров:
    @Override
    public Epic getEpicById(int epicId) {
        if (epics.get(epicId) == null) {
            System.out.println("Нет Epic с id = " + epicId);
            return null;
        }

        // Добавляем Эпик в историю просмотров:
        historyManager.addTask(epics.get(epicId));

        return epics.get(epicId);
    }

    // Получение по ID определённого Сабтаска + сохранение Сабтаска в истории просмотров:
    @Override
    public Subtask getSubtaskById(int subtaskId) {
        if (subtasks.get(subtaskId) == null) {
            System.out.println("Нет Subtask с id = " + subtaskId);
            return null;
        }

        // Добавляем Сабтаск в историю просмотров:
        historyManager.addTask(subtasks.get(subtaskId));

        return subtasks.get(subtaskId);
    }

    // Добавляем новый Таск:
    @Override
    public Integer addNewTask(Task task) {
        // Проверим пересечение по времени:
        if (isIntersectionsTasksByTime(task)) {
            return null;
        }
        // Генерируем новый ID:
        task.setId(generateId());
        // Записываем Таск в хеш-таблицу + добавляем в отсортированный список:
        tasks.put(task.getId(), task);
        sortedTaskList.add(task);

        return task.getId();
    }

    // Добавляем новый Эпик:
    @Override
    public int addNewEpic(Epic epic) {
        // Если создаём новый Эпик через клиент:
        if (epic.getStatus() == null && epic.getDuration() == null
                && epic.getStartTime() == null && epic.getSubtaskIds() == null) {
            epic.setStatus(TaskStatus.NEW);
            epic.setDuration(0);
            epic.setStartTime(LocalDateTime.now());
            epic.setSubtaskIds(new ArrayList<>());
        }
        // Генерируем новый ID:
        epic.setId(generateId());
        // Записываем Эпик в хеш-таблицу:
        epics.put(epic.getId(), epic);

        return epic.getId();
    }

    // Добавляем новый Сабтаск:
    @Override
    public Integer addNewSubtask(Subtask subtask) {
        // Находим нужный Эпик:
        Epic epic = epics.get(subtask.getEpicId());
        // Проверяем на null:
        if (epic == null) {
            System.out.println("Нет Epic c id = " + subtask.getEpicId());
            return null;
        }
        // Проверим пересечение по времени:
        if (isIntersectionsTasksByTime(subtask)) {
            return null;
        }

        // Генерируем новый ID:
        subtask.setId(generateId());
        // Записываем Сабтаск в хеш-таблицу + добавляем в отсортированный список:
        subtasks.put(subtask.getId(), subtask);
        sortedTaskList.add(subtask);
        // Добавляем Эпику ID его Сабтаска:
        epic.addSubtaskId(subtask.getId());
        // Проверяем статус Эпика + проверяем время:
        updateEpicStatus(subtask.getEpicId());
        updateEpicTimeDuration(subtask.getEpicId());

        return subtask.getId();
    }

    // Обновление Таска:
    @Override
    public void updateTask(Task task) {
        // Проверим, существует ли Таск, который мы хотим обновить:
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            System.out.println("Нет Task с id = " + task.getId());
            return;
        }

        // Удаляем старую задачу из отсортированного списка и добавляем в него обновлённую
        // + проверим пересечение по времени:
        sortedTaskList.remove(savedTask);
        if (isIntersectionsTasksByTime(task)) {
            return;
        }
        sortedTaskList.add(task);

        // Обновляем Таск:
        tasks.put(task.getId(), task);
    }

    // Обновление Сабтаска:
    @Override
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

        // Удаляем старую задачу из отсортированного списка и добавляем в него обновлённую
        // + проверим пересечение по времени:
        sortedTaskList.remove(savedSubtask);
        if (isIntersectionsTasksByTime(subtask)) {
            return;
        }
        sortedTaskList.add(subtask);

        // Обновляем Сабтаск:
        subtasks.put(subtask.getId(), subtask);
        // Проверяем статус Эпика + проверяем время:
        updateEpicStatus(subtask.getEpicId());
        updateEpicTimeDuration(subtask.getEpicId());
    }

    // Обновление Эпика:
    @Override
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
    @Override
    public void deleteTaskById(int taskId) {
        // Проверяем, существует ли Таск:
        if (tasks.get(taskId) == null) {
            System.out.println("Нет Task с id = " + taskId);
            return;
        }

        // Удаляем Таск из истории просмотров + удаляем из отсортированного списка:
        historyManager.remove(taskId);
        sortedTaskList.remove(tasks.get(taskId));
        // Удаляем Таск:
        tasks.remove(taskId);
    }

    // Удаление Эпика по ID:
    @Override
    public void deleteEpicById(int epicId) {
        // Проверяем, существует ли Эпик:
        if (epics.get(epicId) == null) {
            System.out.println("Нет Epic с id = " + epicId);
            return;
        }

        // Удаляем Сабтаски Эпика, который мы хотим удалить + удаляем сабтаски из истории просмотров
        // + чистим отсортированный список:
        if (epics.get(epicId).getSubtaskIds() != null) {
            for (int key: epics.get(epicId).getSubtaskIds()) {
                sortedTaskList.remove(subtasks.get(key));
                subtasks.remove(key);
                historyManager.remove(key);
            }
        } else {
            System.out.println("subtaskIds = null");
        }

        // Удаляем Эпик из истории просмотров:
        historyManager.remove(epicId);
        // Удаляем Эпик:
        epics.remove(epicId);
    }

    // Удаление Сабтаска по ID:
    @Override
    public void deleteSubtaskById(Integer subtaskId) {
        // Проверяем, существует ли Сабтаск:
        if (subtasks.get(subtaskId) == null) {
            System.out.println("Нет Subtask с id = " + subtaskId);
            return;
        }

        // Записываем в переменную Эпик ID, из Сабтаска:
        int epicId = epics.get(subtasks.get(subtaskId).getEpicId()).getId();
        // Удаляем Сабтаск из истории просмотров + чистим отсортированный список:
        historyManager.remove(subtaskId);
        sortedTaskList.remove(subtasks.get(subtaskId));
        // Удаляем сам Сабтаск, а потом его ID из эпика:
        subtasks.remove(subtaskId);
        epics.get(epicId).getSubtaskIds().remove(subtaskId);
        // Проверяем статус Эпика + проверяем время:
        updateEpicStatus(epicId);
        updateEpicTimeDuration(epicId);
    }

    // Получение списка Сабтасков определённого эпика:
    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        // Создаём список Сабтасков для наполнения:
        List<Subtask> epicSubtasks = new ArrayList<>();

        // Проверяем, существует ли эпик, и есть ли у него Сабтаски:
        if (!epics.containsKey(epicId) || epics.get(epicId).getSubtaskIds().isEmpty()) {
            System.out.println("Epic = null, или subTaskIds = null");
            return new ArrayList<>();
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
    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = epic.getSubtaskIds();

        // Проверяем на пустоту:
        if (subtaskIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        TaskStatus status = null;
        for (int subtaskId: subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);

            // Если первый проход, то сохраняем статус первой подзадачи:
            if (status == null) {
                status = subtask.getStatus();
                continue;
            }
            // Проверяем на равенство и IN_PROGRESS:
            if (status.equals(subtask.getStatus()) && !status.equals(TaskStatus.IN_PROGRESS)) {
                continue;
            }

            epic.setStatus(TaskStatus.IN_PROGRESS);
            return;
        }

        epic.setStatus(status);
    }

    @Override
    // Возвращаем объект со списком просмотренных задач:
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    // Метод рассчитывает поля duration, startTime и endTime Эпика:
    public void updateEpicTimeDuration(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = epic.getSubtaskIds();

        // Проверяем на пустоту:
        if (subtaskIds.isEmpty()) {
            return;
        }

        int duration = 0;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        for (int subtaskId: subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            duration += subtask.getDuration();

            if (startTime == null || endTime == null) {
                startTime = subtask.getStartTime();
                endTime = subtask.getEndTime();
            }
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }

        epic.setDuration(duration);
        epic.setEndTime(endTime);
        epic.setStartTime(startTime);
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        if (sortedTaskList.isEmpty()) {
            System.out.println("sortedTaskList = null");
            return new TreeSet<>();
        }
        return sortedTaskList;
    }

    @Override
    // Метод для проверки пересечений задач по времени:
    public boolean isIntersectionsTasksByTime(Task task) {
        Set<Task> sortedTaskList = getPrioritizedTasks();
        if(sortedTaskList.isEmpty()) {
            return false;
        }

        for (Task sortedTask: sortedTaskList) {
            boolean taskStartIsBeforeSortedTaskStart = task.getStartTime().isBefore(sortedTask.getStartTime());
            boolean taskStartIsEqualsSortedTaskStart = task.getStartTime().equals(sortedTask.getStartTime());
            boolean taskStartIsBeforeSortedTaskEnd = task.getStartTime().isBefore(sortedTask.getEndTime());
            boolean taskEndIsAfterSortedTaskStart = task.getEndTime().isAfter(sortedTask.getStartTime());

            if ((taskStartIsBeforeSortedTaskStart || taskStartIsEqualsSortedTaskStart
                    || taskStartIsBeforeSortedTaskEnd) && taskEndIsAfterSortedTaskStart) {
                System.out.println("Невозможно создать задачу/подзадачу, "
                        + "есть пересечение по времени выполнения с текущими задачами/подзадачами");
                return true;
            }
        }

        return false;
    }

    @Override
    public void deleteAll() {
        deleteAllTasks();
        deleteAllSubtasks();
        deleteAllEpics();
    }
}