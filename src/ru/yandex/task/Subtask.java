package ru.yandex.task;

import ru.yandex.manager.task.TaskTypes;

public class Subtask extends Task {

    private Integer epics ;

    @Override
    public TaskTypes getTaskTypes() {
        return taskTypes;
    }

    private TaskTypes taskTypes = TaskTypes.SUBTASK;

    public Subtask(int id, String name,String description,Integer IdEpic) {
        super( id, name, description);

        this.epics =  IdEpic;

    }

    public Integer getEpicById() {
        return this.epics;
    }


    public void setEpics(Integer epics) {
        this.epics = epics;
    }
}
