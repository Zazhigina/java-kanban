package ru.yandex.task;

public class Subtask extends Task {

    private Integer epics ;

    public Subtask(int id, String name,String description,Epic epic) {
        super( id, name, description);

        this.epics =  epic.getId();

        epic.addSubtasks(this);
    }

    public Integer getEpicById() {
        return this.epics;
    }


    public void setEpics(Integer epics) {
        this.epics = epics;
    }
}
