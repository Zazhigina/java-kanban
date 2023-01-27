package ru.yandex.manager.task;

import ru.yandex.Main;
import ru.yandex.ManagerSaveException;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.status.Status;
import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private static File file;

    private HashMap<Integer, Task> allTasks = new HashMap<>();



    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() throws IOException {
        try {
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


        for (Task task : getTasks().values()) {
            writeTask(task, bufferedWriter, null);
        }
        for (Epic epic : getEpics().values()) {
            writeTask(epic, bufferedWriter, null);
        }
        for (Subtask subtask : getSubtasks().values()) {
            writeTask(subtask, bufferedWriter, subtask.getEpicById());
        }

        bufferedWriter.write(" \n" + historyToString(this.getHistoryManager()));
        bufferedWriter.close();
    } catch (IOException exception){
            throw new ManagerSaveException ("Сохранение не произошло");
        }
    }

    public String toString(Task task, Integer epic) {

        StringConversion sc = new StringConversion<>(task.getId(), task.getName(), task.getDescription(), task.getTaskTypes(), task.getStatus(), epic);
        return String.valueOf(sc);
    }

    public void writeTask(Task task, BufferedWriter bufferedWriter, Integer epic) throws IOException {
        bufferedWriter.write(toString(task, epic) + " \n");
    }

    static String historyToString(HistoryManager manager) {
        String idHistory = "";

        ArrayList<Task> tasks = manager.getHistory();
        for (Task id : tasks) {
            idHistory = idHistory + String.valueOf(id.getId()) + ",";

        }
        return idHistory;
    }

    @Override
    public void removeAllTasks() throws IOException {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() throws IOException {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() throws IOException {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) throws IOException {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) throws IOException {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) throws IOException {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }


    @Override
    public Task createTask(String name, String description) throws IOException {
        Task task = super.createTask(name, description);
        save();
        return task;
    }

    @Override
    public Epic createEpic(String name, String description) throws IOException {
        Epic epic = super.createEpic(name, description);
        save();

        return epic;
    }

    @Override
    public Subtask createSubtask(String name, String description, Integer IdEpic) throws IOException {
        Subtask subtask = super.createSubtask(name, description, IdEpic);
        save();
        return subtask;
    }

    @Override
    public void removeTask(Task task) throws IOException {
        super.removeTask(task);
        save();
    }

    @Override
    public void removeEpic(Epic epic) throws IOException {
        super.removeEpic(epic);
        save();
    }

    @Override
    public void removeSubtask(Subtask subtask) throws IOException {
        super.removeSubtask(subtask);
        save();
    }

    @Override
    public void getListSubtaskByEpic(int id) throws IOException {
        super.getListSubtaskByEpic(id);
        save();
    }

    @Override
    public void updateTask(Task task) throws IOException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws IOException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void checkStatusByEpic(int id) throws IOException {
        super.checkStatusByEpic(id);
        save();
    }


    @Override
    public ArrayList<Task> getHistory() {
        return super.getHistory();
    }


    private static String readFileContentsOrNull(File file) {
        try {
            return Files.readString(Path.of(String.valueOf(file)));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл.");
            System.out.println("Возможно, файл не находится в нужной директории");
            return null;
        }
    }

    public static Task fromString(String line) throws IOException {

        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskTypes taskTypes = TaskTypes.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        Integer epic = null;
        if (taskTypes == TaskTypes.SUBTASK) {
            String epics = parts[5].trim();
            epic = Integer.parseInt(epics);
        }
        switch (taskTypes) {
            case TASK: {
                Task newTask = new Task(id, name, description);
                return newTask;
            }
            case EPIC: {
                Epic newEpic = new Epic(id, name, description);
                return newEpic;
            }
            case SUBTASK: {
                Subtask newSubtask = new Subtask(id, name, description, epic);
                return newSubtask;
            }
        }
        return null;
    }


    static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        String content = readFileContentsOrNull(file);
        String[] lines = content.split("\r?\n"); // массив строк
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            while (!line.isBlank()) {
                Task task = fromString(line);
                if (task.getTaskTypes().equals(TaskTypes.TASK)) {
                    fileBackedTasksManager.tasks.put(task.getId(), task);
                    fileBackedTasksManager.allTasks.put(task.getId(), task);
                    fileBackedTasksManager.maxId(task.getId());
                    line = lines[i = i + 1];
                }
                if (task.getTaskTypes().equals(TaskTypes.EPIC)) {
                    fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                    fileBackedTasksManager.maxId(task.getId());
                    fileBackedTasksManager.allTasks.put(task.getId(), (Epic) task);
                    line = lines[i = i + 1];
                }
                if (task.getTaskTypes().equals(TaskTypes.SUBTASK)) {
                    fileBackedTasksManager.subtasks.put(task.getId(), (Subtask) task);
                    fileBackedTasksManager.allTasks.put(task.getId(), (Subtask) task);
                    fileBackedTasksManager.maxId(task.getId());
                    line = lines[i = i + 1];
                }

            }

            if (line.isBlank()) {
                line = lines[i = i + 1];
            }
            String[] parts = line.split(",");
            for (String part : parts) {
                List<Integer> idNew = historyFromString(part);
                for (Integer list : idNew) {
                    for (Integer key : fileBackedTasksManager.allTasks.keySet()) {
                        if (key.equals(list))
                            fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.allTasks.get(key));// вызов метода восстановления historyFromString
                    }
                }
            }
        }
        return fileBackedTasksManager;
    }

    void maxId (Integer id){
        if (idIndex < id){
            idIndex = id;
        }
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();
        Integer id = Integer.valueOf(value);
        list.add(id);
        return list;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("resources\\task_history.scv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);


        Task task = fileBackedTasksManager.createTask("Сходить в магазин", "Купить все по списку");
        Task task1 = fileBackedTasksManager.createTask("Доделать задачу", "Исправить ошибки по учебе");

        Epic epic = fileBackedTasksManager.createEpic("зачеты", "сдать долги");
        Subtask subtask = fileBackedTasksManager.createSubtask("матан", "долг по матрице", 3);
        Subtask subtask1 = fileBackedTasksManager.createSubtask("химия", "зачет по полимерам", 3);
        Subtask subtask2 = fileBackedTasksManager.createSubtask("Физика", "сдать курсач", 3);

        Epic epic1 = fileBackedTasksManager.createEpic("Отпуск", "собрать вещи");

        System.out.println(fileBackedTasksManager.getTaskById(2));
        System.out.println(fileBackedTasksManager.getEpicById(3));
        System.out.println(fileBackedTasksManager.getSubtaskById(4));
        System.out.println(fileBackedTasksManager.getEpicById(7));
        System.out.println(fileBackedTasksManager.getSubtaskById(5));
        System.out.println("История вызовов " + fileBackedTasksManager.getHistory());

        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(file);

        HashMap<Integer, Task> tasks = fileBackedTasksManager2.getTasks();
        HashMap<Integer, Epic> epics = fileBackedTasksManager2.getEpics();
        HashMap<Integer, Subtask> subtasks = fileBackedTasksManager2.getSubtasks();
        for (Task value : tasks.values()) {
            System.out.println(value.toString());
        }

        for (Epic value : epics.values()) {
            System.out.println(value.toString());
            for (Subtask subtask3 : value.getSubtasks().values()) {
                System.out.println(subtask3.toString());
            }
        }
        for (Subtask sub : subtasks.values()) {
            System.out.println(sub.toString());
        }

        System.out.println("История вызовов " + fileBackedTasksManager2.getHistory().toString());
    }
}

