package ru.yandex.task;

import ru.yandex.manager.task.TaskTypes;

import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private TaskTypes taskTypes = TaskTypes.EPIC;

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Subtask getSubtasksById(Integer id) {
        return subtasks.get(id);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return this.subtasks;
    }

    public void addSubtasks(Subtask subtask) {
        this.subtasks.put(subtask.getId(), subtask);
    }
    @Override
    public TaskTypes getTaskTypes() {
        return taskTypes;
    }

}
