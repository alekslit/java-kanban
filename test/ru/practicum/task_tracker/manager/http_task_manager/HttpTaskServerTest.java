package ru.practicum.task_tracker.manager.http_task_manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.manager.kv_server.KVServer;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final String URL = "http://localhost:8080";
    private TaskManager taskManager;
    private static Task task;
    private static Epic epic;
    private static Subtask subtask1;
    private static Subtask subtask2;
    private static LocalDateTime data1;
    private static LocalDateTime data2;
    private static LocalDateTime data3;
    private static int duration;
    private static HttpClient httpClient;

    public void init() {
        data1 = LocalDateTime.of(2000, 1, 1, 3, 0);
        data2 = LocalDateTime.of(2000, 1, 1, 5, 0);
        data3 = LocalDateTime.of(2000, 1, 1, 1, 0);
        duration = 60;
        taskManager = httpTaskServer.getTaskManager();
        httpClient = HttpClient.newHttpClient();
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
    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        init();
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    // Тестируем эндпоинт GET /tasks/task/ (получаем список всех Тасков):
    @Test
    public void getAllTasksList() {
       final URI uri = URI.create(URL + "/tasks/task/");
       List<Task> tasksListResponse = new ArrayList<>();
       final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
       try {
           HttpRequest httpRequest = HttpRequest.newBuilder()
                   .uri(uri)
                   .GET()
                   .build();

           HttpResponse<String> response = httpClient.send(httpRequest, handler);
           tasksListResponse = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {});
       } catch (InterruptedException | IOException exception) {
           System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
       }

        List<Task> tasksList = taskManager.getAllTasksList();

        assertEquals(tasksList, tasksListResponse,
                "Данные различаются, в ответ на запрос пришли некорректные данные (getAllTasksList)");
    }

    // Тестируем эндпоинт GET /tasks/subtask/ (получаем список всех Сабтасков):
    @Test
    public void getAllSubtasksList() {
        final URI uri = URI.create(URL + "/tasks/subtask/");
        List<Subtask> subtasksListResponse = new ArrayList<>();
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            subtasksListResponse = gson.fromJson(response.body(), new TypeToken<ArrayList<Subtask>>() {});
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        List<Subtask> subtasksList = taskManager.getAllSubtasksList();

        assertEquals(subtasksList, subtasksListResponse,
                "Данные различаются, в ответ на запрос пришли некорректные данные (getAllSubtasksList)");
    }

    // Тестируем эндпоинт GET /tasks/epic/ (получаем список всех Эпиков):
    @Test
    public void getAllEpicsList() {
        final URI uri = URI.create(URL + "/tasks/epic/");
        List<Epic> epicsListResponse = new ArrayList<>();
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            epicsListResponse = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {});
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        List<Epic> epicsList = taskManager.getAllEpicsList();

        assertEquals(epicsList, epicsListResponse,
                "Данные различаются, в ответ на запрос пришли некорректные данные (getAllEpicsList)");
    }

    // Тестируем эндпоинт GET /tasks/task/?id= (получаем Таск по ID):
    @Test
    public void getTaskById() {
        final URI uri = URI.create(URL + "/tasks/task/?id=1");
        Task taskResponse = null;
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            taskResponse = gson.fromJson(response.body(), Task.class);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        Task taskExpected = taskManager.getTaskById(1);

        assertEquals(taskExpected, taskResponse,
                "Данные различаются, в ответ на запрос пришли некорректные данные (getTaskById)");
    }

    // Тестируем эндпоинт GET /tasks/subtask/?id= (получаем Сабтаск по ID):
    @Test
    public void getSubtaskById() {
        final URI uri = URI.create(URL + "/tasks/subtask/?id=3");
        Subtask subtaskResponse = null;
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            subtaskResponse = gson.fromJson(response.body(), Subtask.class);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        Subtask subtaskExpected = taskManager.getSubtaskById(3);

        assertEquals(subtaskExpected, subtaskResponse,
                "Данные различаются, в ответ на запрос пришли некорректные данные (getSubtaskById)");
    }

    // Тестируем эндпоинт GET /tasks/epic/?id= (получаем Эпик по ID):
    @Test
    public void getEpicById() {
        final URI uri = URI.create(URL + "/tasks/epic/?id=2");
        Epic epicResponse = null;
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            epicResponse = gson.fromJson(response.body(), Epic.class);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        Epic epicExpected = taskManager.getEpicById(2);

        assertEquals(epicExpected, epicResponse,
                "Данные различаются, в ответ на запрос пришли некорректные данные (getEpicById)");
    }

    // Тестируем эндпоинт POST /tasks/task/ Body:{...} (Добаляем новый Таск):
    @Test
    public void addNewTask() {
        final URI uri = URI.create(URL + "/tasks/task/");
        // Эта задача будет иметь ID = 5:
        final String jsonTask = "{\n" +
                "\t\"name\": \"Из клиента Имя1\",\n" +
                "\t\"description\": \"Из клиента Описание1\",\n" +
                "\t\"status\": \"NEW\",\n" +
                "\t\"duration\": 60,\n" +
                "\t\"startTime\": {\n" +
                "\t\t\"date\": {\n" +
                "\t\t\t\"year\": 2000,\n" +
                "\t\t\t\"month\": 1,\n" +
                "\t\t\t\"day\": 1\n" +
                "\t\t},\n" +
                "\t\t\"time\": {\n" +
                "\t\t\t\"hour\": 15,\n" +
                "\t\t\t\"minute\": 0,\n" +
                "\t\t\t\"second\": 0,\n" +
                "\t\t\t\"nano\": 0\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                    .build();

            httpClient.send(httpRequest, handler);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        final Task newTask = taskManager.getTaskById(5);

        assertNotNull(newTask, "Не удалось создать новую задачу через запрос (addNewTask)");
    }

    // Тестируем эндпоинт POST /tasks/subtask/ Body:{...} (Добаляем новый Сабтаск):
    @Test
    public void addNewSubtask() {
        final URI uri = URI.create(URL + "/tasks/subtask/");
        // Эта задача будет иметь ID = 5:
        final String jsonSubtask = "\t{\n" +
                "\t\t\"epicId\": 2,\n" +
                "\t\t\"name\": \"Клиент Имя2\",\n" +
                "\t\t\"description\": \"Клиент Описание2\",\n" +
                "\t\t\"status\": \"NEW\",\n" +
                "\t\t\"duration\": 60,\n" +
                "\t\t\"startTime\": {\n" +
                "\t\t\t\"date\": {\n" +
                "\t\t\t\t\"year\": 2001,\n" +
                "\t\t\t\t\"month\": 1,\n" +
                "\t\t\t\t\"day\": 2\n" +
                "\t\t\t},\n" +
                "\t\t\t\"time\": {\n" +
                "\t\t\t\t\"hour\": 11,\n" +
                "\t\t\t\t\"minute\": 0,\n" +
                "\t\t\t\t\"second\": 0,\n" +
                "\t\t\t\t\"nano\": 0\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}";
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask))
                    .build();

            httpClient.send(httpRequest, handler);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        final Subtask newSubtask = taskManager.getSubtaskById(5);

        assertNotNull(newSubtask, "Не удалось создать новую задачу через запрос (addNewSubtask)");
    }

    // Тестируем эндпоинт POST /tasks/epic/ Body:{...} (Добаляем новый Эпик):
    @Test
    public void addNewEpic() {
        final URI uri = URI.create(URL + "/tasks/epic/");
        // Эта задача будет иметь ID = 5:
        final String jsonEpic = "{\n" +
                "\t\t\"name\": \"Клиент Имя3\",\n" +
                "\t\t\"description\": \"Клиент Описание3\"\n" +
                "\t}";
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                    .build();

            httpClient.send(httpRequest, handler);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        final Epic newEpic = taskManager.getEpicById(5);

        assertNotNull(newEpic, "Не удалось создать новую задачу через запрос (addNewEpic)");
    }

    // Тестируем эндпоинт DELETE /tasks/task/?id= (Удаляем Таск по ID):
    @Test
    public void deleteTaskById() {
        final URI uri = URI.create(URL + "/tasks/task/?id=1");
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .DELETE()
                    .build();

            httpClient.send(httpRequest, handler);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        final Task nullTask = taskManager.getTaskById(1);

        assertNull(nullTask, "Не удалось удалить задачу через запрос (deleteTaskById)");
    }

    // Тестируем эндпоинт DELETE /tasks/subtask/?id= (Удаляем Сабтаск по ID):
    @Test
    public void deleteSubtaskById() {
        final URI uri = URI.create(URL + "/tasks/subtask/?id=3");
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .DELETE()
                    .build();

            httpClient.send(httpRequest, handler);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        final Subtask nullSubtask = taskManager.getSubtaskById(3);

        assertNull(nullSubtask, "Не удалось удалить задачу через запрос (deleteSubtaskById)");
    }

    // Тестируем эндпоинт DELETE /tasks/epic/?id= (Удаляем Эпик по ID):
    @Test
    public void deleteEpicByIdAndHisSubtasks() {
        final URI uri = URI.create(URL + "/tasks/epic/?id=2");
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .DELETE()
                    .build();

            httpClient.send(httpRequest, handler);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        final Epic nullEpic = taskManager.getEpicById(2);

        assertNull(nullEpic, "Не удалось удалить задачу через запрос (deleteEpicByIdAndHisSubtasks)");

        final List<Subtask> nullEpicSubtasks = taskManager.getAllSubtasksList();

        assertEquals(0, nullEpicSubtasks.size(),
                "Не удалось удалить подзадачи (deleteEpicByIdAndHisSubtasks)");
    }

    // Тестируем эндпоинт DELETE /tasks/task/ (Удаляем все Таски):
    @Test
    public void deleteAllTasks() {
        final URI uri = URI.create(URL + "/tasks/task/");
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .DELETE()
                    .build();

            httpClient.send(httpRequest, handler);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        final List<Task> taskList = taskManager.getAllTasksList();

        assertEquals(0, taskList.size(),
                "Не удалось удалить задачи (deleteAllTasks)");
    }

    // Тестируем эндпоинт DELETE /tasks/subtask/ (Удаляем все Сабтаски):
    @Test
    public void deleteAllSubtasks() {
        final URI uri = URI.create(URL + "/tasks/subtask/");
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .DELETE()
                    .build();

            httpClient.send(httpRequest, handler);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        final List<Subtask> subtaskList = taskManager.getAllSubtasksList();

        assertEquals(0, subtaskList.size(),
                "Не удалось удалить задачи (deleteAllSubtasks)");
    }

    // Тестируем эндпоинт DELETE /tasks/epic/ (Удаляем все Эпики):
    @Test
    public void deleteAllEpicsAndSubtasks() {
        final URI uri = URI.create(URL + "/tasks/epic/");
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .DELETE()
                    .build();

            httpClient.send(httpRequest, handler);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        final List<Subtask> subtaskList = taskManager.getAllSubtasksList();
        final List<Epic> epicList = taskManager.getAllEpicsList();

        assertEquals(0, subtaskList.size(),
                "Не удалось удалить подзадачи (deleteAllEpicsAndSubtasks)");
        assertEquals(0, epicList.size(),
                "Не удалось удалить задачи (deleteAllEpicsAndSubtasks)");
    }

    // Тестируем эндпоинт GET /tasks/subtask/epic/?id= (получаем список всех Сабтасков Эпика):
    @Test
    public void getEpicSubtasks() {
        final URI uri = URI.create(URL + "/tasks/subtask/epic/?id=2");
        List<Subtask> subtasksListResponse = null;
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            subtasksListResponse = gson.fromJson(response.body(), new TypeToken<ArrayList<Subtask>>() {});
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        List<Subtask> subtasksList = taskManager.getEpicSubtasks(2);

        assertEquals(subtasksList, subtasksListResponse,
                "Данные различаются, в ответ на запрос пришли некорректные данные (getEpicSubtasks)");
    }

    // Тестируем эндпоинт GET /tasks/history/ (получаем историю просмотров):
    @Test
    public void getHistoryList() {
        taskManager.getTaskById(task.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask2.getId());
        final URI uri = URI.create(URL + "/tasks/history/");
        List<Task> historyListResponse = new ArrayList<>();
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            historyListResponse = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {});
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        List<Task> historyList = taskManager.getHistoryManager().getTaskHistory();

        assertEquals(historyList.size(), historyListResponse.size(),
                "Данные различаются, в ответ на запрос пришли некорректные данные (getHistoryList)");
    }

    // Тестируем эндпоинт GET /tasks/ (получаем список задач по приоритету):
    @Test
    public void getSortedTasksList() {
        final URI uri = URI.create(URL + "/tasks/");
        List<Task> sortedTasksListResponse = new ArrayList<>();
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            sortedTasksListResponse = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {});
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        List<Task> sortedTasksList = new ArrayList<>(taskManager.getPrioritizedTasks());

        assertEquals(sortedTasksList.size(), sortedTasksListResponse.size(),
                "Данные различаются, в ответ на запрос пришли некорректные данные (getSortedTasksList)");
    }

    // Тестируем эндпоинт DELETE /tasks/ (Удаляем все данные):
    @Test
    public void deleteAll() {
        final URI uri = URI.create(URL + "/tasks/");
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .DELETE()
                    .build();

            httpClient.send(httpRequest, handler);
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        final List<Task> tasksList = taskManager.getAllTasksList();
        final List<Subtask> subtasksList = taskManager.getAllSubtasksList();
        final List<Epic> epicsList = taskManager.getAllEpicsList();

        assertEquals(0, tasksList.size(),"Не удалось удалить задачи (deleteAll)");
        assertEquals(0, subtasksList.size(),"Не удалось удалить задачи (deleteAll)");
        assertEquals(0, epicsList.size(),"Не удалось удалить задачи (deleteAll)");
    }

