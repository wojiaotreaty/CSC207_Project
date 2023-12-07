package use_case.refactor_project;

public class RefactorProjectOutputData {
private final String projectId;
private final String projectName;
private final String projectDescription;
private final String tasks;

    public RefactorProjectOutputData(String projectID, String projectName, String projectDescription, String tasks) {
    this.projectId=projectID;
    this.projectName=projectName;
    this.projectDescription=projectDescription;
    this.tasks=tasks;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getTasks() {
        return tasks;
    }
}
