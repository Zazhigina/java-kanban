package ru.yandex.manager.Task;

import ru.yandex.manager.History.InMemoryHistoryManager;
import ru.yandex.task.*;

import java.util.HashMap;

import ru.yandex.status.Status;


public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();

    private HashMap<Integer, Epic> epics = new HashMap<>();

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private Integer idIndex = 1;

    public void save() {

        idIndex = idIndex + 1;

    }

    @Override
    public void removeAllTasks() {
        this.tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        this.epics.clear();
    }

    @Override
    public void removeAllSubtasks() {
        this.subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        inMemoryHistoryManager.checkListHistory();
        inMemoryHistoryManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        inMemoryHistoryManager.checkListHistory();
        inMemoryHistoryManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        inMemoryHistoryManager.checkListHistory();
        inMemoryHistoryManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Task createTask(String name, String description) {

        Task task = new Task(idIndex, name, description);
        save();
        this.tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(String name, String description) {

        Epic epic = new Epic(idIndex, name, description);
        save();
        this.epics.put(epic.getId(), epic);
        return epic;

    }

    @Override
    public Subtask createSubtask(String name, String description, Epic epic) {

        Subtask subtask = new Subtask(idIndex, name, description, epic);
        save();
        this.subtasks.put(subtask.getId(), subtask);
        return subtask;
    }

    @Override
    public void removeTask(Task task) {
        this.tasks.remove(task.getId());
    }

    @Override
    public void removeEpic(Epic epic) {
        this.epics.remove(epic.getId());
    }

    @Override
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

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
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

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

}
