package use_case.set_status;

public class SetStatusInputData {
    final private String projectId;
    final private String taskId;
    public SetStatusInputData(String projectId, String taskId) {
        this.projectId = projectId;
        this.taskId = taskId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTaskId() {
        return taskId;
    }
}
