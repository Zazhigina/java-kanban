package ru.yandex.test;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.manager.task.InMemoryTaskManager;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    InMemoryTaskManagerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws Exception {
        taskManager = new InMemoryTaskManager();
        task = taskManager.createTask("Test addNewTask", "Test addNewTask1 description", LocalDate.of(2023,02,02), Duration.ofDays(1));
        task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",LocalDate.of(2023,02,03),Duration.ofDays(1));
        task2 = taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",LocalDate.of(2023,02,04),Duration.ofDays(1));

        epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description");
        epic1 = taskManager.createEpic("Test addNewEpic1", "Test addNewEpic1 description");
        epic2 = taskManager.createEpic("Test addNewEpic2", "Test addNewEpic2 description");

        subtask = taskManager.createSubtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId(),LocalDate.of(2023,05,02),Duration.ofDays(1));
        subtask1 = taskManager.createSubtask("Test addNewSubtask1", "Test addNewSubtask1 description", epic.getId(),LocalDate.of(2023,06,02),Duration.ofDays(1));
        subtask2 = taskManager.createSubtask("Test addNewSubtask2", "Test addNewSubtask2 description", epic.getId(),LocalDate.of(2023,07,02),Duration.ofDays(1));
    }

}