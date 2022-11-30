import java.util.HashMap;

public class Subtask extends Task {

    private HashMap<Integer, Epic> epics = new HashMap<>();

    public Subtask(Task task, Epic epic) {
        super(task.getStatus(), task.getName(), task.getDescription());

        this.id = task.getId();
        this.epics.put(epic.getId(), epic);

        epic.addSubtasks(this);
    }

    public Epic getEpicById(Integer id) {
        return this.epics.get(id);
    }

    @Override
    public void save() {
    }

}
