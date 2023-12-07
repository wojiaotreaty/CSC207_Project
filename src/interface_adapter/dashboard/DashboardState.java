package interface_adapter.dashboard;

import java.util.ArrayList;

public class DashboardState {
    private String username = "";
    private ArrayList<ProjectData> projects = new ArrayList<ProjectData>();
    private String addProjectError = null;
    private String notificationMessage = null;
    private String notificationImage = null;

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

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationImage(String notificationImage) {
        this.notificationImage = notificationImage;
    }

    public String getNotificationImage() {
        return notificationImage;
    }
}
