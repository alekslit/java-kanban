package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTasksManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTasksManager();
        init();
    }
}