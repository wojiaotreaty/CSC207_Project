package use_case.set_status;

public class SetStatusOutputData {
    private final String projectId;
    private final String taskString;

    public SetStatusOutputData(String projectId, String taskString) {
        this.projectId = projectId;
        this.taskString = taskString;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTaskString() {
        return taskString;
    }
}
