package ru.yandex.entites;

import ru.yandex.common.TaskTypes;

public class Subtask extends Task {

    private Integer epics;

    @Override
    public TaskTypes getTaskTypes() {
        return taskTypes;
    }
    public Subtask(int id, String name, String description, Integer IdEpic) {
        super(id, name, description, TaskTypes.SUBTASK);

        this.epics = IdEpic;

    }

    public Integer getEpicById() {
        return this.epics;
    }


    public void setEpics(Integer epics) {
        this.epics = epics;
    }
}
