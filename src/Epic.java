import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(Status status, String name, String description) {
        super(status, name, description);
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


}
