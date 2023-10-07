package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract public class TaskManagerTest<T extends TaskManager> {


    protected T taskManager;
    LocalDateTime data1 = LocalDateTime.of(2000, 1, 1, 1, 0);
    LocalDateTime data2 = LocalDateTime.of(2000, 1, 1, 3, 0);
    int duration = 60;

    @Test
    public void shouldAddNewTask() {
        Task task = new Task("Тест Имя", "Тест Описание", TaskStatus.NEW, duration, data1);
        final Integer taskId = taskManager.addNewTask(task);

        final List<Task> taskList = taskManager.getAllTasksList();
        assertNotNull(taskList, "Список пуст, задача не добавлена (shouldAddNewTask)");
    }

    @Test
    public void shouldGetAllTasksList() {
        Task task1 = new Task("Тест Имя1", "Тест Описание1", TaskStatus.NEW, duration, data1);
        final Integer task1Id = taskManager.addNewTask(task1);
        Task task2 = new Task("Тест Имя2", "Тест Описание2", TaskStatus.NEW, duration, data2);
        final Integer task2Id = taskManager.addNewTask(task2);

        final List<Task> taskList = taskManager.getAllTasksList();
        assertEquals(2, taskList.size(),
                "Неверное количество задач в списке (shouldGetAllTasksList)");
    }

    @Test
    public void shouldGetNullWhenTasksListIsEmpty() {
        assertEquals(0, taskManager.getAllTasksList().size(),
                "Список не пуст (shouldGetNullWhenTasksListIsEmpty)");
    }

    @Test
    public void shouldDeleteAllTasks() {
        Task task1 = new Task("Тест Имя1", "Тест Описание1", TaskStatus.NEW, duration, data1);
        final Integer task1Id = taskManager.addNewTask(task1);
        Task task2 = new Task("Тест Имя2", "Тест Описание2", TaskStatus.NEW, duration, data2);
        final Integer task2Id = taskManager.addNewTask(task2);

        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasksList().size(),
                "Список не пуст (shouldDeleteAllTasks)");
    }

    @Test
    public void shouldGetTaskById() {
        Task task = new Task("Тест Имя", "Тест Описание", TaskStatus.NEW, duration, data1);
        final Integer taskId = taskManager.addNewTask(task);

        Task taskById = taskManager.getTaskById(taskId);
        assertEquals(task, taskById, "Задачи не совпали (shouldGetTaskById)");
    }

    @Test
    public void shouldGetNullWhenTaskByIdWithIncorrectId() {
        Task task = new Task("Тест Имя", "Тест Описание", TaskStatus.NEW, duration, data1);
        final Integer taskId = taskManager.addNewTask(task);

        final int falseTaskId = 5;
        assertNull(taskManager.getTaskById(falseTaskId),
                "Задача найдена (shouldGetNullWhenTaskByIdWithIncorrectId)");
    }

    @Test
    public void shouldUpdateTask() {
        Task task = new Task("Тест Имя1", "Тест Описание1", TaskStatus.NEW, duration, data1);
        final Integer task1Id = taskManager.addNewTask(task);

        // Обновили статус:
        task.setStatus(TaskStatus.IN_PROGRESS);
        // Обновили Задачу:
        taskManager.updateTask(task);
        // Проверяем изменение статуса:
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTaskById(task1Id).getStatus(),
                "Статус не изменился (shouldUpdateTask)");
    }

    @Test
    public void shouldDeleteTaskById() {
        Task task = new Task("Тест Имя1", "Тест Описание1", TaskStatus.NEW, duration, data1);
        final Integer task1Id = taskManager.addNewTask(task);

        taskManager.deleteTaskById(task1Id);
        assertNull(taskManager.getTaskById(task1Id), "Задача не удалена (shouldDeleteTaskById)");
    }

    /***--- Тесты для Subtask ---***/
    @Test
    public void shouldAddNewSubtask() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtaskId = taskManager.addNewSubtask(subtask);

        final List<Epic> epicList = taskManager.getAllEpicsList();
        final List<Subtask> subtaskList = taskManager.getAllSubtasksList();
        assertNotNull(epicList, "Список пуст, задача не добавлена (addNewSubtask)");
        assertNotNull(subtaskList, "Список пуст, задача не добавлена (addNewSubtask)");
    }

    @Test
    public void shouldNullWhenAddNewSubtaskWithoutEpic() {
        final int epicId = 5;
        Subtask subtask = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);

        assertNull(taskManager.addNewSubtask(subtask),
                "Ошибка: задача добавлена (shouldNullWhenAddNewSubtaskWithoutEpic)");
    }

    @Test
    public void shouldGetAllSubtasksList() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.NEW, duration, data2, epicId);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        final List<Subtask> subtaskList = taskManager.getAllSubtasksList();
        assertEquals(2, subtaskList.size(),
                "Неверное количество задач в списке (shouldGetAllSubtasksList)");
    }

    @Test
    public void shouldGetNullWhenSubtaskListIsEmpty() {
        assertEquals(0, taskManager.getAllSubtasksList().size(),
                "Список не пуст (shouldGetNullWhenSubtaskListIsEmpty)");
    }

    @Test
    public void shouldDeleteAllSubtasks() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3", TaskStatus.NEW,
                duration, data2, epicId);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getAllSubtasksList().size(),
                "Список не пуст (shouldDeleteAllSubtasks)");
    }
    @Test
    public void shouldGetSubtaskById() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);

        Subtask subtaskById = taskManager.getSubtaskById(subtask1Id);
        assertEquals(subtask1, subtaskById, "Задачи не совпали (shouldGetSubtaskById)");
    }

    @Test
    public void shouldGetNullWhenSubtaskByIdWithIncorrectId() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);

        final int falseSubtaskId = 25;
        assertNull(taskManager.getSubtaskById(falseSubtaskId),
                "Ошибка: задача найдена (shouldGetNullWhenSubtaskByIdWithIncorrectId)");
    }

    @Test
    public void shouldUpdateSubtask() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getSubtaskById(subtask1Id).getStatus(),
                "Статус не изменился (shouldUpdateSubtask)");
    }

    @Test
    public void shouldDeleteSubtaskById() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);

        taskManager.deleteSubtaskById(subtask1Id);
        assertNull(taskManager.getSubtaskById(subtask1Id),
                "Задача не удалена (shouldDeleteSubtaskById)");
    }

    @Test
    public void shouldGetEpicSubtasks() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.NEW, duration, data2, epicId);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        List<Subtask> epicSubtasks = taskManager.getEpicSubtasks(epicId);
        assertEquals(2, epicSubtasks.size(),
                "Неверное количество задач в списке (shouldGetEpicSubtasks)");
    }

    @Test
    public void shouldGetNullEpicSubtasksWhenEpicWithoutSubtask() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);

        assertEquals(0, taskManager.getEpicSubtasks(epicId).size(),
                "Ошибка: список создан без задач (shouldGetNullEpicSubtasksWhenEpicWithoutSubtask)");
    }

    /***--- Тесты для Epic ---***/
    @Test
    public void shouldAddNewEpic() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);

        final List<Epic> epicList = taskManager.getAllEpicsList();
        assertNotNull(epicList, "Список пуст, задача не добавлена (shouldAddNewEpic)");
    }

    @Test
    public void shouldGetAllEpicsList() {
        Epic epic1 = new Epic("Тест Имя1", "Тест Описание1");
        final int epic1Id = taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("Тест Имя2", "Тест Описание2");
        final int epic2Id = taskManager.addNewEpic(epic2);

        final List<Epic> epicList = taskManager.getAllEpicsList();
        assertEquals(2, epicList.size(),
                "Неверное количество задач в списке (shouldGetAllEpicsList)");
    }

    @Test
    public void shouldGetNullWhenEpicListIsEmpty() {
        final List<Epic> epicList = taskManager.getAllEpicsList();
        assertEquals(0, epicList.size(),
                "Список не пуст (shouldGetNullWhenEpicListIsEmpty)");
    }

    @Test
    public void deleteAllEpics() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Epic epic2 = new Epic("Тест Имя4", "Тест Описание4");
        final int epic2Id = taskManager.addNewEpic(epic2);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.NEW, duration, data2, epicId);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getAllSubtasksList().size(),"Список не пуст (deleteAllEpics)");
        assertEquals(0, taskManager.getAllEpicsList().size(),"Список не пуст (deleteAllEpics)");
    }

    @Test
    public void shouldGetEpicById() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);

        Epic epicById = taskManager.getEpicById(epicId);
        assertEquals(epic, epicById, "Задачи не совпали (shouldGetEpicById)");
    }

    @Test
    public void shouldGetNullWhenEpicByIdWithIncorrectId() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);

        final int falseEpicId = 25;
        assertNull(taskManager.getSubtaskById(falseEpicId),
                "Ошибка: задача найдена (shouldGetNullWhenEpicByIdWithIncorrectId)");
    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);

        epic.setName("Тест Имя2");
        epic.setDescription("Тест Описание2");
        taskManager.updateEpic(epic);
        assertEquals("Тест Имя2", taskManager.getEpicById(epicId).getName(),
                "Название не обновилось (shouldUpdateEpic)");
        assertEquals("Тест Описание2", taskManager.getEpicById(epicId).getDescription(),
                "Описание не обновилось (shouldUpdateEpic)");
    }

    @Test
    public void shouldDeleteEpicById() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.NEW, duration, data2, epicId);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        taskManager.deleteEpicById(epicId);
        assertEquals(0, taskManager.getAllSubtasksList().size(),
                "Подзадачи не удалены (shouldDeleteEpicById)");
        assertNull(taskManager.getEpicById(epicId), "Задача не удалена (shouldDeleteEpicById)");
    }

    @Test
    public void shouldUpdateEpicStatusToNewWhenSubtasksIsEmpty() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);

        assertEquals(TaskStatus.NEW, epic.getStatus(),
                "Статус рассчитан ошибочно (shouldUpdateEpicStatusToNewWhenSubtasksIsEmpty)");
    }

    /***--- Тесты для обновления Epic ---***/
    @Test
    public void shouldUpdateEpicStatusToNewWhenAllSubtasksStatusIsNew() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.NEW, duration, data2, epicId);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        assertEquals(TaskStatus.NEW, epic.getStatus(),
                "Статус рассчитан ошибочно (shouldUpdateEpicStatusToNewWhenAllSubtasksStatusIsNew)");
    }

    @Test
    public void shouldUpdateEpicStatusToDoneWhenAllSubtasksStatusIsDone() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.DONE, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.DONE, duration, data2, epicId);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        assertEquals(TaskStatus.DONE, epic.getStatus(),
                "Статус рассчитан ошибочно (shouldUpdateEpicStatusToDoneWhenAllSubtasksStatusIsDone)");
    }

    @Test
    public void shouldUpdateEpicStatusToInProgressWhenSubtasksStatusIsNewAndDone() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.NEW, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.DONE, duration, data2, epicId);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(),
                "Статус рассчитан ошибочно "
                        + "(shouldUpdateEpicStatusToInProgressWhenSubtasksStatusIsNewAndDone)");
    }

    @Test
    public void shouldUpdateEpicStatusToInProgressWhenAllSubtasksStatusIsInProgress() {
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.IN_PROGRESS, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.IN_PROGRESS, duration, data2, epicId);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(),
                "Статус рассчитан ошибочно "
                        + "(shouldUpdateEpicStatusToInProgressWhenAllSubtasksStatusIsInProgress)");
    }

    @Test
    public void shouldUpdateEpicDurationAndTime() {
        // Проверим обновление времени у Эпика: должен быть сумарный duration от Сабтасков,
        // startTime от первого Сабтаска, а endTime от второго Сабтаска:
        Epic epic = new Epic("Тест Имя1", "Тест Описание1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Тест Имя2", "Тест Описание2",
                TaskStatus.IN_PROGRESS, duration, data1, epicId);
        final Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя3", "Тест Описание3",
                TaskStatus.IN_PROGRESS, duration, data2, epicId);
        final Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        assertEquals((subtask1.getDuration() + subtask2.getDuration()), epic.getDuration(),
                "duration рассчитан неверно (shouldUpdateEpicDurationAndTime)");
        assertEquals(subtask1.getStartTime(), epic.getStartTime(),
                "startTime рассчитан неверно (shouldUpdateEpicDurationAndTime)");
        assertEquals(subtask2.getEndTime(), epic.getEndTime(),
                "endTime рассчитан неверно (shouldUpdateEpicDurationAndTime)");
    }
}
