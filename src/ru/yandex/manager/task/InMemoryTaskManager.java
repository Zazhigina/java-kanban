package ru.yandex.manager.task;

import ru.yandex.manager.Managers;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.entites.*;

import java.util.List;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import ru.yandex.common.Status;


public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected Integer idIndex = 1;
    private HistoryManager historyManager = Managers.getDefaultHistory();

    private final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId);
    private final Set<Task> prioritizedTasks = new TreeSet<>(comparator);


    public void saveIndex() throws IOException {
        idIndex = idIndex + 1;
    }

    public Integer getIndex() {
        return idIndex;
    }

    @Override
    public void removeAllTasks() throws IOException {
        this.tasks.clear();
        for (Task removeTask : tasks.values()) {
            historyManager.remove(removeTask.getId());
            prioritizedTasks.remove(removeTask);
        }
    }

    @Override
    public void removeAllEpics() throws IOException {
        this.epics.clear();
        for (Epic removeEpic : epics.values()) {
            historyManager.remove(removeEpic.getId());
            HashMap<Integer, Subtask> subtask = removeEpic.getSubtasks();
            for (int id : subtask.keySet()) {
                prioritizedTasks.remove(subtask.get(id));
                historyManager.remove(id);
            }
        }
    }

    @Override
    public void removeAllSubtasks() throws IOException {
        this.subtasks.clear();
        for (Subtask removeSubtask : subtasks.values()) {
            historyManager.remove(removeSubtask.getId());
            prioritizedTasks.remove(removeSubtask);
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
    public Task createTask(String name, String description, LocalDate startTime, Duration duration) throws IOException {
        Task task = new Task(idIndex, name, description);
        task.setStartTime(startTime);
        task.setDuration(duration);
        if (checkTaskCrossingRange(task)){
            saveIndex();
            addPrioritizedTask(task);
            this.tasks.put(task.getId(), task);
            return task;
        }
        System.out.println("В заданное время назначено выполнение другой под/задачи.");
        return null;
    }

    @Override
    public Epic createEpic(String name, String description) throws IOException {

        Epic epic = new Epic(idIndex, name, description);
        saveIndex();
        this.epics.put(epic.getId(), epic);
        return epic;

    }

    @Override
    public Subtask createSubtask(String name, String description, Integer IdEpic, LocalDate startTime, Duration
            duration) throws Exception {
        Subtask subtask = new Subtask(idIndex, name, description, IdEpic);
        subtask.setStartTime(startTime);
        subtask.setDuration(duration);
        subtask.getEndTime();
        saveIndex();
        if (checkTaskCrossingRange(subtask)) {
            this.epics.get(IdEpic).addSubtasks(subtask);
            addPrioritizedTask(subtask);
            this.epics.get(IdEpic).getEndTimeEpic(startTime, duration);
            this.subtasks.put(subtask.getId(), subtask);
            updateEpic(this.epics.get(IdEpic));
            return subtask;
        }
        System.out.println("В заданное время назначено выполнение другой под/задачи.");
        return null;
    }

    @Override
    public void removeTask(Task task) throws IOException {
        if (task != null) {
            this.tasks.remove(task.getId());
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        }

    }

    @Override
    public void removeEpic(Epic epic) throws IOException {
        if (epic != null) {
            this.epics.remove(epic.getId());
            historyManager.remove(epic.getId());
            prioritizedTasks.remove(epic);
            for (Subtask subtask : epic.getSubtasks().values()) {
                removeSubtask(subtask);
                prioritizedTasks.remove(subtask);
            }
        }

    }

    @Override
    public void removeSubtask(Subtask subtask) throws IOException {
        if (subtask != null) {
            for (Epic value : this.epics.values()) {
                value.getSubtasks().remove(subtask.getId());
            }
            this.subtasks.remove(subtask.getId());
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);

        }
    }


    @Override
    public void updateTask(Task task) throws IOException {
        if (task != null) {
            if (checkTaskCrossingRange(task)) {
                int id = task.getId();

                if (tasks.containsKey(id)) {
                    tasks.put(id, task);
                    addPrioritizedTask(task);
                } else {
                    System.out.println("Задача не найдена.");
                }
            } else {
                System.out.println("В заданное время назначено выполнение другой под/задачи.");
            }
        } else {
            System.out.println("Ошибка обновления задачи.");
        }
    }


    @Override
    public void updateEpic(Epic epic) throws Exception {
        if (epic != null) {
            int id = epic.getId();
            if (epics.containsKey(id)) {
                epics.put(id, epic);
                checkStatusByEpic(id);
                checkTimeByEpic(id);
            } else {
                System.out.println("Эпик не найден.");
            }
        } else {
            System.out.println("Ошибка обновления эпика.");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) throws Exception {
        if (subtask != null) {
            int id = subtask.getId();

            if (checkTaskCrossingRange(subtask)) {
                if (subtasks.containsKey(id)) {
                    subtasks.put(id, subtask);
                    addPrioritizedTask(subtask);

                    Epic epic = epics.get(subtask.getEpicById());
                    checkStatusByEpic(epic.getId());
                    checkTimeByEpic(epic.getId());
                } else {
                    System.out.println("Подзадача не найдена.");
                }
            } else {
                System.out.println("В заданное время назначено выполнение другой под/задачи.");
            }
        } else {
            System.out.println("Ошибка обновления подзадача.");
        }
    }

    public void checkStatusByEpic(int id) throws Exception {
        int doneTask = 0;
        int newTask = 0;
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new Exception("Задача не найдена");
        } else {
            HashMap<Integer, Subtask> subtask = epic.getSubtasks();
            for (Subtask name : subtask.values()) {
                Status status = name.getStatus();
                if (status == Status.DONE) {
                    doneTask = doneTask + 1;
                }
                if (status == Status.NEW) {
                    newTask = newTask + 1;
                }
                if (doneTask == subtask.size()) {
                    epic.setStatus(Status.DONE);
                    return;
                }
                if (newTask == subtask.size()) {
                    epic.setStatus(Status.NEW);
                    return;
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }

    public void checkTimeByEpic(int id) throws Exception {
        LocalDate endTime = LocalDate.MAX;
        LocalDate startTime = LocalDate.MAX;
        Duration duration = Duration.ZERO;
        LocalDate newTime;
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new Exception("Задача не найдена");
        } else {
            HashMap<Integer, Subtask> subtasks1 = epic.getSubtasks();
            for (Subtask subtask : subtasks1.values()) {
                newTime = subtask.getStartTime();
                if (newTime.isBefore(startTime)) {
                    startTime = newTime;
                    epic.setStartTime(startTime);
                }
                duration = subtask.getDuration().plus(duration);
                epic.setDuration(duration);
            }
        }
        endTime = epic.getEndTimeEpic(startTime, duration);
        epic.setEndTime(endTime);
    }

    public void addPrioritizedTask(Task task) {
        int id = task.getId();
        this.prioritizedTasks.removeIf(prioritizeTask -> prioritizeTask.getId() == id);
        this.prioritizedTasks.add(task);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().collect(Collectors.toList());
    }

    private boolean checkTaskCrossingRange(Task checkTask) {
        if (checkTask.getStartTime() != null) {
            int checkId = checkTask.getId();
            List<Task> tasksList = getPrioritizedTasks();

            LocalDate checkStartTime = checkTask.getStartTime();
            LocalDate checkEndTime = checkTask.getEndTime();

            for (Task task : tasksList) {
                if (checkId != task.getId()) {
                    if (task.getStartTime() != null) {
                        LocalDate startTime = task.getStartTime();
                        LocalDate endTime = task.getEndTime();

                        if ((checkEndTime.isAfter(startTime)) && (checkStartTime.isBefore(endTime))) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
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
