package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager(Paths.get("./resources/managerData.csv"));
        init();
    }

    /*---Тесты проверки сохранения и загрузки---*/
    @Test
    public void saveLoadTest1() {
        // Обычная ситуация: все типы задач + история + 1 Эпик без подзадач:
        final Epic epic2 = new Epic("Тест Имя6", "Тест Описание6");
        final int epic2Id = taskManager.addNewEpic(epic2);
        taskManager.getTaskById(task.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getEpicById(epic.getId());
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

    /*---Тесты для выбрасываемого исключения---*/
    @Test
    public void shouldGetExceptionByLoadWithFakeFileName() {
        // Передадим ошибочное имя файла для загрузки:
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> {
                    TaskManager loadTaskManager = FileBackedTasksManager.loadFromFile(
                            Paths.get("fakeFileName"));
                }
        );

        assertEquals("Ошибка чтения файла: fakeFileName "
                + "(Не удается найти указанный файл)", exception.getMessage());
    }

    @Test
    public void shouldGetExceptionBySaveWithLockFileAccess() {
        // Заблокируем доступ к файлу, чтобы проверить выбрасываемое исключение при попытке записи:
        taskManager.deleteAll();
        Path lockFile = Paths.get("./resources/managerData.csv");
        try (FileChannel channel = FileChannel.open(lockFile, StandardOpenOption.APPEND)) {
        channel.lock();

        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> taskManager.addNewTask(task));

        assertEquals("Ошибка записи файла: Процесс не может получить доступ к файлу,"
                + " так как часть этого файла заблокирована другим процессом", exception.getMessage());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при попытке заблокировать"
                    + " доступ к файлу для теста: " + e.getMessage());
        }
    }
}
