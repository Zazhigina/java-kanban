package ru.yandex.entites;
import ru.yandex.common.TaskTypes;
import java.time.LocalDate;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public Epic(int id, String name, String description) {
        super(id, name, description, TaskTypes.EPIC);
        endTime = LocalDate.ofEpochDay(0);
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
    public LocalDate getEndTimeEpic(LocalDate startTime,Long duration) {
         LocalDate newEndTime = startTime.plusDays(duration);
         if (newEndTime.isAfter(endTime)){
             return endTime=newEndTime;
         }
          return endTime;
    }

}
