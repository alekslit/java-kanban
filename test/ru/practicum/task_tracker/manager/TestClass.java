package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClass {

    TaskManager taskManager1 = Managers.getDefault();
    HistoryManager historyManager1 = taskManager1.getHistoryManager();
    LocalDateTime data1 = LocalDateTime.of(2000, 1, 1, 1, 0);
    LocalDateTime data2 = LocalDateTime.of(2000, 1, 1, 3, 0);
    LocalDateTime data3 = LocalDateTime.of(2000, 1, 1, 5, 0);
    LocalDateTime data4 = LocalDateTime.of(2000, 1, 1, 7, 0);
    LocalDateTime data5 = LocalDateTime.of(2000, 1, 1, 9, 0);
    int duration = 60;

    @BeforeEach
    public void beforeEach() {
        taskManager1.deleteAllTasks();
        taskManager1.deleteAllSubtasks();
        taskManager1.deleteAllEpics();
        // Создаём и добавдяем 2 Таска:
        Task task1 = new Task("Тест Имя1", "Тест Описание1",
                TaskStatus.NEW, duration, data5);
        Integer task1Id = taskManager1.addNewTask(task1);
        Task task2 = new Task("Тест Имя2", "Тест Описание2",
                TaskStatus.IN_PROGRESS, duration, data4);
        Integer task2Id = taskManager1.addNewTask(task2);

        // Создаём и добавляем 1 Эпик - с тремя Сабтасками:
        Epic epic1 = new Epic("Тест Имя3", "Тест Описание3");
        int epic1Id = taskManager1.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Тест Имя4", "Тест Описание4",
                TaskStatus.DONE, duration, data3, epic1Id);
        Integer subtask1Id = taskManager1.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Тест Имя5", "Тест Описание5",
                TaskStatus.IN_PROGRESS, duration, data2, epic1Id);
        Integer subtask2Id = taskManager1.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("Тест Имя6", "Тест Описание6",
                TaskStatus.IN_PROGRESS, duration, data1, epic1Id);
        Integer subtask3Id = taskManager1.addNewSubtask(subtask3);
    }

    // Тест работы сортировки:
    @Test
    public void test1() {
        Set<Task> sortedTaskList = taskManager1.getPrioritizedTasks();
        for (Task task: sortedTaskList) {
            System.out.println(task.getId() + " " + task.getClass().getSimpleName()+ " " + task.getStartTime());
        }
    }

    // Тест проверки поиска пересечений:
    @Test
    public void shouldGetTwoEqualsTasksListAfterAddTasksWithIncorrectDateTime() {
        // Пробуем добавить задачи которые пересекаются с первой задачей с разных сторон (начало, середина, конец):
        List<Task> beforeAddTask = taskManager1.getAllTasksList();
        LocalDateTime data6 = LocalDateTime.of(2000, 1, 1, 0, 45);
        LocalDateTime data7 = LocalDateTime.of(2000, 1, 1, 1, 10);
        LocalDateTime data8 = LocalDateTime.of(2000, 1, 1, 1, 45);
        int duration2 = 45;

        Task task3 = new Task("Тест Имя10", "Тест Описание10",
                TaskStatus.NEW, duration2, data6);
        Integer task3Id = taskManager1.addNewTask(task3);
        Task task4 = new Task("Тест Имя11", "Тест Описание11",
                TaskStatus.IN_PROGRESS, duration2, data7);
        Integer task4Id = taskManager1.addNewTask(task4);
        Task task5 = new Task("Тест Имя12", "Тест Описание12",
                TaskStatus.NEW, duration2, data8);
        Integer task5Id = taskManager1.addNewTask(task5);

        List<Task> afterAddTask = taskManager1.getAllTasksList();
        assertEquals(beforeAddTask.size(), afterAddTask.size(),
                "Ошибка: пересекающиеся задачи"
                        + " добавлены (shouldGetTwoEqualsTasksListAfterAddTasksWithIncorrectDateTime)");
    }
}
