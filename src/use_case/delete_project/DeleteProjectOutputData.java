package use_case.delete_project;
public class DeleteProjectOutputData {

    final private String projectId;
    final private String projectName;

    public DeleteProjectOutputData(String projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

}
