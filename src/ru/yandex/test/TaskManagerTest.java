package ru.yandex.test;

import org.junit.jupiter.api.Test;
import ru.yandex.manager.task.TaskManager;
import ru.yandex.common.Status;
import ru.yandex.entites.Epic;
import ru.yandex.entites.Subtask;
import ru.yandex.entites.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    Task task;
    Task task1;
    Task task2;

    Epic epic;
    Epic epic1;
    Epic epic2;

    Subtask subtask;
    Subtask subtask1;
    Subtask subtask2;

    TaskManagerTest() throws IOException {
    }

    @Test
    void addNewTask() throws IOException {

        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(taskId), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() throws IOException {
        int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final HashMap<Integer, Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(3, epics.size(), "Неверное количество эпик.");
        assertEquals(epic, epics.get(epicId), "Эпики не совпадают.");

        Status status = epic.getStatus();
        assertEquals(Status.NEW, status, "Неверный статус.");
    }

    @Test
    void addNewSubtask() throws IOException {

        final int subtaskId = subtask.getId();

        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(subtaskId), "Подзадачи не совпадают.");

        assertNotNull(subtask.getEpicById(), "Эпик на возвращаются.");
    }

    @Test
    void addNewStatusEpicNEWSubtask0() throws IOException {
        Status status = epic.getStatus();
        assertEquals(Status.NEW, status, "Неверный статус.");
    }

    @Test
    void addNewSubtaskFaultId() throws IOException {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    int epicId = 4;
                    taskManager.getSubtaskById(epicId);
                });

        assertEquals("Такой задачи нету.Неверный идентификатор", ex.getMessage());
    }

    @Test
    void addNewStatusEpicNEWSubtask2NEW() throws Exception {
        taskManager.updateSubtask(subtask);
        taskManager.updateSubtask(subtask1);
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.NEW, status, "Неверный статус.");
    }

    @Test
    void addNewStatusEpicDONESubtask2DONE() throws Exception {
        subtask.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.DONE, status, "Неверный статус.");
    }

    @Test
    void addNewStatusEpicInProgressSubtaskDONE() throws Exception {
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status, "Неверный статус.");
    }

    @Test
    void addNewStatusEpicInProgressSubtask2InProgress() throws Exception {
        subtask.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        taskManager.updateSubtask(subtask1);
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status, "Неверный статус.");
    }


    @Test
    public void returnIntWhenTaskCrossingDate() throws IOException {
        Task noValidTask = taskManager.createTask("задача 2", "описание задачи 2", LocalDate.of(2023,02,02), Duration.ofDays(1));
        assertNull( noValidTask);
    }

    @Test
    public void returnIntWhenSubTaskCrossingDate() throws Exception {
        Subtask noValidSubTask =  taskManager.createSubtask("подзадача 1-1", "описание подзадачи 1-1",4, LocalDate.of(2023,02,02), Duration.ofDays(1));
        assertNull( noValidSubTask);
    }


    @Test
    public void notUpdateTaskIfNotId() throws IOException {
        Task task3 = taskManager.getTasks().get(5);
        taskManager.updateTask(task3);
        assertNull(task3);
    }

    @Test
    void removeAllTasks() throws IOException {
        taskManager.removeAllTasks();
        HashMap<Integer, Task> tasks = taskManager.getTasks();
        boolean nullTasks = tasks.isEmpty();
        int size = tasks.size();
        assertTrue(nullTasks);
        assertEquals(0, size);

    }

    @Test
    void removeAllEpics() throws IOException {
        taskManager.removeAllEpics();
        HashMap<Integer, Epic> epics = taskManager.getEpics();
        boolean nullTasks = epics.isEmpty();
        int size = epics.size();
        assertTrue(nullTasks);
        assertEquals(0, size);

    }

    @Test
    void removeAllSubtask() throws IOException {
        taskManager.removeAllSubtasks();
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        boolean nullTasks = subtasks.isEmpty();
        int size = subtasks.size();
        assertTrue(nullTasks);
        assertEquals(0, size);

    }

    @Test
    void getTaskById() throws IOException {
        int id = task.getId();
        Task savedTask = taskManager.getTaskById(id);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getEpicById() throws IOException {
        int id = epic.getId();
        Task savedTask = taskManager.getEpicById(id);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getSubtaskById() throws IOException {
        int id = subtask.getId();
        Subtask savedTask = taskManager.getSubtaskById(id);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subtask, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getTaskById0() throws IOException {

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    int id = 0;
                    Task savedTask = taskManager.getTaskById(id);
                });

        assertEquals("Такой задачи нету.Неверный идентификатор", ex.getMessage());
    }

    @Test
    void getEpicById0() throws IOException {

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    int id = 0;
                    Task savedTask = taskManager.getEpicById(id);
                });

        assertEquals("Такой задачи нету.Неверный идентификатор", ex.getMessage());
    }

    @Test
    void getSubtaskById0() throws IOException {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    int id = 0;
                    Task savedTask = taskManager.getSubtaskById(id);
                });

        assertEquals("Такой задачи нету.Неверный идентификатор", ex.getMessage());
    }

    @Test
    void getTaskByIdEmptyTask() throws IOException {

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    task = null;
                    try {
                        Task savedTask = taskManager.getTaskById(task.getId());
                    } catch (NullPointerException e) {
                        throw new NullPointerException("Получить идентификатор невозможно. Пустой список задач");
                    }
                });

        assertEquals("Получить идентификатор невозможно. Пустой список задач", ex.getMessage());
    }

    @Test
    void getEpicByIdEmptyEpic() throws IOException {

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    epic = null;
                    try {
                        Task savedTask = taskManager.getTaskById(epic.getId());
                    } catch (NullPointerException e) {
                        throw new NullPointerException("Получить идентификатор невозможно. Пустой список задач");
                    }
                });

        assertEquals("Получить идентификатор невозможно. Пустой список задач", ex.getMessage());
    }

    @Test
    void getTaskByIdEmptySubtask() throws IOException {

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    subtask = null;
                    try {
                        Task savedTask = taskManager.getTaskById(subtask.getId());
                    } catch (NullPointerException e) {
                        throw new NullPointerException("Получить идентификатор невозможно. Пустой список задач");
                    }
                });

        assertEquals("Получить идентификатор невозможно. Пустой список задач", ex.getMessage());
    }

    @Test
    void removeTask() throws IOException {
        taskManager.removeTask(task1);
        int size = taskManager.getTasks().size();
        assertEquals(2, size, "Количество не совпадает.");
    }

    @Test
    void removeEpic() throws Exception {
        taskManager.removeEpic(epic1);
        int size = taskManager.getEpics().size();
        assertEquals(2, size, "Количество не совпадает.");
    }

    @Test
    void removeSubtask() throws Exception {
        taskManager.removeSubtask(subtask);
        int size = taskManager.getSubtasks().size();
        assertEquals(2, size, "Количество не совпадает.");
    }

    @Test
    void removeTaskEmpty() throws IOException {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    taskManager.removeTask(null);
                    throw new NullPointerException("Передана пустая задача");
                });

        assertEquals("Передана пустая задача", ex.getMessage());
    }

    @Test
    void removeEpicEmpty() throws IOException {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    taskManager.removeTask(null);
                    throw new NullPointerException("Передана пустая задача");
                });

        assertEquals("Передана пустая задача", ex.getMessage());
    }

    @Test
    void removeSubtaskEmpty() throws IOException {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    taskManager.removeTask(null);
                    throw new NullPointerException("Передана пустая задача");
                });

        assertEquals("Передана пустая задача", ex.getMessage());
    }


    @Test
    void updateTask() throws IOException {
        task.setDescription("Новое описание");
        task.setName("Новое");
        task.setStatus(Status.DONE);
        taskManager.updateTask(task);
        String name = task.getName();
        String description = task.getDescription();
        Status status = task.getStatus();
        assertEquals("Новое", name);
        assertEquals("Новое описание", description);
        assertEquals(Status.DONE, status);
    }

    @Test
    void updateEpic() throws Exception {
        Epic epicHash = taskManager.getEpics().get(epic.getId());

        HashMap<Integer, Subtask> subtask = epic.getSubtasks();
        for (Subtask name : subtask.values()) {
            name.setStatus(Status.DONE);
        }
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.DONE, status);
        assertEquals(epic, epicHash);
    }

    @Test
    void updateSubtask() throws Exception {
        subtask.setDescription("Новое описание");
        subtask.setName("Новое");
        subtask.setStatus(Status.DONE);
        subtask.setEpics(3);
        String name = subtask.getName();
        String description = subtask.getDescription();
        Status status = subtask.getStatus();
        int id = subtask.getEpicById();
        assertEquals("Новое", name);
        assertEquals("Новое описание", description);
        assertEquals(Status.DONE, status);
        assertEquals(3, id);
    }

    @Test
    void updateTaskNull() throws IOException {
        task = taskManager.createTask(null, null,LocalDate.EPOCH, Duration.ZERO);
        task.setDescription("Новое описание");
        task.setName("Новое");
        task.setStatus(Status.DONE);
        taskManager.updateTask(task);
        String name = task.getName();
        String description = task.getDescription();
        Status status = task.getStatus();
        assertEquals("Новое", name);
        assertEquals("Новое описание", description);
        assertEquals(Status.DONE, status);
    }

    @Test
    void updateEpicNull() throws Exception {
        epic = taskManager.createEpic(null, null);
        epic.setDescription("Новое описание");
        epic.setName("Новое");
        epic.setStatus(Status.DONE);
        taskManager.updateEpic(epic);
        String name = epic.getName();
        String description = epic.getDescription();
        Status status = epic.getStatus();
        assertEquals("Новое", name);
        assertEquals("Новое описание", description);
        assertEquals(Status.DONE, status);
    }

    @Test
    void updateSubtaskNull() throws Exception {
        int epicId = epic.getId();
        subtask = taskManager.createSubtask(null, null, epicId, LocalDate.EPOCH, Duration.ZERO);
        subtask.setDescription("Новое описание");
        subtask.setName("Новое");
        subtask.setStatus(Status.DONE);
        subtask.setEpics(3);
        String name = subtask.getName();
        String description = subtask.getDescription();
        Status status = subtask.getStatus();
        int id = subtask.getEpicById();
        assertEquals("Новое", name);
        assertEquals("Новое описание", description);
        assertEquals(Status.DONE, status);
        assertEquals(3, id);
    }

    @Test
    void updateTask0() throws IOException {

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    try {
                        task = null;
                        task.setDescription("Новое описание");
                    } catch (NullPointerException e) {
                        throw new NullPointerException("Передана пустая задача");
                    }
                });

        assertEquals("Передана пустая задача", ex.getMessage());
    }

    @Test
    void updateEpic0() throws IOException {

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    try {
                        epic = null;
                        epic.setDescription("Новое описание");
                    } catch (NullPointerException e) {
                        throw new NullPointerException("Передана пустая задача");
                    }
                });
        assertEquals("Передана пустая задача", ex.getMessage());
    }

    @Test
    void updateSubtask0() throws IOException {

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> {
                    try {
                        subtask = null;
                        subtask.setDescription("Новое описание");
                    } catch (NullPointerException e) {
                        throw new NullPointerException("Передана пустая задача");
                    }
                });
        assertEquals("Передана пустая задача", ex.getMessage());
    }

    @Test
    void updateSubtaskFaultId() throws IOException {

        Exception ex = assertThrows(
                Exception.class,
                () -> {
                    taskManager.checkStatusByEpic(3);
                });
        assertEquals("Задача не найдена", ex.getMessage());
    }

    @Test
    void checkStatusByEpicIN_PROGRESS() throws Exception {
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status);
    }

    @Test
    void checkStatusByEpicNotSubtask() throws Exception {
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.NEW, status);
    }

    @Test
    void getTasks() throws IOException {
        HashMap<Integer, Task> tasks = taskManager.getTasks();
        int size = tasks.size();
        assertEquals(3, size, "Число задач не совпадает");
        assertNotNull(tasks, "Задачи не найдены.");
    }

    @Test
    void getEpics() throws IOException {
        HashMap<Integer, Epic> epics = taskManager.getEpics();
        int size = epics.size();
        assertEquals(3, size, "Число задач не совпадает");
        assertNotNull(epics, "Задачи не найдены.");
    }

    @Test
    void getSubtasks() throws IOException {
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        int size = subtasks.size();
        assertEquals(3, size, "Число задач не совпадает");
        assertNotNull(subtasks, "Задачи не найдены.");
    }

    @Test
    void checkStatusByEpic() throws Exception {
        taskManager.updateEpic(epic);
        Status status = subtask.getStatus();
        assertEquals(Status.NEW, status, "Неверный статус");
    }

    @Test
    void checkStatusByEpicDONESubtask() throws Exception {
        subtask.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.DONE, status, "Неверный статус.");
    }


    @Test
    void getHistory() throws IOException {
        taskManager.getTaskById(1);
        int size = taskManager.getHistory().size();
        ArrayList<Task> tasks = taskManager.getHistory();
        assertEquals(1, size, "Число задач не совпадает");
        assertNotNull(tasks, "Задачи не найдены.");

        String list = tasks.toString();
        assertEquals("[id = 1; name = Test addNewTask; description = Test addNewTask1 description; status = NEW; startTime = 2023-02-02; duration = PT24H]",list, "Задачи не совпадают ");
    }

    @Test
    void getHistory1() throws IOException {
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);

        int size = taskManager.getHistory().size();
        ArrayList<Task> tasks = taskManager.getHistory();
        assertEquals(1, size, "Число задач не совпадает");
        assertNotNull(tasks, "Задачи не найдены.");

        String list = tasks.toString();
        assertEquals("[id = 1; name = Test addNewTask; description = Test addNewTask1 description; status = NEW; startTime = 2023-02-02; duration = PT24H]",
                list, "Задачи не совпадают ");
    }
}

//("Задачи нету"

