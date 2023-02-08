package ru.yandex.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.manager.Managers;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.manager.task.InMemoryTaskManager;
import ru.yandex.manager.task.TaskManager;

import java.io.IOException;

import ru.yandex.entites.Epic;
import ru.yandex.entites.Subtask;
import ru.yandex.entites.Task;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryManagerTest<T extends HistoryManager> {
    T historyManager = (T) Managers.getDefaultHistory();
    TaskManager taskManager = new InMemoryTaskManager();;
    Task task;
    Epic epic;
    Subtask subtask;
    Subtask subtask1;

    public HistoryManagerTest() throws IOException {
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        task = taskManager.createTask("Test addNewTask", "Test addNewTask1 description", LocalDate.of(2023,02,02), Duration.ofDays(1));
        epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description");
        subtask = taskManager.createSubtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId(),LocalDate.of(2023,05,02),Duration.ofDays(1));
        subtask1 = taskManager.createSubtask("Test addNewSubtask1", "Test addNewSubtask1 description", epic.getId(),LocalDate.of(2023,06,02),Duration.ofDays(1));

    }

    @Test
    void add() throws IOException {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void addSimilarTasks() throws IOException {
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

    }

    @Test
    void addNull() throws IOException {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    historyManager.add(null);
                });

        assertEquals("Такой задачи нету.Неверный идентификатор", ex.getMessage());
    }


    @Test
    void removeEpic() throws IOException {
        historyManager.add(epic);
        historyManager.remove(epic.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(0, history.size(), "История не пустая.");
    }

    @Test
    void remove() throws IOException {
        historyManager.remove(epic.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(0, history.size(), "История не пустая.");
    }

    @Test
    void removeNull() throws IOException {
        historyManager.remove(0);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(0, history.size(), "История не пустая.");
    }

    @Test
    void getHistory() {
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(0, history.size(), "История не пустая.");
    }

    @Test
    void getHistory2size() throws IOException {
        historyManager.add(task);
        historyManager.add(epic);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История не пустая.");
    }

    @Test
    void getHistory1size() throws IOException {
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

}

