package ru.yandex.manager.Task;

import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;

import java.util.HashMap;

public interface TaskManager {


    public void removeAllTasks();

    public void removeAllEpics();

    public void removeAllSubtasks();


    public Task getTaskById(int id);

    public Epic getEpicById(int id);

    public Subtask getSubtaskById(int id);

    Task createTask(String name, String description);

    Epic createEpic(String name, String description);

    Subtask createSubtask(String name, String description, Epic epic);

    void removeTask(Task task);

    void removeEpic(Epic epic);

    void removeSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    public HashMap<Integer, Task> getTasks();

    public HashMap<Integer, Epic> getEpics();

    public HashMap<Integer, Subtask> getSubtasks();
}


