package ru.yandex.task;

import ru.yandex.manager.task.TaskTypes;
import ru.yandex.status.Status;

public class Task {
    protected Integer id;
    private String name;
    private String description;
    private Status status = Status.NEW;

    private TaskTypes taskTypes = TaskTypes.TASK;

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskTypes getTaskTypes() {
        return taskTypes;
    }

    public String toString() {
        return "id = " + id + "; name = " + name + "; description = " + description + "; status = " + status;
    }

}
