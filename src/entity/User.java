package entity;

import java.util.ArrayList;

public class User {
    private final String username;
    private final String password;
    private final ArrayList<Project> projects;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.projects = new ArrayList<Project>();
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
