public class Task {
    static Integer idIndex = 1;

    protected Integer id ;
    private String name;
    private String description;
    private Status status;

    public Task(Status status, String name, String description) {
        this.status = status;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void save() {

        this.id = idIndex;
        idIndex = idIndex + 1;

    }
    public String toString(){
        return "id = "+ id +"; name = " +name + "; description = " +description +"; status = " +status;
    }

}
