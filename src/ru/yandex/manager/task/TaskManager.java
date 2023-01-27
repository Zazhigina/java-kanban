package ru.yandex.manager.task;

import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {


    void removeAllTasks() throws IOException;

    void removeAllEpics() throws IOException;

    void removeAllSubtasks() throws IOException;

    Task getTaskById(int id) throws IOException;

    Epic getEpicById(int id) throws IOException;

    Subtask getSubtaskById(int id) throws IOException;

    Task createTask(String name, String description) throws IOException;

    Epic createEpic(String name, String description) throws IOException;

    Subtask createSubtask(String name, String description, Integer IdEpic) throws IOException;

    void removeTask(Task task) throws IOException;

    void removeEpic(Epic epic) throws IOException;

    void removeSubtask(Subtask subtask) throws IOException;

    void updateTask(Task task) throws IOException;

    void updateEpic(Epic epic) throws IOException;

    void updateSubtask(Subtask subtask) throws IOException;

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, Subtask> getSubtasks();

    ArrayList<Task> getHistory();

}


