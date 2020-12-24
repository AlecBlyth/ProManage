package object_classes;

public class taskObject {

    private final int id;
    private String taskType;
    private String taskName;
    private String taskDesc;
    private String taskHex;
    private int taskProgress;
    private int taskSection;
    private String taskSubject;

    public int getTaskID() {
        return id;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public String getTaskHex() {
        return taskHex;
    }

    public int getTaskProgress() {
        return taskProgress;
    }

    public int getTaskSection() {
        return taskSection;
    }

    public String getTaskSubject() {
        return taskSubject;
    }

    public taskObject(int id, String taskType, String taskName, String taskDesc, String taskHex, int taskProgress, int taskSection, String taskSubject){
        this.id = id;
        this.taskType = taskType;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskHex = taskHex;
        this.taskProgress = taskProgress;
        this.taskSection = taskSection;
        this.taskSubject = taskSubject;
    }
}
