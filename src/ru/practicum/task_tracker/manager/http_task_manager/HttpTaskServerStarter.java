package ru.practicum.task_tracker.manager.http_task_manager;

import java.io.IOException;

public class HttpTaskServerStarter {
    public static void main(String[] args) throws IOException {
        new HttpTaskServer().start();
    }
}