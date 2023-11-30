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
    public ArrayList<Project> getProjects() {
        return (ArrayList<Project>) projects.clone();
    }
    public Project deleteProject(String id) {
        for (Project project : projects) {
            if (project.getId().equals(id)) {
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
