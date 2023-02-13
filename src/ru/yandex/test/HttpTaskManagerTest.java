package ru.yandex.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.manager.task.FileBackedTasksManager;
import ru.yandex.manager.task.TaskManager;
import ru.yandex.server.HttpTaskManager;
import ru.yandex.server.KVServer;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    ru.yandex.server.KVServer kvServer;// создали веб-сервер

    HttpTaskManagerTest() throws IOException {
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskManager = new HttpTaskManager("http://localhost:8078");


        task = taskManager.createTask("Test addNewTask", "Test addNewTask1 description", LocalDate.of(2023, 02, 02), 1L);
        task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description", LocalDate.of(2023, 02, 03), 1L);
        task2 = taskManager.createTask("Test addNewTask2", "Test addNewTask2 description", LocalDate.of(2023, 02, 04), 1L);

        epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description");
        epic1 = taskManager.createEpic("Test addNewEpic1", "Test addNewEpic1 description");
        epic2 = taskManager.createEpic("Test addNewEpic2", "Test addNewEpic2 description");

        subtask = taskManager.createSubtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId(), LocalDate.of(2023, 05, 02), 1L);
        subtask1 = taskManager.createSubtask("Test addNewSubtask1", "Test addNewSubtask1 description", epic.getId(), LocalDate.of(2023, 06, 02), 1L);
        subtask2 = taskManager.createSubtask("Test addNewSubtask2", "Test addNewSubtask2 description", epic.getId(), LocalDate.of(2023, 07, 02), 1L);
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }


    @Test
    public void shouldCorrectlySaveAndLoad() throws IOException {
        taskManager.getTaskById(1);
        taskManager.getEpicById(4);
        taskManager.getSubtaskById(7);
        //FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile("testResources/test.scv");

        assertEquals(task.toString(), taskManager.getTaskById(1).toString());
        assertEquals(epic.toString(), taskManager.getEpicById(4).toString());
        assertEquals(subtask.toString(), taskManager.getSubtaskById(7).toString());
        assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    public void shouldSaveAndLoadEmptyTasksEpicsSubtasks() throws IOException {


        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubtasks();

        //FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile("testResources/test.scv");

        assertTrue(taskManager.getTasks().isEmpty());
        assertTrue(taskManager.getEpics().isEmpty());
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() throws IOException {

        //FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile("testResources/test.scv");

        assertTrue(taskManager.getHistory().isEmpty());
    }
}
