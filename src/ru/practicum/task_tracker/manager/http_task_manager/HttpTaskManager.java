package ru.practicum.task_tracker.manager.http_task_manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.practicum.task_tracker.manager.FileBackedTasksManager;
import ru.practicum.task_tracker.manager.kv_server.KVTaskClient;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private final Gson gson;
    public HttpTaskManager() {
        this.kvTaskClient = new KVTaskClient();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        load();
    }

    private void load(){
        // Восстанавливаем Таски, Сабтаски, Эпики:
        String tasksString = kvTaskClient.load("task");
        if (tasksString != null) {
            List<Task> tasksList = gson.fromJson(tasksString, new TypeToken<ArrayList<Task>>() {});
            tasksList.forEach(task -> tasks.put(task.getId(), task));
        }
        String subtasksString = kvTaskClient.load("subtask");
        if (subtasksString != null) {
            List<Subtask> subtasksList = gson.fromJson(subtasksString, new TypeToken<ArrayList<Subtask>>() {});
            subtasksList.forEach(subtask -> subtasks.put(subtask.getId(), subtask));
        }
        String epicsString = kvTaskClient.load("epic");
        if (epicsString != null) {
            List<Epic> epicsList = gson.fromJson(epicsString, new TypeToken<ArrayList<Epic>>() {});
            epicsList.forEach(epic -> epics.put(epic.getId(), epic));
        }

        // Восстанавливаем историю:
        String historyString = kvTaskClient.load("history");
        if (historyString != null) {
            List<Task> historyList = gson.fromJson(historyString, new TypeToken<ArrayList<Task>>() {});
            for (Task task: historyList) {
                int taskId = task.getId();
                if (tasks.containsKey(taskId)) {
                    getHistoryManager().addTask(tasks.get(taskId));
                } else if (subtasks.containsKey(taskId)) {
                    getHistoryManager().addTask(subtasks.get(taskId));
                } else if (epics.containsKey(taskId)) {
                    getHistoryManager().addTask(epics.get(taskId));
                }
            }
        }

        // Восстанавливаем отсортированный список:
        String sortedTasksString = kvTaskClient.load("tasks");
        if (sortedTasksString != null) {
            List<Task> sortedTasks = gson.fromJson(sortedTasksString, new TypeToken<ArrayList<Task>>() {});
            for (Task task: sortedTasks) {
                int taskId = task.getId();
                if (tasks.containsKey(taskId)) {
                    sortedTaskList.add(tasks.get(taskId));
                } else if (subtasks.containsKey(taskId)) {
                    sortedTaskList.add(subtasks.get(taskId));
                }
            }
        }
    }

    @Override
    public void save() {
        // Сохраняем Таски, Сабтакски и Эпики:
        String tasksString = gson.toJson(tasks.values());
        kvTaskClient.put("task", tasksString);
        String subtasksString = gson.toJson(subtasks.values());
        kvTaskClient.put("subtask", subtasksString);
        String epicsString = gson.toJson(epics.values());
        kvTaskClient.put("epic", epicsString);
        // Сохраняем историю:
        String historyString = gson.toJson(getHistoryManager().getTaskHistory());
        kvTaskClient.put("history", historyString);
        // Сохраняем отсортированный список:
        String sortedTasksString = gson.toJson(getPrioritizedTasks());
        kvTaskClient.put("tasks", sortedTasksString);
    }
}
