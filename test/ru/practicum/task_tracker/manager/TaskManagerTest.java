package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

abstract public class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected LocalDateTime data1;
    protected LocalDateTime data2;
    protected LocalDateTime data3;
    protected int duration;

    public void init() {
        // Общая тест дата для 2 реализаций: 1 Таск, 1 Эпик, 2 Сабтаска, все задачи добавлены в менеджер:
        data1 = LocalDateTime.of(2000, 1, 1, 3, 0);
        data2 = LocalDateTime.of(2000, 1, 1, 5, 0);
        data3 = LocalDateTime.of(2000, 1, 1, 1, 0);
        duration = 60;
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
    public void shouldAddNewTask() {
        final List<Task> taskList = taskManager.getAllTasksList();

        assertNotNull(taskList, "Список пуст, задача не добавлена (shouldAddNewTask)");
    }

    @Test
    public void shouldGetAllTasksList() {
        final List<Task> taskList = taskManager.getAllTasksList();

        assertEquals(1, taskList.size(),
                "Неверное количество задач в списке (shouldGetAllTasksList)");
    }

    @Test
    public void shouldGetNullWhenTasksListIsEmpty() {
        taskManager.deleteAllTasks();

        assertEquals(0, taskManager.getAllTasksList().size(),
                "Список не пуст (shouldGetNullWhenTasksListIsEmpty)");
    }

    @Test
    public void shouldDeleteAllTasks() {
        taskManager.deleteAllTasks();

        assertEquals(0, taskManager.getAllTasksList().size(),
                "Список не пуст (shouldDeleteAllTasks)");
    }

    @Test
    public void shouldGetTaskById() {
        Task taskById = taskManager.getTaskById(task.getId());

        assertEquals(task, taskById, "Задачи не совпали (shouldGetTaskById)");
    }

    @Test
    public void shouldGetNullWhenTaskByIdWithIncorrectId() {
        final int falseTaskId = 45;

        assertNull(taskManager.getTaskById(falseTaskId),
                "Задача найдена (shouldGetNullWhenTaskByIdWithIncorrectId)");
    }

    @Test
    public void shouldUpdateTask() {
        // Обновили статус:
        task.setStatus(TaskStatus.IN_PROGRESS);

        // Обновили Задачу:
        taskManager.updateTask(task);

        // Проверяем изменение статуса:
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTaskById(task.getId()).getStatus(),
                "Статус не изменился (shouldUpdateTask)");
    }

    @Test
    public void shouldDeleteTaskById() {
        taskManager.deleteTaskById(task.getId());

        assertNull(taskManager.getTaskById(task.getId()), "Задача не удалена (shouldDeleteTaskById)");
    }

    /***--- Тесты для Subtask ---***/
    @Test
    public void shouldAddNewSubtask() {
        final List<Epic> epicList = taskManager.getAllEpicsList();
        final List<Subtask> subtaskList = taskManager.getAllSubtasksList();

        assertNotNull(epicList, "Список пуст, задача не добавлена (addNewSubtask)");
        assertNotNull(subtaskList, "Список пуст, задача не добавлена (addNewSubtask)");
    }

    @Test
    public void shouldNullWhenAddNewSubtaskWithoutEpic() {
        final int fakeEpicId = 25;

        final Subtask subtaskWithFakeEpicId = new Subtask("Тест Имя5", "Тест Описание5",
                TaskStatus.NEW, duration, LocalDateTime.now(), fakeEpicId);

        assertNull(taskManager.addNewSubtask(subtaskWithFakeEpicId),
                "Ошибка: задача добавлена (shouldNullWhenAddNewSubtaskWithoutEpic)");
    }

    @Test
    public void shouldGetAllSubtasksList() {
        final List<Subtask> subtaskList = taskManager.getAllSubtasksList();

        assertEquals(2, subtaskList.size(),
                "Неверное количество задач в списке (shouldGetAllSubtasksList)");
    }

    @Test
    public void shouldGetNullWhenSubtaskListIsEmpty() {
        taskManager.deleteAllSubtasks();

        assertEquals(0, taskManager.getAllSubtasksList().size(),
                "Список не пуст (shouldGetNullWhenSubtaskListIsEmpty)");
    }

    @Test
    public void shouldDeleteAllSubtasks() {
        taskManager.deleteAllSubtasks();

        assertEquals(0, taskManager.getAllSubtasksList().size(),
                "Список не пуст (shouldDeleteAllSubtasks)");
    }

    @Test
    public void shouldGetSubtaskById() {
        final Subtask subtaskById = taskManager.getSubtaskById(subtask1.getId());

        assertEquals(subtask1, subtaskById, "Задачи не совпали (shouldGetSubtaskById)");
    }

    @Test
    public void shouldGetNullWhenSubtaskByIdWithIncorrectId() {
        final int falseSubtaskId = 55;

        assertNull(taskManager.getSubtaskById(falseSubtaskId),
                "Ошибка: задача найдена (shouldGetNullWhenSubtaskByIdWithIncorrectId)");
    }

    @Test
    public void shouldUpdateSubtask() {
        subtask1.setStatus(TaskStatus.IN_PROGRESS);

        taskManager.updateSubtask(subtask1);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getSubtaskById(subtask1.getId()).getStatus(),
                "Статус не изменился (shouldUpdateSubtask)");
    }

    @Test
    public void shouldDeleteSubtaskById() {
        taskManager.deleteSubtaskById(subtask1.getId());

        assertNull(taskManager.getSubtaskById(subtask1.getId()),
                "Задача не удалена (shouldDeleteSubtaskById)");
    }

    @Test
    public void shouldGetEpicSubtasks() {
        List<Subtask> epicSubtasks = taskManager.getEpicSubtasks(epic.getId());

        assertEquals(2, epicSubtasks.size(),
                "Неверное количество задач в списке (shouldGetEpicSubtasks)");
    }

    @Test
    public void shouldGetNullEpicSubtasksWhenEpicWithoutSubtask() {
        taskManager.deleteAllSubtasks();

        assertEquals(0, taskManager.getEpicSubtasks(epic.getId()).size(),
                "Ошибка: список создан без задач (shouldGetNullEpicSubtasksWhenEpicWithoutSubtask)");
    }

    /***--- Тесты для Epic ---***/
    @Test
    public void shouldAddNewEpic() {
        final List<Epic> epicList = taskManager.getAllEpicsList();

        assertNotNull(epicList, "Список пуст, задача не добавлена (shouldAddNewEpic)");
    }

    @Test
    public void shouldGetAllEpicsList() {
        final List<Epic> epicList = taskManager.getAllEpicsList();

        assertEquals(1, epicList.size(),
                "Неверное количество задач в списке (shouldGetAllEpicsList)");
    }

    @Test
    public void shouldGetNullWhenEpicListIsEmpty() {
        taskManager.deleteAllEpics();

        final List<Epic> epicList = taskManager.getAllEpicsList();

        assertEquals(0, epicList.size(),
                "Список не пуст (shouldGetNullWhenEpicListIsEmpty)");
    }

    @Test
    public void deleteAllEpics() {
        taskManager.deleteAllEpics();

        assertEquals(0, taskManager.getAllSubtasksList().size(),"Список не пуст (deleteAllEpics)");
        assertEquals(0, taskManager.getAllEpicsList().size(),"Список не пуст (deleteAllEpics)");
    }

    @Test
    public void shouldGetEpicById() {
        final Epic epicById = taskManager.getEpicById(epic.getId());

        assertEquals(epic, epicById, "Задачи не совпали (shouldGetEpicById)");
    }

    @Test
    public void shouldGetNullWhenEpicByIdWithIncorrectId() {
        final int falseEpicId = 65;

        assertNull(taskManager.getSubtaskById(falseEpicId),
                "Ошибка: задача найдена (shouldGetNullWhenEpicByIdWithIncorrectId)");
    }

    @Test
    public void shouldUpdateEpic() {
        epic.setName("Тест Имя12");
        epic.setDescription("Тест Описание12");

        taskManager.updateEpic(epic);

        assertEquals("Тест Имя12", taskManager.getEpicById(epic.getId()).getName(),
                "Название не обновилось (shouldUpdateEpic)");
        assertEquals("Тест Описание12", taskManager.getEpicById(epic.getId()).getDescription(),
                "Описание не обновилось (shouldUpdateEpic)");
    }

    @Test
    public void shouldDeleteEpicByIdAndHisSubtask() {
        taskManager.deleteEpicById(epic.getId());

        assertEquals(0, taskManager.getAllSubtasksList().size(),
                "Подзадачи не удалены (shouldDeleteEpicById)");
        assertNull(taskManager.getEpicById(epic.getId()), "Задача не удалена (shouldDeleteEpicById)");
    }

    @Test
    public void shouldUpdateEpicStatusToNewWhenSubtasksIsEmpty() {
        taskManager.deleteAllSubtasks();

        assertEquals(TaskStatus.NEW, epic.getStatus(),
                "Статус рассчитан ошибочно (shouldUpdateEpicStatusToNewWhenSubtasksIsEmpty)");
    }

    /***--- Тесты для обновления Epic ---***/
    @Test
    public void shouldUpdateEpicStatusToNewWhenAllSubtasksStatusIsNew() {
        assertEquals(TaskStatus.NEW, epic.getStatus(),
                "Статус рассчитан ошибочно (shouldUpdateEpicStatusToNewWhenAllSubtasksStatusIsNew)");
    }

    @Test
    public void shouldUpdateEpicStatusToDoneWhenAllSubtasksStatusIsDone() {
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        assertEquals(TaskStatus.DONE, epic.getStatus(),
                "Статус рассчитан ошибочно (shouldUpdateEpicStatusToDoneWhenAllSubtasksStatusIsDone)");
    }

    @Test
    public void shouldUpdateEpicStatusToInProgressWhenSubtasksStatusIsNewAndDone() {
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(),
                "Статус рассчитан ошибочно "
                        + "(shouldUpdateEpicStatusToInProgressWhenSubtasksStatusIsNewAndDone)");
    }

    @Test
    public void shouldUpdateEpicStatusToInProgressWhenAllSubtasksStatusIsInProgress() {
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(),
                "Статус рассчитан ошибочно "
                        + "(shouldUpdateEpicStatusToInProgressWhenAllSubtasksStatusIsInProgress)");
    }

    @Test
    public void shouldUpdateEpicDurationAndTime() {
        // Проверим обновление времени у Эпика: должен быть сумарный duration от Сабтасков,
        // startTime от второго Сабтаска, а endTime от первого Сабтаска:

        assertEquals((subtask1.getDuration() + subtask2.getDuration()), epic.getDuration(),
                "duration рассчитан неверно (shouldUpdateEpicDurationAndTime)");
        assertEquals(subtask2.getStartTime(), epic.getStartTime(),
                "startTime рассчитан неверно (shouldUpdateEpicDurationAndTime)");
        assertEquals(subtask1.getEndTime(), epic.getEndTime(),
                "endTime рассчитан неверно (shouldUpdateEpicDurationAndTime)");
    }

    /*---Тест работы сортировки---*/
    @Test
    public void shouldGetSortedList() {
        // Проверим отсортирован ли список:
        Set<Task> sortedTaskList = taskManager.getPrioritizedTasks();
        List<Task> df = List.copyOf(sortedTaskList);

        boolean isSortedList = (df.get(0).getStartTime().isBefore(df.get(1).getStartTime())
                && df.get(1).getStartTime().isBefore(df.get(2).getStartTime()));

        assertTrue(isSortedList, "Список не отсортирован по времени старта (shouldGetSortedList)");
    }

    /*---Тест проверки поиска пересечений---*/
    @Test
    public void shouldGetTwoEqualsTasksListAfterAddTasksWithIncorrectDateTime() {
        // Пробуем добавить задачи которые пересекаются по времени с subtask2
        // с разных сторон (начало, середина, конец):
        List<Task> beforeAddTask = taskManager.getAllTasksList();
        final LocalDateTime data4 = LocalDateTime.of(2000, 1, 1, 0, 45);
        final LocalDateTime data5 = LocalDateTime.of(2000, 1, 1, 1, 10);
        final LocalDateTime data6 = LocalDateTime.of(2000, 1, 1, 1, 45);
        final int duration2 = 45;
        final Task task2 = new Task("Тест Имя10", "Тест Описание10",
                TaskStatus.NEW, duration2, data4);
        final Task task3 = new Task("Тест Имя11", "Тест Описание11",
                TaskStatus.IN_PROGRESS, duration2, data5);
        final Task task4 = new Task("Тест Имя12", "Тест Описание12",
                TaskStatus.NEW, duration2, data6);

        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        taskManager.addNewTask(task4);
        List<Task> afterAddTask = taskManager.getAllTasksList();

        assertEquals(beforeAddTask.size(), afterAddTask.size(),
                "Ошибка: пересекающиеся задачи"
                        + " добавлены (shouldGetTwoEqualsTasksListAfterAddTasksWithIncorrectDateTime)");
    }
}