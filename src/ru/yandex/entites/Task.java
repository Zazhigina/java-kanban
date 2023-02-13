package ru.yandex.entites;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

import ru.yandex.common.TaskTypes;
import ru.yandex.common.Status;

public class Task {
    protected Integer id;
    private String name;
    private String description;
    private Status status = Status.NEW;

    protected LocalDate startTime;
    protected Long duration;
    protected TaskTypes taskTypes;
    protected LocalDate endTime;

    public Task(int id, String name, String description, TaskTypes taskTypes) {
        this.id = id;
        this.name = name;
        this.taskTypes = taskTypes;
        this.description = description;

    }
    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.taskTypes = TaskTypes.TASK;
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

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String toString() {
        return "id = " + id + "; name = " + name + "; description = " + description + "; status = " + status +
                "; startTime = " + startTime + "; duration = " + duration;
    }

    public LocalDate getEndTime() {
        return this.endTime = this.startTime.plusDays(this.duration);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(name, task.name) && taskTypes == task.taskTypes && status == task.status && Objects.equals(description, task.description) && Objects.equals(startTime, task.startTime);
    }

}
