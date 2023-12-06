package use_case.add_project;

public class AddProjectInputData {

    final private String projectTitle;
    final private String projectDetails;
    final private String projectDeadline;
    final private String username;

    public AddProjectInputData(String projectTitle, String projectDetails, String projectDeadline, String username) {
        this.projectTitle = projectTitle;
        this.projectDetails = projectDetails;
        this.projectDeadline = projectDeadline;
        this.username = username;
    }

    String getProjectTitle() {
        return projectTitle;
    }

    String getProjectDetails() {
        return projectDetails;
    }

    String getProjectDeadline() {
        return projectDeadline;
    }
    String getUsername() {
        return username;
    }
}
