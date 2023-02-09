package ru.yandex.manager.task;

import ru.yandex.entites.Epic;
import ru.yandex.entites.Subtask;
import ru.yandex.entites.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {


    void removeAllTasks() throws IOException;

    void removeAllEpics() throws IOException;

    void removeAllSubtasks() throws IOException;

    Task getTaskById(int id) throws IOException;

    Epic getEpicById(int id) throws IOException;

    void checkStatusByEpic(int id) throws Exception;

    Subtask getSubtaskById(int id) throws IOException;

    Task createTask(String name, String description, LocalDate startTime, Duration duration) throws IOException;

    Epic createEpic(String name, String description) throws IOException;

    Subtask createSubtask(String name, String description, Integer IdEpic, LocalDate startTime, Duration duration) throws Exception;

    void removeTask(Task task) throws IOException;

    void removeEpic(Epic epic) throws Exception;

    void removeSubtask(Subtask subtask) throws Exception;

    void updateTask(Task task) throws IOException;

    void updateEpic(Epic epic) throws Exception;

    void updateSubtask(Subtask subtask) throws Exception;

    List<Task> getPrioritizedTasks();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, Subtask> getSubtasks();

    ArrayList<Task> getHistory();

}


