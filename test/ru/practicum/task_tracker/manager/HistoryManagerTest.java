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

    private static TaskManager taskManager;
    private static HistoryManager historyManager;
    private static Task task;
    private static Epic epic;
    private static Subtask subtask1;
    private static Subtask subtask2;
    private static LocalDateTime data1;
    private static LocalDateTime data2;
    private static LocalDateTime data3;
    private static int duration;

    @BeforeEach
    public void beforeEach() {
        // Общая тест дата для этого класса: 1 Таск, 1 Эпик, 2 Сабтаска, все задачи добавлены в менеджер,
        // но не добавлены в историю:
        data1 = LocalDateTime.of(2000, 1, 1, 3, 0);
        data2 = LocalDateTime.of(2000, 1, 1, 5, 0);
        data3 = LocalDateTime.of(2000, 1, 1, 1, 0);
        duration = 60;
        taskManager = new InMemoryTasksManager();
        historyManager = taskManager.getHistoryManager();
        task = new Task("Тест Имя1", "Тест Описание1", TaskStatus.NEW, duration, data1);
        taskManager.addNewTask(task);
        epic = new Epic("Тест Имя2", "Тест Описание2");
        taskManager.addNewEpic(epic);
        subtask1 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.NEW, duration, data2, epic.getId());
        taskManager.addNewSubtask(subtask1);
        subtask2 = new Subtask("Тест Имя4", "Тест Описание4",
                TaskStatus.NEW, duration, data3, epic.getId());
        taskManager.addNewSubtask(subtask2);
    }

    @Test
    public void shouldGetTaskHistoryWithAddTask() {
        historyManager.addTask(task);
        historyManager.addTask(subtask1);
        historyManager.addTask(subtask2);
        historyManager.addTask(epic);
        final List<Task> taskHistory = historyManager.getTaskHistory();

        assertEquals(4, taskHistory.size(),
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
            historyManager.addTask(subtask1);
            historyManager.addTask(subtask2);
            historyManager.addTask(epic);
        }
        final List<Task> taskHistory = historyManager.getTaskHistory();

        assertEquals(4, taskHistory.size(),
                "В истории есть дубли задач (shouldGetTaskHistoryWithoutDoubleWhenAdd12Task)");
    }

    /***---Проверка удаления из истории---***/
    @Test
    public void removeTest1() {
        // Удаляем задачу из начала истории и проверяем очерёдность и наличие оставшихся задач в истории:
        historyManager.addTask(task);
        historyManager.addTask(subtask1);
        historyManager.addTask(epic);
        historyManager.addTask(subtask2);

        historyManager.remove(task.getId());
        final List<Task> taskHistory = historyManager.getTaskHistory();

        assertEquals(taskHistory.get(0), subtask1,
                "Задача не удалена из истории или нарушен порядок (removeTest1)");
        assertEquals(taskHistory.get(1), epic,
                "Задача не удалена из истории или нарушен порядок (removeTest1)");
        assertEquals(taskHistory.get(2), subtask2,
                "Задача не удалена из истории или нарушен порядок (removeTest1)");
    }

    @Test
    public void removeTest2() {
        // Удаляем задачу из середины истории и проверяем очерёдность и наличие оставшихся задач в истории:
        historyManager.addTask(task);
        historyManager.addTask(subtask1);
        historyManager.addTask(epic);
        historyManager.addTask(subtask2);

        historyManager.remove(subtask1.getId());
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
        historyManager.addTask(task);
        historyManager.addTask(subtask1);
        historyManager.addTask(epic);
        historyManager.addTask(subtask2);

        historyManager.remove(subtask2.getId());
        final List<Task> taskHistory = historyManager.getTaskHistory();

        assertEquals(taskHistory.get(0), task,
                "Задача не удалена из истории или нарушен порядок (removeTest3)");
        assertEquals(taskHistory.get(1), subtask1,
                "Задача не удалена из истории или нарушен порядок (removeTest3)");
        assertEquals(taskHistory.get(2), epic,
                "Задача не удалена из истории или нарушен порядок (removeTest3)");
    }
}