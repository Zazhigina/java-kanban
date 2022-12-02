package ru.yandex.manager;

import ru.yandex.task.*;

import java.util.HashMap;

import ru.yandex.status.Status;


public class Manager {

    private HashMap<Integer, Task> tasks = new HashMap<>();

    private HashMap<Integer, Epic> epics = new HashMap<>();

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private Integer idIndex = 1;

    public void save() {

        idIndex = idIndex + 1;

    }

    public void removeAllTasks() {
        this.tasks.clear();
    }

    public void removeAllEpics() {
        this.epics.clear();
    }

    public void removeAllSubtasks() {
        this.subtasks.clear();
    }


    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }


    public Task createTask(String name, String description) {

        Task task = new Task(idIndex, name, description);
        save();
        this.tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(String name, String description) {

        Epic epic = new Epic(idIndex, name, description);
        save();
        this.epics.put(epic.getId(), epic);
        return epic;

    }

    public Subtask createSubtask(String name, String description, Epic epic) {

        Subtask subtask = new Subtask(idIndex, name, description, epic);
        this.subtasks.put(subtask.getId(), subtask);
        return subtask;
    }


    public void removeTask(Task task) {
        this.tasks.remove(task.getId());
    }

    public void removeEpic(Epic epic) {
        this.epics.remove(epic.getId());
    }

    public void removeSubtask(Subtask subtask) {

        for (Epic value : this.epics.values()) {

            value.getSubtasks().remove(subtask.getId());
        }

    }


    public void getListSubtaskByEpic(int id) {

        Epic epic = epics.get(id);
        epic.getSubtasksById(id);

        System.out.println(epic.toString());
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void checkStatusByEpic(int id) {
        int doneTask = 0;
        Epic epic = epics.get(id);
        if (epic != null) {
            HashMap<Integer, Subtask> subtask = epic.getSubtasks();
            for (Subtask name : subtask.values()) {
                Status status = name.getStatus();
                if (status == Status.DONE) {
                    doneTask = doneTask + 1;
                }
                if (doneTask == subtask.size()) {
                    epic.setStatus(Status.DONE);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        }

    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}
