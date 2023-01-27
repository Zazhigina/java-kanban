package ru.yandex.manager.task;

import ru.yandex.manager.Managers;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.task.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ru.yandex.status.Status;


public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected Integer idIndex = 1;


    private HistoryManager historyManager = Managers.getDefaultHistory();


    public void saveIndex() throws IOException {
        idIndex = idIndex + 1;
    }


    @Override
    public void removeAllTasks() throws IOException {
        this.tasks.clear();
        for (Task removeTask : tasks.values()) {
            historyManager.remove(removeTask.getId());
        }
    }

    @Override
    public void removeAllEpics() throws IOException {
        this.epics.clear();
        for (Epic removeEpic : epics.values()) {
            historyManager.remove(removeEpic.getId());
        }
    }

    @Override
    public void removeAllSubtasks() throws IOException {
        this.subtasks.clear();
        for (Subtask removeSubtask : subtasks.values()) {
            historyManager.remove(removeSubtask.getId());
        }
    }

    @Override
    public Task getTaskById(int id) throws IOException {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) throws IOException {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) throws IOException {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Task createTask(String name, String description) throws IOException {

        Task task = new Task(idIndex, name, description);
        saveIndex();
        this.tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(String name, String description) throws IOException {

        Epic epic = new Epic(idIndex, name, description);
        saveIndex();
        this.epics.put(epic.getId(), epic);
        return epic;

    }

    @Override
    public Subtask createSubtask(String name, String description, Integer IdEpic) throws IOException {

        Subtask subtask = new Subtask(idIndex, name, description, IdEpic);
        saveIndex();
        getEpicById(IdEpic).addSubtasks(subtask);
        this.subtasks.put(subtask.getId(), subtask);
        return subtask;
    }

    @Override
    public void removeTask(Task task) throws IOException {
        this.tasks.remove(task.getId());
        historyManager.remove(task.getId());
    }

    @Override
    public void removeEpic(Epic epic) throws IOException {
        this.epics.remove(epic.getId());
        historyManager.remove(epic.getId());
        for (Subtask subtask : epic.getSubtasks().values()) {
            removeSubtask(subtask);
        }
    }

    @Override
    public void removeSubtask(Subtask subtask) throws IOException {

        for (Epic value : this.epics.values()) {

            value.getSubtasks().remove(subtask.getId());
        }
        this.subtasks.remove(subtask.getId());
        historyManager.remove(subtask.getId());


    }


    public void getListSubtaskByEpic(int id) throws IOException {

        Epic epic = epics.get(id);
        epic.getSubtasksById(id);

        System.out.println(epic.toString());
    }

    @Override
    public void updateTask(Task task) throws IOException {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) throws IOException {
        epics.put(epic.getId(), epic);
        checkStatusByEpic(epic.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException {
        subtasks.put(subtask.getId(), subtask);
        checkStatusByEpic(subtask.getId());
    }

    public void checkStatusByEpic(int id) throws IOException {
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

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected HistoryManager getHistoryManager() {
        return historyManager;
    }
}
