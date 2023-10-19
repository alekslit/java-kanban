package ru.practicum.task_tracker.manager.http_task_manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.manager.TaskManagerTest;
import ru.practicum.task_tracker.manager.kv_server.KVServer;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        taskManager = new HttpTaskManager();
        init();
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    public void saveAndLoadTest() {
        // Пробуем загрузить данные создав новый менеджер, и сравнить два менеджера между собой:
        // Конструктор менеджера содержит вызов метода загрузки load():
        HttpTaskManager loadHttpTaskManager = new HttpTaskManager();
        List<Task> taskList = taskManager.getAllTasksList();
        List<Task> loadTaskList = loadHttpTaskManager.getAllTasksList();
        List<Subtask> subtaskList = taskManager.getAllSubtasksList();
        List<Subtask> loadSubtaskList = loadHttpTaskManager.getAllSubtasksList();
        List<Epic> epicList = taskManager.getAllEpicsList();
        List<Epic> loadEpicList = loadHttpTaskManager.getAllEpicsList();
        List<Task> taskHistoryList = taskManager.getHistoryManager().getTaskHistory();
        List<Task> loadTaskHistoryList = loadHttpTaskManager.getHistoryManager().getTaskHistory();
        Set<Task> sortedTaskList = taskManager.getPrioritizedTasks();
        Set<Task> loadSortedTaskList = loadHttpTaskManager.getPrioritizedTasks();

        assertEquals(taskList, loadTaskList,
                "Сохранение/загрузка работают некорректно (saveAndLoadTest)");
        assertEquals(subtaskList, loadSubtaskList,
                "Сохранение/загрузка работают некорректно (saveAndLoadTest)");
        assertEquals(epicList, loadEpicList,
                "Сохранение/загрузка работают некорректно (saveAndLoadTest)");
        assertEquals(taskHistoryList, loadTaskHistoryList,
                "Сохранение/загрузка работают некорректно (saveAndLoadTest)");
        assertEquals(sortedTaskList, loadSortedTaskList,
                "Сохранение/загрузка работают некорректно (saveAndLoadTest)");
    }
}