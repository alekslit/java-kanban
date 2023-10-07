package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    LocalDateTime data1 = LocalDateTime.of(2000, 1, 1, 1, 0);
    LocalDateTime data2 = LocalDateTime.of(2000, 1, 1, 3, 0);
    LocalDateTime data3 = LocalDateTime.of(2000, 1, 1, 5, 0);
    LocalDateTime data4 = LocalDateTime.of(2000, 1, 1, 7, 0);
    int duration = 60;
    public FileBackedTasksManagerTest() {
        super.taskManager = new FileBackedTasksManager(Paths.get("./resources/managerData.csv"));
    }

    @BeforeEach
    public void beforeEach() {
        // Обнуляем менеджер перед каждым новым тестом:
        super.taskManager.deleteAllTasks();
        super.taskManager.deleteAllSubtasks();
        super.taskManager.deleteAllEpics();
    }

    @Test
    public void saveLoadTest1() {
        // Обычная ситуация: все задачи + история + 1 Эпик без подзадач:
        Task task1 = new Task("Тест Имя1", "Тест Описание1",
                TaskStatus.NEW, duration, data1);
        final Integer task1Id = taskManager.addNewTask(task1);
        Task task2 = new Task("Тест Имя2", "Тест Описание2",
                TaskStatus.IN_PROGRESS, duration, data2);
        final Integer task2Id = taskManager.addNewTask(task2);
        Epic epic1 = new Epic("Тест Имя3", "Тест Описание3");
        final int epic1Id = taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("Тест Имя4", "Тест Описание4");
        final int epic2Id = taskManager.addNewEpic(epic2);
        Subtask subtask1 = new Subtask("Тест Имя5", "Тест Описание5",
                TaskStatus.DONE, duration, data3, epic1Id);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя6", "Тест Описание6",
                TaskStatus.NEW, duration, data4, epic1Id);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);
        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task2Id);
        taskManager.getSubtaskById(subtask1Id);
        taskManager.getSubtaskById(subtask2Id);
        taskManager.getEpicById(epic1Id);
        taskManager.getEpicById(epic2Id);

        TaskManager loadTaskManager = FileBackedTasksManager.loadFromFile(
                Paths.get("./resources/managerData.csv"));
        List<Task> taskList = taskManager.getAllTasksList();
        List<Task> loadTaskList = loadTaskManager.getAllTasksList();
        List<Subtask> subtaskList = taskManager.getAllSubtasksList();
        List<Subtask> loadSubtaskList = loadTaskManager.getAllSubtasksList();
        List<Epic> epicList = taskManager.getAllEpicsList();
        List<Epic> loadEpicList = loadTaskManager.getAllEpicsList();
        List<Task> taskHistoryList = taskManager.getHistoryManager().getTaskHistory();
        List<Task> loadTaskHistoryList = loadTaskManager.getHistoryManager().getTaskHistory();
        assertEquals(taskList, loadTaskList,
                "Сохранение/загрузка работают некорректно (saveLoadTest1)");
        assertEquals(subtaskList, loadSubtaskList,
                "Сохранение/загрузка работают некорректно (saveLoadTest1)");
        assertEquals(epicList, loadEpicList,
                "Сохранение/загрузка работают некорректно (saveLoadTest1)");
        assertEquals(taskHistoryList, loadTaskHistoryList,
                "Сохранение/загрузка работают некорректно (saveLoadTest1)");
    }

    @Test
    public void saveLoadTest2() {
        // Пустой список задач и история:
        TaskManager loadTaskManager = FileBackedTasksManager.loadFromFile(
                Paths.get("./resources/managerData.csv"));
        List<Task> taskList = taskManager.getAllTasksList();
        List<Task> loadTaskList = loadTaskManager.getAllTasksList();
        List<Subtask> subtaskList = taskManager.getAllSubtasksList();
        List<Subtask> loadSubtaskList = loadTaskManager.getAllSubtasksList();
        List<Epic> epicList = taskManager.getAllEpicsList();
        List<Epic> loadEpicList = loadTaskManager.getAllEpicsList();
        List<Task> taskHistoryList = taskManager.getHistoryManager().getTaskHistory();
        List<Task> loadTaskHistoryList = loadTaskManager.getHistoryManager().getTaskHistory();
        assertEquals(taskList, loadTaskList,
                "Сохранение/загрузка работают некорректно (saveLoadTest2)");
        assertEquals(subtaskList, loadSubtaskList,
                "Сохранение/загрузка работают некорректно (saveLoadTest2)");
        assertEquals(epicList, loadEpicList,
                "Сохранение/загрузка работают некорректно (saveLoadTest2)");
        assertEquals(taskHistoryList, loadTaskHistoryList,
                "Сохранение/загрузка работают некорректно (saveLoadTest2)");
    }

    @Test
    public void saveLoadTest3() {
        // Пустая только история:
        Task task1 = new Task("Тест Имя1", "Тест Описание1",
                TaskStatus.NEW, duration, data1);
        final Integer task1Id = taskManager.addNewTask(task1);
        Task task2 = new Task("Тест Имя2", "Тест Описание2",
                TaskStatus.IN_PROGRESS, duration, data2);
        final Integer task2Id = taskManager.addNewTask(task2);
        Epic epic1 = new Epic("Тест Имя3", "Тест Описание3");
        final int epic1Id = taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("Тест Имя4", "Тест Описание4");
        final int epic2Id = taskManager.addNewEpic(epic2);
        Subtask subtask1 = new Subtask("Тест Имя5", "Тест Описание5",
                TaskStatus.DONE, duration, data3, epic1Id);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя6", "Тест Описание6",
                TaskStatus.NEW, duration, data4, epic1Id);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        TaskManager loadTaskManager = FileBackedTasksManager.loadFromFile(
                Paths.get("./resources/managerData.csv"));
        List<Task> taskList = taskManager.getAllTasksList();
        List<Task> loadTaskList = loadTaskManager.getAllTasksList();
        List<Subtask> subtaskList = taskManager.getAllSubtasksList();
        List<Subtask> loadSubtaskList = loadTaskManager.getAllSubtasksList();
        List<Epic> epicList = taskManager.getAllEpicsList();
        List<Epic> loadEpicList = loadTaskManager.getAllEpicsList();
        List<Task> taskHistoryList = taskManager.getHistoryManager().getTaskHistory();
        List<Task> loadTaskHistoryList = loadTaskManager.getHistoryManager().getTaskHistory();
        assertEquals(taskList, loadTaskList,
                "Сохранение/загрузка работают некорректно (saveLoadTest3)");
        assertEquals(subtaskList, loadSubtaskList,
                "Сохранение/загрузка работают некорректно (saveLoadTest3)");
        assertEquals(epicList, loadEpicList,
                "Сохранение/загрузка работают некорректно (saveLoadTest3)");
        assertEquals(taskHistoryList, loadTaskHistoryList,
                "Сохранение/загрузка работают некорректно (saveLoadTest3)");
    }
}
