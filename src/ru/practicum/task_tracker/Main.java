package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskTracker taskTracker = new TaskTracker();

        // Создаём и добавдяем 2 Таска:
        Task task1 = new Task("Выполнить ТЗ-1", "Разбиваем ТЗ-1 на составляющие...", "NEW");
        int task1Id = taskTracker.addNewTask(task1);

        Task task2 = new Task("Изучить класс Object",
                "Изучаем теорию и выполняем задания в тренажёре...", "IN_PROGRESS");
        int task2Id = taskTracker.addNewTask(task2);

        // Создаём и добавляем 2 Эпика - с двумями Сабтасками, и с одним:
        Epic epic1 = new Epic("Закончить первый модуль",
                "Изучаем теорию, выполняем задания в тренажёре, делаем ТЗ...");
        int epic1Id = taskTracker.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Проходим 1-2 спринты",
                "Изучаем Методы, Классы, Объекты, Хеш-Таблицы....", "DONE", epic1Id);
        Integer subtask1Id = taskTracker.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Проходим 3-4 спринты",
                "Знакомство с ООП, класс Object...", "IN_PROGRESS", epic1Id);
        Integer subtask2Id = taskTracker.addNewSubtask(subtask2);

        Epic epic2 = new Epic("Не закончить первый модуль...", "Ничего не успеть.... :( ");
        int epic2Id = taskTracker.addNewEpic(epic2);
        Subtask subtask3 = new Subtask("Просто взять и не закончить... :О ? ",
                "Ну как же так... :( ", "NEW", epic2Id);
        Integer subtask3Id = taskTracker.addNewSubtask(subtask3);

        // Получение объекта по ID:
        System.out.println("___________Вызываем объекты по ID________________");
        System.out.println(taskTracker.getTaskById(task2Id));
        System.out.println(taskTracker.getSubtaskById(subtask2Id));
        System.out.println(taskTracker.getEpicById(epic2Id));

        // Распечатаем список Эпиков, Тасков, и Сабтасков:
        System.out.println("___________Без изменений________________");
        System.out.println(taskTracker.getAllTasks());
        System.out.println(taskTracker.getAllSubtasks());
        System.out.println(taskTracker.getAllEpics());

        // Удаляем все Таски, Сабтаски, или Эпики и проверяем изменения:
        /*taskTracker.deleteAllTasks();
        System.out.println("___________Удалили Таски________________");
        System.out.println(taskTracker.getAllTasks());*/

        /*taskTracker.deleteAllSubtasks();
        System.out.println("___________Удалили Сабтаски________________");
        System.out.println(taskTracker.getAllSubtasks());
        System.out.println(taskTracker.getAllEpics());*/

        /*taskTracker.deleteAllEpic();
        System.out.println("___________Удалили Эпики________________");
        System.out.println(taskTracker.getAllSubtasks());
        System.out.println(taskTracker.getAllEpics());*/

        // Распечаатаем список Сабтасков определённого эпика:
        System.out.println(taskTracker.getEpicSubtasks(epic1Id));

        // Поменяем статус Тасков и Сабтасков через методы обновления, и проверим изменение статусов всех объектов:
        task1.setStatus("IN_PROGRESS");
        task2.setStatus("DONE");
        subtask1.setStatus("NEW");
        subtask2.setStatus("NEW");
        subtask3.setStatus("IN_PROGRESS");

        taskTracker.updateTask(task1);
        taskTracker.updateTask(task2);
        taskTracker.updateSubtask(subtask1);
        taskTracker.updateSubtask(subtask2);
        taskTracker.updateSubtask(subtask3);

        System.out.println("___________Обновили статус________________");
        System.out.println(taskTracker.getAllTasks());
        System.out.println(taskTracker.getAllSubtasks());
        System.out.println(taskTracker.getAllEpics());

        // Удаляем Таск, Эпик и Сабтаск, и проверяем изменения:
        taskTracker.deleteTaskById(task1Id);
        taskTracker.deleteSubtaskById(subtask2Id);
        taskTracker.deleteEpicById(epic2Id);

        System.out.println("___________Удалили Таск, Эпик, и Сабтаск________________");
        System.out.println(taskTracker.getAllTasks());
        System.out.println(taskTracker.getAllSubtasks());
        System.out.println(taskTracker.getAllEpics());
    }
}