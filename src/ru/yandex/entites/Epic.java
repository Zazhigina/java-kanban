package ru.yandex.entites;

import ru.yandex.common.TaskTypes;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private LocalDate endTime = LocalDate.ofEpochDay(0);
    private TaskTypes taskTypes = TaskTypes.EPIC;
    private LocalDate startTime;
    private  Duration duration;
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

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }
    public LocalDate getEndTimeEpic(LocalDate startTime,Duration duration) {
         LocalDate newEndTime = startTime.plusDays(duration.toDays());
         if (newEndTime.isAfter(endTime)){
             return endTime=newEndTime;
         }
          return endTime;
    }

}
