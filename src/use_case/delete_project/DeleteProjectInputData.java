package use_case.delete_project;
public class DeleteProjectInputData {

    final private String username;
    final private String projectId;

    public DeleteProjectInputData(String username, String projectId) {
        this.username = username;
        this.projectId = projectId;
    }

    String getUsername() {return this.username;}

    String getProjectId() {
        return this.projectId;
}
}
