import java.util.HashMap;
import java.util.Scanner;

public class Manager {

    static Scanner scanner = new Scanner(System.in);
    public HashMap<Integer, Task> tasks = new HashMap<>();

    public HashMap<Integer, Epic> epics = new HashMap<>();

    public HashMap<Integer, Subtask> subtasks = new HashMap<>();


    public void watchTask() {
        System.out.println("Список задач:");
        for (Task value : this.tasks.values()) {
            System.out.println(value.toString());
        }
    }

    public void watchEpic() {
        for (Epic value : this.epics.values()) {
            System.out.println("Список ");
            System.out.println(value.toString());
            System.out.println("Подзадачи");
            for (Subtask subtask : value.getSubtasks().values()) {
                System.out.println(subtask.toString());

            }
        }
    }


    public void watchSubtask() {
        System.out.println("Всех подзадач:");
        for (Subtask value : this.subtasks.values()) {
            System.out.println(value.toString());

        }
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


    public void getTaskById(int id) {
        System.out.println(tasks.get(id));
    }

    public void getEpicById(int id) {
        System.out.println(epics.get(id));
    }

    public void getSubtaskById(int id) {
        System.out.println(subtasks.get(id));
    }


    public Task createTask(String name, String description) {

        Task task = new Task(Status.NEW, name, description);
        task.save();
        this.tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(String name, String description) {

        Epic epic = new Epic(Status.NEW, name, description);
        epic.save();
        this.epics.put(epic.getId(), epic);
        return epic;

    }

    public Subtask createSubtask(Task task, Epic epic) {
        Subtask subtask = new Subtask(task, epic);
        this.subtasks.put(subtask.getId(), subtask);
        return subtask;
    }

    public Epic createEpicByTask(Task task) {
        Epic epic = new Epic(task.getStatus(), task.getName(), task.getDescription());
        epic.setId(task.getId());

        this.epics.put(epic.getId(), epic);
        epic.save();
        return epic;
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



    public void updateStatusByTask(Task task, Status status) {
        task.setStatus(status);

    }

    public void updateStatusByIdEpic(Epic epic, Status status) {
        System.out.println("Не может обновлять статсы эпика");
        watchEpic();
    }

    public void updateStatusByIdSubtask(Subtask subtask, Status status) {
        subtask.setStatus(status);

    }


    public void addNewTaskInEpic(int id, String name, String description) {

        Task newTask = new Task(Status.NEW, name, description);

        Subtask newEpic = null;
        Epic epic = newEpic.getEpicById(id);
        Subtask subtask = new Subtask(newTask, epic);

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
        tasks.put(epic.getId(), epic);
    }

    public void updateEpic(Subtask subtask) {
        tasks.put(subtask.getId(), subtask);
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
}
