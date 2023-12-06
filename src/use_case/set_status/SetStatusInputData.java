package use_case.set_status;

public class SetStatusInputData {
    private final String username;
    final private String projectId;
    final private String taskString;
    public SetStatusInputData(String username, String projectId, String taskString) {
        this.username = username;
        this.projectId = projectId;
        this.taskString = taskString;
    }

    public String getUsername() {
        return username;
    }
    public String getProjectId() {
        return projectId;
    }

    public String getTaskString() {
        return taskString;
    }
}
