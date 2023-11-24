package use_case.delete_project;
public class DeleteProjectInputData {

    final private String projectId;

    public DeleteProjectInputData(String projectId) {
        this.projectId = projectId;
    }

    String getProjectId() {
        return projectId;
}
}