/*---Тесты на ошибочный запрос---*/
    @Test
    public void getNotSupportedRequestWarning1() {
        final URI uri = URI.create(URL + "/tasks/task/id/false/");
        String warningExpected = "Такой запрос не поддерживается: " + "/tasks/task/id/false/";
        String warningActual = "";
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            warningActual = response.body();
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        assertEquals(warningExpected, warningActual,
                "Некорректный запрос выполнен (getNotSupportedRequestWarning1)");
    }

    @Test
    public void getNotSupportedRequestWarning2() {
        final URI uri = URI.create(URL + "/tasks/task/id/false/");
        String warningExpected = "Такой метод запроса не поддерживается.";
        String warningActual = "";
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .PUT(HttpRequest.BodyPublishers.ofString("false"))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            warningActual = response.body();
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        assertEquals(warningExpected, warningActual,
                "Некорректный запрос выполнен (getNotSupportedRequestWarning2)");
    }

    @Test
    public void getNotSupportedRequestWarning3() {
        final URI uri = URI.create(URL + "/tasks/epic/false/");
        String warningExpected = "Такой запрос не поддерживается: " + "/tasks/epic/false/";
        String warningActual = "";
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString("false"))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            warningActual = response.body();
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        assertEquals(warningExpected, warningActual,
                "Некорректный запрос выполнен (getNotSupportedRequestWarning3)");
    }

    @Test
    public void getNotSupportedRequestWarning4() {
        final URI uri = URI.create(URL + "/tasks/epic/false/45/");
        String warningExpected = "Такой запрос не поддерживается: " + "/tasks/epic/false/45/";
        String warningActual = "";
        final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            warningActual = response.body();
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        assertEquals(warningExpected, warningActual,
                "Некорректный запрос выполнен (getNotSupportedRequestWarning4)");
    }
}