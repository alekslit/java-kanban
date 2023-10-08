package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

// Класс для второй реализации менеджера задач:
public class FileBackedTasksManager extends InMemoryTasksManager {
    // Переменная для передачи информации из файла:
    private final Path managerData;

    public FileBackedTasksManager(Path managerData) {
        this.managerData = managerData;
    }

    //Метод для автосохранения текущего состояния менеджера в файл:
    public void save() {
        // Предварительно сохраняем данные в переменную:
        String managerDataToString = "id,,type,,name,,status,,description,,duration,,startTime,,epic" + "\n";

        // Пройдёмся ID-шниками по мапам, соберём задачи:
        for (int i = 1; i <= currentIdNumber; i++) {
            if (tasks.containsKey(i)) {
                managerDataToString += DataManager.toString(tasks.get(i)) + "\n";
            } else if (subtasks.containsKey(i)) {
                managerDataToString += DataManager.toString(subtasks.get(i)) + "\n";
            } else if (epics.containsKey(i)) {
                managerDataToString += DataManager.toString(epics.get(i)) + "\n";
            }
        }

        // Отделяем задачи от истории пустой строкой и добавляем историю:
        managerDataToString += "\n" + DataManager.historyToString(getHistoryManager());
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(managerData.toFile()))) {
            fileWriter.write(managerDataToString);
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка записи файла: " + exception.getMessage());
        }
    }

    // Метод восстанавливает данные менеджера из файла:
    public static FileBackedTasksManager loadFromFile (Path managerData) {
        FileBackedTasksManager taskManager = new FileBackedTasksManager(managerData);

        try (BufferedReader fileReader = new BufferedReader(new FileReader(String.valueOf(managerData)))) {
            // Пропускаем заголовок в файле:
            fileReader.readLine();
            while (fileReader.ready()) {
                // Восстанавливаем задачи из файла:
                String taskString = fileReader.readLine();
                if (!taskString.isEmpty()) {
                    if (DataManager.fromString(taskString) instanceof Epic) {
                        taskManager.addNewEpic((Epic) DataManager.fromString(taskString));
                    } else if (DataManager.fromString(taskString) instanceof Subtask) {
                        taskManager.addNewSubtask((Subtask) DataManager.fromString(taskString));
                    } else if (DataManager.fromString(taskString) instanceof Task) {
                        taskManager.addNewTask(DataManager.fromString(taskString));
                    }
                } else {
                    break;
                }
            }

            // Восстанавливаем историю из файла:
            while (fileReader.ready()) {
                String historyString = fileReader.readLine();
                List<Integer> historyIds = DataManager.historyFromString(historyString);
                for (Integer id: historyIds) {
                    if (tasks.containsKey(id)) {
                        taskManager.getTaskById(id);
                    } else if (subtasks.containsKey(id)) {
                        taskManager.getSubtaskById(id);
                    } else if (epics.containsKey(id)) {
                        taskManager.getEpicById(id);
                    }
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка чтения файла: " + exception.getMessage());
        }

        return taskManager;
    }

    /* Переопределяем методы добавления задач и добавляем в них автосохранение в файл */
    @Override
    public Integer addNewTask(Task task) {
        Integer taskId = super.addNewTask(task);
        save();
        return taskId;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int epicId = super.addNewEpic(epic);
        save();
        return epicId;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        Integer subtaskId = super.addNewSubtask(subtask);
        save();
        return subtaskId;
    }

    /* Переопределяем методы вызова задач по ID и добавляем в них автосохранение в файл */
    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        save();
        return subtask;
    }

    /* Переопределяем методы удаления всех задач и добавляем в них автосохранение в файл */
    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    /* Перепоределяем методы обновления задач + save() */
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    /* Переопределяем методы удаления задач по ID + save() */
    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }
}