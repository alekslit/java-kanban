package ru.practicum.task_tracker.manager.kv_server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private static final String URL = "http://localhost:8078/";
    private final String apiToken;
    private final HttpClient httpClient;

    public KVTaskClient() {
        httpClient = HttpClient.newHttpClient();
        apiToken = register();
    }

    // Метод для считывания apiToken из ответа на запрос:
    public String register() {
        URI uri = URI.create(URL + "register");
        HttpResponse<String> response = null;

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = httpClient.send(httpRequest, handler);

            int status = response.statusCode();
            if (status != 200) {
                System.out.println("Сервер не смог обработать запрос, код состояния: " + status);
                return null;
            }

        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        if (response == null) {
            return null;
        }
        return response.body();
    }

    // Метод для сохранения состояния менеджера задач:
    public void put(String key, String json) {
        URI uri = URI.create(URL + "save/" + key + "/?API_TOKEN=" + apiToken);
        HttpResponse<String> response;

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = httpClient.send(httpRequest, handler);

            int status = response.statusCode();
            if (status != 200) {
                System.out.println("Сервер не смог обработать запрос, код состояния: " + status);
            }
        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }
    }

    // Метод возвращает состояние менеджера задач:
    public String load(String key) {
        URI uri = URI.create(URL + "load/" + key + "/?API_TOKEN=" + apiToken);
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        String value = "";

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, handler);
            value = response.body();

            int status = response.statusCode();
            if (status == 404) {
                return null;
            }
            if (status != 200) {
                System.out.println("Сервер не смог обработать запрос, код состояния: " + status);
                return null;
            }

        } catch (InterruptedException | IOException exception) {
            System.out.println("Во время выполнения запроса возникла ошибка. " + exception.getMessage());
        }

        return value;
    }
}