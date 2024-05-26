package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.http_task_manager.HttpTaskServer;
import ru.practicum.task_tracker.manager.kv_server.KVServer;

import java.io.IOException;

public class TaskTrackerApp {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();
    }
}