package entity;

import java.util.ArrayList;

public class CommonUser implements User {
    private final String username;
    private final String password;
    private final ArrayList<Project> projects;
    public CommonUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.projects = new ArrayList<Project>();
    }
    public CommonUser(String username, String password, ArrayList<Project> projects) {
        this.username = username;
        this.password = password;
        this.projects = projects;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Project getProject(String id) {
        for (Project project : projects) {
            if (project.getProjectId().equals(id)) {
                return project;
            }
        }
        return null;
    }

    public ArrayList<Project> getProjects() {
        return (ArrayList<Project>) projects.clone();
    }
    public Project deleteProject(String id) {
        for (Project project : projects) {
            if (project.getProjectId().equals(id)) {
                projects.remove(project);
                return project;
            }
        }
        return null;
    }
    public void addProject(Project project) {
        projects.add(project);
    }
}
