package use_case.open_project;

public class OpenProjectInputData {
    private final String projectId;
    public OpenProjectInputData(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }
}
