package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.*;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = taskManager.getHistoryManager();

        // Создаём и добавдяем 2 Таска:
        Task task1 = new Task("Выполнить ТЗ-1", "Разбиваем ТЗ-1 на составляющие...", TaskStatus.NEW);
        int task1Id = taskManager.addNewTask(task1);

        Task task2 = new Task("Изучить класс Object",
                "Изучаем теорию и выполняем задания в тренажёре...", TaskStatus.IN_PROGRESS);
        int task2Id = taskManager.addNewTask(task2);

        // Создаём и добавляем 2 Эпика - с двумями Сабтасками, и с одним:
        Epic epic1 = new Epic("Закончить первый модуль",
                "Изучаем теорию, выполняем задания в тренажёре, делаем ТЗ...");
        int epic1Id = taskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Проходим 1-2 спринты",
                "Изучаем Методы, Классы, Объекты, Хеш-Таблицы....", TaskStatus.DONE, epic1Id);
        Integer subtask1Id = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Проходим 3-4 спринты",
                "Знакомство с ООП, класс Object...", TaskStatus.IN_PROGRESS, epic1Id);
        Integer subtask2Id = taskManager.addNewSubtask(subtask2);

        Epic epic2 = new Epic("Не закончить первый модуль...", "Ничего не успеть.... :( ");
        int epic2Id = taskManager.addNewEpic(epic2);
        Subtask subtask3 = new Subtask("Просто взять и не закончить... :О ? ",
                "Ну как же так... :( ", TaskStatus.NEW, epic2Id);
        Integer subtask3Id = taskManager.addNewSubtask(subtask3);

        // Получение объекта по ID + проверяем вывод истории просмотров:
        System.out.println("___________Вызываем объекты по ID________________");
        for (int i = 1; i <= 4; i++) {
            System.out.println(taskManager.getTaskById(task2Id));
            System.out.println(taskManager.getSubtaskById(subtask2Id));
            System.out.println(taskManager.getEpicById(epic2Id));
        }
        System.out.println("___________История просмотров________________");
        System.out.println(historyManager.getTaskHistory());

        // Распечатаем список Эпиков, Тасков, и Сабтасков:
        System.out.println("___________Без изменений________________");
        System.out.println(taskManager.getAllTasksList());
        System.out.println(taskManager.getAllSubtasksList());
        System.out.println(taskManager.getAllEpicsList());

        // Удаляем все Таски, Сабтаски, или Эпики и проверяем изменения:
        /*taskManager.deleteAllTasks();
        System.out.println("___________Удалили Таски________________");
        System.out.println(taskManager.getAllTasksList());*/

        /*taskManager.deleteAllSubtasks();
        System.out.println("___________Удалили Сабтаски________________");
        System.out.println(taskManager.getAllSubtasksList());
        System.out.println(taskManager.getAllEpicsList());*/

        /*taskManager.deleteAllEpics();
        System.out.println("___________Удалили Эпики________________");
        System.out.println(taskManager.getAllSubtasksList());
        System.out.println(taskManager.getAllEpicsList());*/

        // Распечатаем список Сабтасков определённого эпика:
        System.out.println(taskManager.getEpicSubtasks(epic1Id));

        // Поменяем статус Тасков и Сабтасков через методы обновления, и проверим изменение статусов всех объектов:
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task2.setStatus(TaskStatus.DONE);
        subtask1.setStatus(TaskStatus.NEW);
        subtask2.setStatus(TaskStatus.NEW);
        subtask3.setStatus(TaskStatus.IN_PROGRESS);

        taskManager.updateTask(task1);
        taskManager.updateTask(task2);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        System.out.println("___________Обновили статус________________");
        System.out.println(taskManager.getAllTasksList());
        System.out.println(taskManager.getAllSubtasksList());
        System.out.println(taskManager.getAllEpicsList());

        // Удаляем Таск, Эпик и Сабтаск, и проверяем изменения:
        taskManager.deleteTaskById(task1Id);
        taskManager.deleteSubtaskById(subtask2Id);
        taskManager.deleteEpicById(epic2Id);

        System.out.println("___________Удалили Таск, Эпик, и Сабтаск________________");
        System.out.println(taskManager.getAllTasksList());
        System.out.println(taskManager.getAllSubtasksList());
        System.out.println(taskManager.getAllEpicsList());

        // Ещё раз проверяем историю просмотров после вызова методов:
        System.out.println("___________История просмотров________________");
        System.out.println(historyManager.getTaskHistory());
    }
}