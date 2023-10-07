import java.nio.file.Paths;

public class Draft {
        /*// Изначальный объект FBTM:
        TaskManager taskManager1 = Managers.getDefault();
        HistoryManager historyManager1 = taskManager1.getHistoryManager();
        LocalDateTime data1 = LocalDateTime.of(2000, 1, 1, 1, 0);
        LocalDateTime data2 = LocalDateTime.of(2000, 1, 1, 3, 0);
        LocalDateTime data3 = LocalDateTime.of(2000, 1, 1, 5, 0);
        LocalDateTime data4 = LocalDateTime.of(2000, 1, 1, 7, 0);
        LocalDateTime data5 = LocalDateTime.of(2000, 1, 1, 9, 0);
        int duration = 60;

        // Создаём и добавдяем 2 Таска:
        Task task1 = new Task("Выполнить ТЗ-1", "Разбиваем ТЗ-1 на составляющие...",
                TaskStatus.NEW, duration, data1);
        Integer task1Id = taskManager1.addNewTask(task1);
        Task task2 = new Task("Изучить класс Object",
                "Изучаем теорию и выполняем задания в тренажёре...",
                TaskStatus.IN_PROGRESS, duration, data2);
        Integer task2Id = taskManager1.addNewTask(task2);

        // Создаём и добавляем 2 Эпика - с тремя Сабтасками, и без:
        Epic epic1 = new Epic("Закончить первый модуль",
                "Изучаем теорию, выполняем задания в тренажёре, делаем ТЗ...");
        int epic1Id = taskManager1.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Проходим 1-2 спринты",
                "Изучаем Методы, Классы, Объекты, Хеш-Таблицы....",
                TaskStatus.DONE, duration, data3, epic1Id);
        Integer subtask1Id = taskManager1.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Проходим 3-4 спринты",
                "Знакомство с ООП, класс Object...", TaskStatus.IN_PROGRESS, duration, data4, epic1Id);
        Integer subtask2Id = taskManager1.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("Каникулы",
                "Ничего не делаем...", TaskStatus.IN_PROGRESS, duration, data5, epic1Id);
        Integer subtask3Id = taskManager1.addNewSubtask(subtask3);
        Epic epic2 = new Epic("Не закончить первый модуль...", "Ничего не успеть.... :( ");
        int epic2Id = taskManager1.addNewEpic(epic2);

        // Получение объекта по ID + проверяем вывод истории просмотров, отсутствие повторов в ней,
        // и корректность смены ссылок нод:
        System.out.println(taskManager1.getTaskById(task1Id));
        System.out.println(taskManager1.getTaskById(task2Id));
        System.out.println(taskManager1.getEpicById(epic1Id));
        System.out.println(taskManager1.getEpicById(epic2Id));
        System.out.println(taskManager1.getSubtaskById(subtask1Id));
        System.out.println(taskManager1.getSubtaskById(subtask2Id));
        System.out.println(taskManager1.getSubtaskById(subtask3Id));

        System.out.println("___________История просмотров 1________________");
        System.out.println(historyManager1.getTaskHistory());

        System.out.println(taskManager1.getTaskById(task1Id));
        System.out.println(taskManager1.getSubtaskById(subtask3Id));
        System.out.println(taskManager1.getSubtaskById(subtask1Id));
        System.out.println(taskManager1.getSubtaskById(subtask2Id));
        System.out.println(taskManager1.getTaskById(task2Id));
        System.out.println(taskManager1.getEpicById(epic2Id));
        System.out.println(taskManager1.getEpicById(epic1Id));

        System.out.println("___________История просмотров 2________________");
        System.out.println(historyManager1.getTaskHistory());*/

        /*// Распечатаем список Эпиков, Тасков, и Сабтасков:
        System.out.println("___________Без изменений________________");
        System.out.println(taskManager1.getAllTasksList());
        System.out.println(taskManager1.getAllSubtasksList());
        System.out.println(taskManager1.getAllEpicsList());*/

    // Удаляем все Таски, Сабтаски, или Эпики и проверяем изменения:
        /*taskManager1.deleteAllTasks();
        System.out.println("___________Удалили Таски________________");
        System.out.println(taskManager1.getAllTasksList());*/

        /*taskManager1.deleteAllSubtasks();
        System.out.println("___________Удалили Сабтаски________________");
        System.out.println(taskManager1.getAllSubtasksList());
        System.out.println(taskManager1.getAllEpicsList());*/

        /*taskManager1.deleteAllEpics();
        System.out.println("___________Удалили Эпики________________");
        System.out.println(taskManager1.getAllSubtasksList());
        System.out.println(taskManager1.getAllEpicsList());*/

        /*// Распечатаем список Сабтасков определённого эпика:
        System.out.println(taskManager1.getEpicSubtasks(epic1Id));*/

        /*// Поменяем статус Тасков и Сабтасков через методы обновления, и проверим изменение статусов всех объектов:
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task2.setStatus(TaskStatus.DONE);
        subtask1.setStatus(TaskStatus.NEW);
        subtask2.setStatus(TaskStatus.NEW);
        subtask3.setStatus(TaskStatus.IN_PROGRESS);

        taskManager1.updateTask(task1);
        taskManager1.updateTask(task2);
        taskManager1.updateSubtask(subtask1);
        taskManager1.updateSubtask(subtask2);
        taskManager1.updateSubtask(subtask3);

        System.out.println("___________Обновили статус________________");
        System.out.println(taskManager1.getAllTasksList());
        System.out.println(taskManager1.getAllSubtasksList());
        System.out.println(taskManager1.getAllEpicsList());*/

        /*// Удаляем Таск, Эпик и Сабтаск, и проверяем изменения:
        taskManager1.deleteTaskById(task1Id);
        taskManager1.deleteSubtaskById(subtask2Id);
        taskManager1.deleteEpicById(epic2Id);

        System.out.println("___________Удалили Таск, Эпик, и Сабтаск________________");
        System.out.println(taskManager1.getAllTasksList());
        System.out.println(taskManager1.getAllSubtasksList());
        System.out.println(taskManager1.getAllEpicsList());*/

        /*// Удаляем поочерёдно задачи и проверяем изменения:
        taskManager1.deleteSubtaskById(subtask2Id);
        taskManager1.deleteTaskById(task2Id);
        System.out.println("___________История просмотров (Удалили Сабтаск и Таск)________________");
        System.out.println(historyManager1.getTaskHistory());

        taskManager1.deleteEpicById(epic1Id);
        System.out.println("___________История просмотров (Удалили Эпик)________________");
        System.out.println(historyManager1.getTaskHistory());*/

        /*// Ещё раз проверяем историю просмотров после вызова методов:
        System.out.println("___________История просмотров________________");
        System.out.println(historyManager1.getTaskHistory());*/

    // Пробуем восстановить FBTM из файла и проверяем, как восстановились задачи и история просмотров:
    /*TaskManager taskManager2 = FileBackedTasksManager.loadFromFile(Paths.get("./resources/managerData.csv"));
    HistoryManager historyManager2 = taskManager2.getHistoryManager();
    // Распечатаем список Эпиков, Тасков, и Сабтасков:
        System.out.println("___________Восстановили задачи из файла________________");
        System.out.println(taskManager2.getAllTasksList());
        System.out.println(taskManager2.getAllSubtasksList());
        System.out.println(taskManager2.getAllEpicsList());
    // Проверяем историю просмотров:
        System.out.println("___________Восстановили историю из файла________________");
        System.out.println(historyManager2.getTaskHistory());*/
}
