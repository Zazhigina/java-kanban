package ru.yandex.manager.task;

import ru.yandex.common.Status;
import ru.yandex.common.TaskTypes;
import ru.yandex.entites.Task;

import java.time.Duration;
import java.time.LocalDate;

public class StringConversion<T> extends Task {
    private T type;
    private Status status;
    private Integer epic;
    private LocalDate startTime;
    private Long duration;


    public StringConversion(int id, String name, String description, T type, Status status, Integer epic, LocalDate startTime, Long duration) {
        super(id, name, description);
        this.type = type;
        this.status = status;
        this.epic = epic;
        this.startTime = startTime;
        this.duration = duration;
    }


    @Override
    public String toString() {

        if (type.equals(TaskTypes.TASK)){
            return id + "," + type + "," + getName() + "," + status + "," + getDescription() + "," + startTime +","+ duration;
        } if (type.equals(TaskTypes.EPIC)) {
            return id + "," + type + "," + getName() + "," + status + "," + getDescription() + "," + startTime + "," + duration;
        }if (type.equals(TaskTypes.SUBTASK)){
            return id + "," + type + "," + getName() + "," + status + "," + getDescription() + "," + startTime + "," + duration + ","+ epic;
        }
        return null;
    }



}
