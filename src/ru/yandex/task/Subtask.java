package ru.yandex.task;
import ru.yandex.task.Epic;
import ru.yandex.task.Task;

import java.util.ArrayList;

public class Subtask extends Task {

    private ArrayList<Epic> epics = new ArrayList<>();

    public Subtask(int id, String name,String description,Epic epic) {
        super( id, name, description);


        this.epics.add(epic.getId(), epic);

        epic.addSubtasks(this);
    }

    public Epic getEpicById(Integer id) {
        return this.epics.get(id);
    }


    public void setEpics(ArrayList<Epic> epics) {
        this.epics = epics;
    }
}
