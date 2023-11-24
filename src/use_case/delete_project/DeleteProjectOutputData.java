package use_case.delete_project;
public class DeleteProjectOutputData {

    final private String projectId;
    final private String projectName;

    public DeleteProjectOutputData(String projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    String getProjectId() {
        return projectId;
    }

    String getProjectName() {
        return projectName;
    }

}
