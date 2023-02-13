package ru.yandex.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.manager.task.FileBackedTasksManager;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;



public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    protected File file = new File("testResources/test.scv");

    FileBackedTasksManagerTest() throws IOException {
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        taskManager = new FileBackedTasksManager("testResources/test.scv");

        task = taskManager.createTask("Test addNewTask", "Test addNewTask1 description", LocalDate.of(2023,02,02), 1L);
        task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",LocalDate.of(2023,02,03),1L);
        task2 = taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",LocalDate.of(2023,02,04),1L);

        epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description");
        epic1 = taskManager.createEpic("Test addNewEpic1", "Test addNewEpic1 description");
        epic2 = taskManager.createEpic("Test addNewEpic2", "Test addNewEpic2 description");

        subtask = taskManager.createSubtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId(),LocalDate.of(2023,05,02),1L);
        subtask1 = taskManager.createSubtask("Test addNewSubtask1", "Test addNewSubtask1 description", epic.getId(),LocalDate.of(2023,06,02),1L);
        subtask2 = taskManager.createSubtask("Test addNewSubtask2", "Test addNewSubtask2 description", epic.getId(),LocalDate.of(2023,07,02),1L);
    }

   @AfterEach
    public void afterEach() {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void shouldCorrectlySaveAndLoad() throws IOException {
        taskManager.getTaskById(1);
        taskManager.getEpicById(4);
        taskManager.getSubtaskById(7);
        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile("testResources/test.scv");

        assertEquals(task.toString(), fileManager.getTaskById(1).toString());
        assertEquals(epic.toString(), fileManager.getEpicById(4).toString());
        assertEquals(subtask.toString(), fileManager.getSubtaskById(7).toString());
        assertEquals(3, fileManager.getHistory().size());
    }

    @Test
    public void shouldSaveAndLoadEmptyTasksEpicsSubtasks() throws IOException {


        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubtasks();

        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile("testResources/test.scv");

        assertTrue(fileManager.getTasks().isEmpty());
        assertTrue(fileManager.getEpics().isEmpty());
        assertTrue(fileManager.getSubtasks().isEmpty());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() throws IOException {

        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile("testResources/test.scv");

        assertTrue(fileManager.getHistory().isEmpty());
    }

}
