package ru.practicum.task_tracker.manager.kv_server;

import java.io.IOException;

public class KVServerStarter {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }
}