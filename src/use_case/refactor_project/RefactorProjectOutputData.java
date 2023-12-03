package use_case.refactor_project;

import entity.Project;
import entity.Task;

import java.util.ArrayList;

public class RefactorProjectOutputData {
private String projectId;
private String projectName;
private String projectDescription;
private String tasks;


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
