package interface_adapter.dashboard;

public class ProjectData {
    private String projectTitle = "";
    private String projectID = "";
    private String projectDescription = "";
    private String projectTasks = "";

    public ProjectData(String projectID, String projectTitle, String projectDescription, String projectTasks) {
        this.projectTitle = projectTitle;
        this.projectID = projectID;
        this.projectDescription = projectDescription;
        this.projectTasks = projectTasks;
    }

    public String getProjectDescription() {
        return projectDescription;
    }
    public String getProjectTitle() {
        return projectTitle;
    }
    public String getProjectID() {
        return projectID;
    }
    public String getProjectTasks() {
        return projectTasks;
    }
}
