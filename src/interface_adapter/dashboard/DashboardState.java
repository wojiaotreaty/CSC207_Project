package interface_adapter.dashboard;

import java.util.ArrayList;

public class DashboardState {
    private String username = "";
    private ArrayList<ProjectData> projects;
    private String addProjectError = null;

    public DashboardState(DashboardState copy) {
        username = copy.username;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public DashboardState() {
    }

    public String getAddProjectError() { return addProjectError; }
    public void setAddProjectError(String error) { this.addProjectError = error; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<ProjectData> getProjects() { return projects; }

    public void setProjects(ArrayList<ArrayList<String>> listOfProjects) {
        ArrayList<ProjectData> projects = new ArrayList<>();

        for (ArrayList<String> project : listOfProjects) {
            //project is formatted as follows: {String projectID, String projectTitle, String projectDescription, String projectTasks}
            ProjectData projectData = new ProjectData(project.get(0), project.get(1), project.get(2), project.get(3));
            projects.add(projectData);
        }

        this.projects = projects;
    }

    public void addProjectData(ArrayList<String> project) {
        ProjectData projectData = new ProjectData(project.get(0), project.get(1), project.get(2), project.get(3));
        this.projects.add(projectData);
    }

    public void deleteProjectData(String projectID){
        for (ProjectData project : this.projects){
            if (project.getProjectID().equals(projectID)){
                projects.remove(project);
                break;
            }
        }
    }
}

