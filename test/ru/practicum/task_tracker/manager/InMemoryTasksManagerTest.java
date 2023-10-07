package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTasksManager> {


    public InMemoryTasksManagerTest() {
        super.taskManager = new InMemoryTasksManager();
    }
    @BeforeEach
    public void beforeEach() {
        // Обнуляем менеджер перед каждым новым тестом:
        super.taskManager.deleteAllTasks();
        super.taskManager.deleteAllSubtasks();
        super.taskManager.deleteAllEpics();
    }
}
