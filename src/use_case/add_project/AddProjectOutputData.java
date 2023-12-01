package use_case.add_project;

import java.util.ArrayList;

public class AddProjectOutputData {
    final private ArrayList<String> project = new ArrayList<String>();


    public AddProjectOutputData(String projectID, String projectTitle, String projectDescription, String projectTasks) {
        this.project.add(projectID);
        this.project.add(projectTitle);
        this.project.add(projectDescription);
        this.project.add(projectTasks);

    }
    public ArrayList<String> getProject () { return this.project; }
}