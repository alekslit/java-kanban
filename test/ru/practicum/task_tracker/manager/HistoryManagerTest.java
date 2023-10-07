package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {

    private static final TaskManager taskManager = Managers.getDefault();
    private static final HistoryManager historyManager = taskManager.getHistoryManager();
    private static Task task;
    private static Subtask subtask;
    private static Epic epic;
    LocalDateTime data1 = LocalDateTime.of(2000, 1, 1, 1, 0);
    LocalDateTime data2 = LocalDateTime.of(2000, 1, 1, 3, 0);
    LocalDateTime data3 = LocalDateTime.of(2000, 1, 1, 5, 0);
    int duration = 60;

    @BeforeEach
    public void beforeEach() {
        // Обнуляем менеджер и историю + создаём и добавляем задачи перед каждым тестом:
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
        taskManager.deleteAllEpics();
        task = new Task("Тест Имя", "Тест Описание", TaskStatus.NEW, duration, data1);
        final Integer taskId = taskManager.addNewTask(task);
        epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        subtask = new Subtask("Тест Имя2", "Тест Описание2", TaskStatus.NEW, duration, data2, epicId);
        final Integer subtaskId = taskManager.addNewSubtask(subtask);
    }

    @Test
    public void shouldGetTaskHistoryWithAddTask() {
        historyManager.addTask(task);
        historyManager.addTask(subtask);
        historyManager.addTask(epic);

        final List<Task> taskHistory = historyManager.getTaskHistory();
        assertEquals(3, taskHistory.size(),
                "История пустая, либо количество задач не совпадает (shouldAddTaskAndGetTaskHistory)");
    }

    @Test
    public void shouldGetIsEmptyTaskHistoryWhenNoAddTask() {
        final List<Task> taskHistory = historyManager.getTaskHistory();
        assertEquals(0, taskHistory.size(),
                "Ошибка: история не пустая(shouldGetIsEmptyTaskHistoryWhenNoAddTask)");
    }

    @Test
    public void shouldGetTaskHistoryWithoutDoubleWhenAdd9Task() {
        // Проверим дублируются ли задачи в историю:
        for (int i = 1; i <= 3; i++) {
            historyManager.addTask(task);
            historyManager.addTask(subtask);
            historyManager.addTask(epic);
        }

        final List<Task> taskHistory = historyManager.getTaskHistory();
        assertEquals(3, taskHistory.size(),
                "В истории есть дубли задач (shouldGetTaskHistoryWithoutDoubleWhenAdd12Task)");
    }

    /***---Проверка удаления из истории---***/
    @Test
    public void removeTest1() {
        // Удаляем задачу из начала истории и проверяем очерёдность и наличие оставшихся задач в истории:
        final Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.NEW, duration, data3, epic.getId());
        final Integer subtaskId = taskManager.addNewSubtask(subtask2);
        historyManager.addTask(task);
        historyManager.addTask(subtask);
        historyManager.addTask(epic);
        historyManager.addTask(subtask2);

        historyManager.remove(task.getId());
        final List<Task> taskHistory = historyManager.getTaskHistory();
        assertEquals(taskHistory.get(0), subtask,
                "Задача не удалена из истории или нарушен порядок (removeTest1)");
        assertEquals(taskHistory.get(1), epic,
                "Задача не удалена из истории или нарушен порядок (removeTest1)");
        assertEquals(taskHistory.get(2), subtask2,
                "Задача не удалена из истории или нарушен порядок (removeTest1)");
    }

    @Test
    public void removeTest2() {
        // Удаляем задачу из середины истории и проверяем очерёдность и наличие оставшихся задач в истории:
        final Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.NEW, duration, data3, epic.getId());
        final Integer subtaskId = taskManager.addNewSubtask(subtask2);
        historyManager.addTask(task);
        historyManager.addTask(subtask);
        historyManager.addTask(epic);
        historyManager.addTask(subtask2);

        historyManager.remove(subtask.getId());
        final List<Task> taskHistory = historyManager.getTaskHistory();
        assertEquals(taskHistory.get(0), task,
                "Задача не удалена из истории или нарушен порядок (removeTest2)");
        assertEquals(taskHistory.get(1), epic,
                "Задача не удалена из истории или нарушен порядок (removeTest2)");
        assertEquals(taskHistory.get(2), subtask2,
                "Задача не удалена из истории или нарушен порядок (removeTest2)");
    }

    @Test
    public void removeTest3() {
        // Удаляем последнюю задачу в истории и проверяем очерёдность и наличие оставшихся задач в истории:
        final Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.NEW, duration, data3, epic.getId());
        final Integer subtaskId = taskManager.addNewSubtask(subtask2);
        historyManager.addTask(task);
        historyManager.addTask(subtask);
        historyManager.addTask(epic);
        historyManager.addTask(subtask2);

        historyManager.remove(subtask2.getId());
        final List<Task> taskHistory = historyManager.getTaskHistory();
        assertEquals(taskHistory.get(0), task,
                "Задача не удалена из истории или нарушен порядок (removeTest3)");
        assertEquals(taskHistory.get(1), subtask,
                "Задача не удалена из истории или нарушен порядок (removeTest3)");
        assertEquals(taskHistory.get(2), epic,
                "Задача не удалена из истории или нарушен порядок (removeTest3)");
    }
}
