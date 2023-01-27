package ru.yandex.manager.task;

import ru.yandex.status.Status;
import ru.yandex.task.Epic;
import ru.yandex.task.Task;

public class StringConversion<T> extends Task {
    private T type;
    private Status status;
    private Integer epic;


    public StringConversion(int id, String name, String description, T type, Status status, Integer epic) {
        super(id, name, description);
        this.type = type;
        this.status = status;
        this.epic = epic;
    }


    @Override
    public String toString() {

        if (type.equals(TaskTypes.TASK)){
            return id + "," + type + "," + getName() + "," + status + "," + getDescription() + ",";
        } if (type.equals(TaskTypes.EPIC)) {
            return id + "," + type + "," + getName() + "," + status + "," + getDescription() + ",";
        }if (type.equals(TaskTypes.SUBTASK)){
            return id + "," + type + "," + getName() + "," + status + "," + getDescription() + ","+ epic;
        }
        return null;
    }



}
