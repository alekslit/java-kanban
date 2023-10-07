package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Утилитарный класс обеспечивающий работу с файлом данных менеджера:
public class DataManager {

    private DataManager() {
    }

    // Метод преобразует задачу в строку:
    public static String toString(Task task) {
        if (task == null) {
            System.out.println("task = null");
            return null;
        }

        // Переменная для сбора строки:
        String result = task.getId() + ",,"
                + task.getClass().getSimpleName().toUpperCase() + ",,"
                + task.getName() + ",,"
                + task.getStatus() + ",,"
                + task.getDescription() + ",,"
                + task.getDuration() + ",,"
                + task.getStartTime() + ",,";
        // Если это Сабтаск то добавим в конце ID его Эпика:
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            result += subtask.getEpicId();
        }

        return result;
    }

    // Метод из строки создаёт задачу:
    // Формат строки: (id-[0],type-[1],name-[2],status-[3],description-[4],duration[5],endTime[6],epic[7])
    public static Task fromString(String value) {
        // Делим строку на массив строк элементов задачи:
        String[] taskElements = value.split(",,");
        // Разносим элементы по переменным:
        int taskId = Integer.parseInt(taskElements[0]);
        TypeOfTask taskType = TypeOfTask.valueOf(taskElements[1]);
        String taskName = taskElements[2];
        TaskStatus taskStatus = TaskStatus.valueOf(taskElements[3]);
        String taskDescription = taskElements[4];
        int taskDuration = Integer.parseInt(taskElements[5]);
        LocalDateTime taskStartTime = LocalDateTime.parse(taskElements[6]);
        int epicId = 0;
        if (taskElements.length == 8){
            epicId = Integer.parseInt(taskElements[7]);
        }

        switch (taskType) {
            case TASK:
                return new Task(taskId, taskName, taskDescription, taskStatus,
                        taskDuration, taskStartTime);
            case SUBTASK:
                return new Subtask(taskId, taskName, taskDescription, taskStatus,
                        taskDuration, taskStartTime, epicId);
            case EPIC:
                return new Epic(taskId, taskName, taskDescription, taskStatus,
                        taskDuration, taskStartTime);
        }

        System.out.println("Неизвестный тип задачи.");
        return null;
    }

    // Метод преобразует историю просмотров в строку ID-шников:
    public static String historyToString(HistoryManager manager) {
        // Соберём ID задач в массив:
        String[] taskIds = new String[manager.getTaskHistory().size()];

        for (int i = 0; i < manager.getTaskHistory().size(); i++) {
            taskIds[i] = String.valueOf(manager.getTaskHistory().get(i).getId());
        }

        // Собираем ID-шники в одну строку:
        String historyString = String.join(",," ,taskIds);
        return historyString;
    }

    // Метод для восстановления менеджера истории из строки:
    public static List<Integer> historyFromString(String value) {
        // Разделяем ID-шники задач + создаём список для сбора ID-шников задач:
        String[] taskIds = value.split(",,");
        List<Integer> idsList = new ArrayList<>();

        for (String id: taskIds) {
            idsList.add(Integer.parseInt(id));
        }

        return idsList;
    }
}
