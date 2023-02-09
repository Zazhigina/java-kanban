package ru.yandex.manager.task;

import ru.yandex.exceptions.ManagerSaveException;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.common.Status;
import ru.yandex.common.TaskTypes;
import ru.yandex.entites.Epic;
import ru.yandex.entites.Subtask;
import ru.yandex.entites.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static ru.yandex.common.TaskTypes.*;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private static File file;


    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

            for (Task task : getTasks().values()) {
                bufferedWriter.write(toString(task, null));
                bufferedWriter.newLine();
            }
            for (Epic epic : getEpics().values()) {
                bufferedWriter.write(toString(epic, null));
                bufferedWriter.newLine();
            }
            for (Subtask subtask : getSubtasks().values()) {
                bufferedWriter.write(toString(subtask, subtask.getEpicById()));
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.write(historyToString(this.getHistoryManager()));
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new ManagerSaveException("Сохранение не произошло");
        }
    }


    public String toString(Task task, Integer epic) {

        StringConversion sc = new StringConversion<>(task.getId(), task.getName(), task.getDescription(), task.getTaskTypes(), task.getStatus(), epic, task.getStartTime(), task.getDuration());

        return String.valueOf(sc);
    }


    static String historyToString(HistoryManager manager) {
        String idHistory = " ";

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
    public Task createTask(String name, String description, LocalDate startTime, Duration duration) throws IOException {
        Task task = super.createTask(name, description, startTime, duration);
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
    public Subtask createSubtask(String name, String description, Integer IdEpic, LocalDate startTime, Duration duration) throws Exception {
        Subtask subtask = super.createSubtask(name, description, IdEpic, startTime, duration);
        updateEpic(epics.get(IdEpic));
        save();
        return subtask;
    }


    @Override
    public void removeTask(Task task) throws IOException {
        super.removeTask(task);
        save();
    }

    @Override
    public void removeEpic(Epic epic) throws Exception {
        super.removeEpic(epic);
        save();
    }

    @Override
    public void removeSubtask(Subtask subtask) throws Exception {
        super.removeSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) throws IOException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws Exception {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws Exception {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void checkStatusByEpic(int id) throws Exception {
        super.checkStatusByEpic(id);
        save();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return super.getHistory();
    }


    private static ArrayList<String> readFileContentsOrNull(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            ArrayList<String> readFile = new ArrayList<>();
            while (br.ready()) {
                String line = br.readLine();
                readFile.add(line);
            }
            br.close();
            return readFile;
        } catch (IOException e) {
            throw new ManagerSaveException("Сохранение не произошло");
        }
    }


    public static Optional<Task> fromString(String line) throws IOException {

        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskTypes taskTypes = TaskTypes.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        Integer epic = null;
        LocalDate startTime = null;
        Duration duration = null;
        if (taskTypes == SUBTASK) {
            String epics = parts[7].trim();
            epic = Integer.parseInt(epics);
        }
        if (!parts[5].equals("null")) {
            startTime = LocalDate.parse(parts[5]);
            duration = Duration.parse(parts[6]);
        }
        switch (taskTypes) {
            case TASK: {
                Task newTask = new Task(id, name, description);
                newTask.setStatus(status);
                newTask.setStartTime(startTime);
                newTask.setDuration(duration);
                return Optional.of(newTask);
            }
            case EPIC: {
                Epic newEpic = new Epic(id, name, description);
                newEpic.setStatus(status);
                newEpic.setStartTime(startTime);
                newEpic.setDuration(duration);
                return Optional.of(newEpic);
            }
            case SUBTASK: {
                Subtask newSubtask = new Subtask(id, name, description, epic);
                newSubtask.setStatus(status);
                newSubtask.setStartTime(startTime);
                newSubtask.setDuration(duration);
                return Optional.of(newSubtask);
            }
        }
        return Optional.empty();
    }


    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
      HashMap<Integer, Task> allTasks = new HashMap<>();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        ArrayList<String> content = readFileContentsOrNull(file);
        if (!content.isEmpty()) {
            for (int i = 0; i < content.size(); i++) {
                String line = content.get(i);
                while (!line.isBlank()) {
                    Optional<Task> taskNew = fromString(line);
                    if (taskNew.isPresent()) {
                        Task task = taskNew.get();
                        TaskTypes taskTypes = task.getTaskTypes();
                        switch (taskTypes) {
                            case TASK: {
                                fileBackedTasksManager.tasks.put(task.getId(), task);
                                allTasks.put(task.getId(), task);
                                fileBackedTasksManager.maxId(task.getId());
                                break;
                            }
                            case EPIC: {
                                fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                                fileBackedTasksManager.maxId(task.getId());
                                allTasks.put(task.getId(), (Epic) task);
                                break;
                            }
                            case SUBTASK: {
                                fileBackedTasksManager.subtasks.put(task.getId(), (Subtask) task);
                                allTasks.put(task.getId(), (Subtask) task);
                                fileBackedTasksManager.maxId(task.getId());
                                break;
                            }
                        }
                        i = i + 1;
                        line = content.get(i);
                    }
                }
                if (line.isBlank()) {
                    i = i + 1;
                    line = content.get(i);
                    if (!line.isBlank()) {
                            List<Integer> idNew = historyFromString(line.trim());
                            for (Integer list : idNew) {
                                for (Integer key : allTasks.keySet()) {
                                    if (key.equals(list))
                                        fileBackedTasksManager.getHistoryManager().add(allTasks.get(key));// вызов метода восстановления historyFromString
                                }
                            }
                    } else return fileBackedTasksManager;
                }
            }
        }
        return fileBackedTasksManager;
    }

    void maxId(Integer id) {
        if (idIndex < id) {
            idIndex = id;
        }
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> idList = new ArrayList<>();
        if (value != null) {
            String[] ids = value.split(",");
            for (String id : ids) {
                idList.add(Integer.parseInt(id));
            }

            return idList;
        }

        return idList;
    }


    public static void main(String[] args) throws Exception {
        /*
        File file = new File("resources\\task_history.scv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);


        Task task = fileBackedTasksManager.createTask("Сходить в магазин", "Купить все по списку", LocalDate.of(2023, 02, 02), Duration.ofDays(1));
        Task task1 = fileBackedTasksManager.createTask("Доделать задачу", "Исправить ошибки по учебе", LocalDate.of(2023, 02, 03), Duration.ofDays(7));

        Epic epic = fileBackedTasksManager.createEpic("зачеты", "сдать долги");
        Subtask subtask = fileBackedTasksManager.createSubtask("матан", "долг по матрице", 3, LocalDate.of(2023, 01, 20), Duration.ofDays(1));
        Subtask subtask1 = fileBackedTasksManager.createSubtask("химия", "зачет по полимерам", 3, LocalDate.of(2023, 01, 01), Duration.ofDays(1));
        Subtask subtask2 = fileBackedTasksManager.createSubtask("Физика", "сдать курсач", 3, LocalDate.of(2023, 01, 07), Duration.ofDays(6));

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
        */
    }
}

