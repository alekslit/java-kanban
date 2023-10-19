package ru.practicum.task_tracker.manager.http_task_manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private final TaskManager taskManager = Managers.getDefault();
    private final HttpServer httpServer;
    private static String response;

    public HttpTaskServer() throws IOException{
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }

    public void start() {
        httpServer.start();
        System.out.println("Сервер HttpTaskServer запущен, порт: " + PORT);
    }

    public void stop() {
        System.out.println("Останавливаем сервер HttpTaskServer.");
        httpServer.stop(0);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String method = httpExchange.getRequestMethod();
                System.out.println("Обрабатываем " + method + " запрос от клиента.");
                String[] uri = httpExchange.getRequestURI().toString().split("/");
                // Параметры в запросе (id):
                String taskIdString = httpExchange.getRequestURI().getQuery();
                // Булева переменная для случаев где в запросе есть id (кроме Сабтасков Эпика):
                boolean isLengthIsFourAndIdIsPresent = (uri.length == 4 && taskIdString != null
                        && taskIdString.substring(0, 2).equals("id"));

                switch (method) {
                    case "GET":
                        if (uri.length == 2) {
                            // GET /tasks/:
                            getPrioritizedTasksEndpoint(httpExchange);
                            break;
                        }

                        if (uri[2].equals("task")) {
                            // GET /tasks/task/:
                            if (uri.length == 3) {
                                getAllTasksListEndpoint(httpExchange);
                            } else if (isLengthIsFourAndIdIsPresent) {
                                // GET /tasks/task/?id=:
                                Optional<Integer> optionalTaskId = getOptionalTaskId(taskIdString
                                        .substring(3));
                                getTaskByIdEndpoint(httpExchange, optionalTaskId);
                            } else {
                                getNotSupportedRequestWarning(httpExchange);
                                break;
                            }

                            break;
                        } else if (uri[2].equals("subtask")) {
                            // GET /tasks/subtasks/:
                            if (uri.length == 3) {
                                getAllSubtasksListEndpoint(httpExchange);
                            } else if (isLengthIsFourAndIdIsPresent) {
                                // GET /tasks/subtask/?id=:
                                Optional<Integer> optionalSubtaskId = getOptionalTaskId(taskIdString
                                        .substring(3));
                                getSubtaskByIdEndpoint(httpExchange, optionalSubtaskId);
                            } else if (uri.length == 5 && taskIdString != null
                                    && taskIdString.substring(0, 2).equals("id")) {
                                // GET /tasks/subtask/epic/?id=:
                                Optional<Integer> optionalEpicId = getOptionalTaskId(taskIdString
                                        .substring(3));
                                getEpicSubtasksEndpoint(httpExchange, optionalEpicId);
                            } else {
                                getNotSupportedRequestWarning(httpExchange);
                                break;
                            }

                            break;
                        } else if (uri[2].equals("epic")) {
                            // GET /tasks/epic/:
                            if (uri.length == 3) {
                                getAllEpicsListEndpoint(httpExchange);
                            } else if (isLengthIsFourAndIdIsPresent){
                                // GET /tasks/epic/?id=:
                                Optional<Integer> optionalEpicId = getOptionalTaskId(taskIdString
                                        .substring(3));
                                getEpicByIdEndpoint(httpExchange, optionalEpicId);
                            } else {
                                getNotSupportedRequestWarning(httpExchange);
                                break;
                            }

                            break;
                        } else if (uri.length == 3 && uri[2].equals("history")) {
                            // GET /tasks/history/:
                            getHistoryManagerEndpoint(httpExchange);
                            break;
                        }

                        getNotSupportedRequestWarning(httpExchange);
                        break;
                    case "POST":
                        if (uri.length == 3 && uri[2].equals("task")) {
                            // POST /tasks/task/:
                            getAddNewTaskAndUpdateTaskEndpoint(httpExchange);
                            break;
                        } else if (uri.length == 3 && uri[2].equals("subtask")) {
                            // POST /tasks/subtask/:
                            getAddNewSubtaskAndUpdateSubtaskEndpoint(httpExchange);
                            break;
                        } else if (uri.length == 3 && uri[2].equals("epic")) {
                            // POST /tasks/epic/:
                            getAddNewEpicAndUpdateEpicEndpoint(httpExchange);
                            break;
                        }

                        getNotSupportedRequestWarning(httpExchange);
                        break;
                    case "DELETE":
                        // DELETE /tasks/:
                        if (uri.length == 2) {
                            getDeleteAllEndpoint(httpExchange);
                            break;
                        }

                        if (uri[2].equals("task")) {
                            if (uri.length == 3) {
                                // DELETE /tasks/task/:
                                getDeleteAllTasksEndpoint(httpExchange);
                            } else if (isLengthIsFourAndIdIsPresent) {
                                // DELETE /tasks/task/?id=:
                                Optional<Integer> optionalTaskId = getOptionalTaskId(taskIdString
                                        .substring(3));
                                getDeleteTaskByIdEndpoint(httpExchange, optionalTaskId);
                            } else {
                                getNotSupportedRequestWarning(httpExchange);
                                break;
                            }

                            break;
                        } else if (uri[2].equals("subtask")) {
                            if (uri.length == 3) {
                                // DELETE /tasks/subtasks/:
                                getDeleteAllSubtasksEndpoint(httpExchange);
                            } else if (isLengthIsFourAndIdIsPresent) {
                                // DELETE /tasks/subtask/?id=:
                                Optional<Integer> optionalSubtaskId = getOptionalTaskId(taskIdString
                                        .substring(3));
                                getDeleteSubtaskByIdEndpoint(httpExchange, optionalSubtaskId);
                            } else {
                                getNotSupportedRequestWarning(httpExchange);
                                break;
                            }

                            break;
                        } else if (uri[2].equals("epic")) {
                            if (uri.length == 3) {
                                // DELETE /tasks/epic/:
                                getDeleteAllEpicsEndpoint(httpExchange);
                            } else if (isLengthIsFourAndIdIsPresent){
                                // DELETE /tasks/epic/?id=:
                                Optional<Integer> optionalEpicId = getOptionalTaskId(taskIdString
                                        .substring(3));
                                getDeleteEpicByIdEndpoint(httpExchange, optionalEpicId);
                            } else {
                                getNotSupportedRequestWarning(httpExchange);
                                break;
                            }

                            break;
                        }
                        getNotSupportedRequestWarning(httpExchange);
                        break;

                    default:
                        response = "Такой метод запроса не поддерживается.";
                        writeResponse(httpExchange, response, 400);
                        break;
                }
            } catch (IOException exception) {
                System.out.println("Во время обработки запроса возникла ошибка. " + exception.getMessage());
            } finally {
                httpExchange.close();
            }
        }

        // Метод для отправки ответа на запрос от клиента:
        private void writeResponse(HttpExchange httpExchange,
                                    String responseString,
                                    int responseCode) throws IOException {
            if (responseString.isBlank()) {
                httpExchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                httpExchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(bytes);
                }
            }

            httpExchange.close();
        }

        // Метод для оборачивания Id в Optional:
        private Optional<Integer> getOptionalTaskId(String id) {
            try {
                return Optional.of(Integer.parseInt(id));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
        }

        /*---Методы для получения эндпоинтов---*/
        private void getPrioritizedTasksEndpoint(HttpExchange httpExchange) throws IOException {
            if (!taskManager.getPrioritizedTasks().isEmpty()){
                System.out.println(taskManager.getPrioritizedTasks());
                response = gson.toJson(taskManager.getPrioritizedTasks());
                writeResponse(httpExchange, response, 200);
            } else {
                response = "Отсортированный список пуст.";
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getAllTasksListEndpoint(HttpExchange httpExchange) throws IOException {
            if (!taskManager.getAllTasksList().isEmpty()){
                System.out.println(taskManager.getAllTasksList());
                response = gson.toJson(taskManager.getAllTasksList());
                writeResponse(httpExchange, response, 200);
            } else {
                response = "Список задач пуст.";
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getTaskByIdEndpoint(HttpExchange httpExchange,
                                         Optional<Integer> optionalTaskId) throws IOException {
            if (optionalTaskId.isEmpty()) {
                response = "В запросе некорректно указан Id задачи.";
                writeResponse(httpExchange, response, 400);
                return;
            }

            int taskId = optionalTaskId.get();
            if (taskManager.getTaskById(taskId) == null) {
                response = "Задачи с таким Id = " + taskId + " не существует.";
                writeResponse(httpExchange, response, 404);
            } else {
                System.out.println(taskManager.getTaskById(taskId));
                response = gson.toJson(taskManager.getTaskById(taskId));
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getNotSupportedRequestWarning(HttpExchange httpExchange) throws IOException {
            response = "Такой запрос не поддерживается: " + httpExchange.getRequestURI().toString();
            writeResponse(httpExchange, response, 404);
            httpExchange.close();
        }

        private void getAllSubtasksListEndpoint(HttpExchange httpExchange) throws IOException {
            if (!taskManager.getAllSubtasksList().isEmpty()) {
                System.out.println(taskManager.getAllSubtasksList());
                response = gson.toJson(taskManager.getAllSubtasksList());
                writeResponse(httpExchange, response, 200);
            } else {
                response = "Список задач пуст.";
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getSubtaskByIdEndpoint(HttpExchange httpExchange,
                                            Optional<Integer> optionalSubtaskId) throws IOException {
            if (optionalSubtaskId.isEmpty()) {
                response = "В запросе некорректно указан Id задачи.";
                writeResponse(httpExchange, response, 400);
                return;
            }

            int subtaskId = optionalSubtaskId.get();
            if (taskManager.getSubtaskById(subtaskId) == null) {
                response = "Задачи с таким Id = " + subtaskId + " не существует.";
                writeResponse(httpExchange, response, 404);
            } else {
                System.out.println(taskManager.getSubtaskById(subtaskId));
                response = gson.toJson(taskManager.getSubtaskById(subtaskId));
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getEpicSubtasksEndpoint(HttpExchange httpExchange,
                                             Optional<Integer> optionalEpicId) throws IOException {
            if (optionalEpicId.isEmpty()) {
                response = "В запросе некорректно указан Id задачи.";
                writeResponse(httpExchange, response, 400);
                return;
            }

            int epicId = optionalEpicId.get();
            if (taskManager.getEpicById(epicId) == null
                    || taskManager.getEpicSubtasks(epicId).isEmpty()) {
                response = "Задачи с таким Id = " + epicId + " не существует, или у неё нет подзадач.";
                writeResponse(httpExchange, response, 404);
            } else {
                System.out.println(taskManager.getEpicSubtasks(epicId));
                response = gson.toJson(taskManager.getEpicSubtasks(epicId));
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getAllEpicsListEndpoint(HttpExchange httpExchange) throws IOException {
            if (!taskManager.getAllEpicsList().isEmpty()) {
                System.out.println(taskManager.getAllEpicsList());
                response = gson.toJson(taskManager.getAllEpicsList());
                writeResponse(httpExchange, response, 200);
            } else {
                response = "Список задач пуст.";
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getEpicByIdEndpoint(HttpExchange httpExchange,
                                         Optional<Integer> optionalEpicId) throws IOException {
            if (optionalEpicId.isEmpty()) {
                response = "В запросе некорректно указан Id задачи.";
                writeResponse(httpExchange, response, 400);
                return;
            }

            int epicId = optionalEpicId.get();
            if (taskManager.getEpicById(epicId) == null) {
                response = "Задачи с таким Id = " + epicId + " не существует.";
                writeResponse(httpExchange, response, 404);
            } else {
                System.out.println(taskManager.getEpicById(epicId));
                response = gson.toJson(taskManager.getEpicById(epicId));
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getHistoryManagerEndpoint(HttpExchange httpExchange) throws IOException {
            if (!taskManager.getHistoryManager().getTaskHistory().isEmpty()) {
                System.out.println(taskManager.getHistoryManager().getTaskHistory());
                response = gson.toJson(taskManager.getHistoryManager().getTaskHistory());
                writeResponse(httpExchange, response, 200);
            } else {
                response = "История просмотров пуста.";
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getAddNewTaskAndUpdateTaskEndpoint(HttpExchange httpExchange) throws IOException {
            InputStream inputStream = httpExchange.getRequestBody();
            String taskToString = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            try {
                Task task = gson.fromJson(taskToString, Task.class);
                if (task.getName() != null && task.getDescription() != null
                        && task.getStatus() != null && task.getDuration() != null
                        && task.getStartTime() != null) {
                    if (taskManager.getAllTasksList().contains(task)) {
                        // update:
                        taskManager.updateTask(task);
                        response = "Задача успешно обновлена.";
                        writeResponse(httpExchange, response, 201);
                    } else {
                        // add:
                        Integer taskId = taskManager.addNewTask(task);
                        if (taskId == null) {
                            response = "Невозможно создать задачу/подзадачу, есть пересечение по времени "
                                    + " выполнения с текущими задачами/подзадачами.";
                            writeResponse(httpExchange, response, 400);
                        } else {
                            response = "Добавлена новая задача.";
                            writeResponse(httpExchange, response, 201);
                        }
                    }
                } else {
                    response = "Поля задачи не могут быть пустыми.";
                    writeResponse(httpExchange, response, 400);
                }
            } catch (JsonSyntaxException exception) {
                response = "Передан некорректный JSON.";
                writeResponse(httpExchange, response, 404);
            }
        }

        private void getAddNewSubtaskAndUpdateSubtaskEndpoint(HttpExchange httpExchange) throws IOException {
            InputStream inputStream = httpExchange.getRequestBody();
            String subtaskToString = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            try {
                Subtask subtask = gson.fromJson(subtaskToString, Subtask.class);
                if (subtask.getName() != null && subtask.getDescription() != null
                        && subtask.getStatus() != null && subtask.getDuration() != null
                        && subtask.getStartTime() != null && subtask.getEpicId() != null) {
                    if (taskManager.getAllSubtasksList().contains(subtask)) {
                        // update:
                        taskManager.updateSubtask(subtask);
                        response = "Задача успешно обновлена.";
                        writeResponse(httpExchange, response, 201);
                    } else {
                        // add:
                        if (taskManager.getAllEpicsList()
                                .contains(taskManager.getEpicById(subtask.getEpicId()))) {
                            Integer subtaskId = taskManager.addNewSubtask(subtask);
                            if (subtaskId == null) {
                                response = "Невозможно создать задачу/подзадачу, есть пересечение по времени "
                                        + " выполнения с текущими задачами/подзадачами.";
                                writeResponse(httpExchange, response, 400);
                            } else {
                                response = "Добавлена новая задача.";
                                writeResponse(httpExchange, response, 201);
                            }
                        } else {
                            response = "У этой подзадачи не существует Эпика.";
                            writeResponse(httpExchange, response, 400);
                        }
                    }
                } else {
                    response = "Поля задачи не могут быть пустыми.";
                    writeResponse(httpExchange, response, 400);
                }
            } catch (JsonSyntaxException exception) {
                response = "Передан некорректный JSON.";
                writeResponse(httpExchange, response, 404);
            }
        }

        private void getAddNewEpicAndUpdateEpicEndpoint(HttpExchange httpExchange) throws IOException {
            InputStream inputStream = httpExchange.getRequestBody();
            String epicToString = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            try {
                Epic epic = gson.fromJson(epicToString, Epic.class);
                if (epic.getName() != null && epic.getDescription() != null) {
                    if (taskManager.getAllEpicsList().contains(epic)) {
                        // update:
                        taskManager.updateEpic(epic);
                        response = "Задача успешно обновлена.";
                        writeResponse(httpExchange, response, 201);
                    } else {
                        // add:
                        taskManager.addNewEpic(epic);
                        response = "Добавлена новая задача.";
                        writeResponse(httpExchange, response, 201);
                    }
                } else {
                    response = "Поля задачи не могут быть пустыми.";
                    writeResponse(httpExchange, response, 400);
                }
            } catch (JsonSyntaxException exception) {
                response = "Передан некорректный JSON.";
                writeResponse(httpExchange, response, 404);
            }
        }

        private void getDeleteAllEndpoint(HttpExchange httpExchange) throws IOException {
            taskManager.deleteAll();
            response = "Все данные удалены.";
            writeResponse(httpExchange, response, 200);
        }

        private void getDeleteAllTasksEndpoint(HttpExchange httpExchange) throws IOException {
            taskManager.deleteAllTasks();
            response = "Все задачи удалены.";
            writeResponse(httpExchange, response, 200);
        }

        private void getDeleteTaskByIdEndpoint(HttpExchange httpExchange,
                                               Optional<Integer> optionalTaskId) throws IOException {
            if (optionalTaskId.isEmpty()) {
                response = "В запросе некорректно указан Id задачи.";
                writeResponse(httpExchange, response, 400);
                return;
            }

            int taskId = optionalTaskId.get();
            if (taskManager.getTaskById(taskId) == null) {
                response = "Задачи с таким Id = " + taskId + " не существует.";
                writeResponse(httpExchange, response, 404);
            } else {
                taskManager.deleteTaskById(taskId);
                response = "Задача с id = " + taskId + " удалена.";
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getDeleteAllSubtasksEndpoint(HttpExchange httpExchange) throws IOException {
            taskManager.deleteAllSubtasks();
            response = "Все задачи удалены.";
            writeResponse(httpExchange, response, 200);
        }

        private void getDeleteSubtaskByIdEndpoint(HttpExchange httpExchange,
                                                  Optional<Integer> optionalSubtaskId) throws IOException {
            if (optionalSubtaskId.isEmpty()) {
                response = "В запросе некорректно указан Id задачи.";
                writeResponse(httpExchange, response, 400);
                return;
            }

            int subtaskId = optionalSubtaskId.get();
            if (taskManager.getSubtaskById(subtaskId) == null) {
                response = "Задачи с таким Id = " + subtaskId + " не существует.";
                writeResponse(httpExchange, response, 404);
            } else {
                taskManager.deleteSubtaskById(subtaskId);
                response = "Задача с id = " + subtaskId + " удалена.";
                writeResponse(httpExchange, response, 200);
            }
        }

        private void getDeleteAllEpicsEndpoint(HttpExchange httpExchange) throws IOException {
            taskManager.deleteAllEpics();
            response = "Все задачи удалены.";
            writeResponse(httpExchange, response, 200);
        }

        private void getDeleteEpicByIdEndpoint(HttpExchange httpExchange,
                                               Optional<Integer> optionalEpicId) throws IOException {
            if (optionalEpicId.isEmpty()) {
                response = "В запросе некорректно указан Id задачи.";
                writeResponse(httpExchange, response, 400);
                return;
            }

            int epicId = optionalEpicId.get();
            if (taskManager.getEpicById(epicId) == null) {
                response = "Задачи с таким Id = " + epicId + " не существует.";
                writeResponse(httpExchange, response, 404);
            } else {
                taskManager.deleteEpicById(epicId);
                response = "Задача с id = " + epicId + " удалена.";
                writeResponse(httpExchange, response, 200);
            }
        }
    }
}