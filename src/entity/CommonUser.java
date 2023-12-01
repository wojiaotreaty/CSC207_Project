package entity;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonUser that = (CommonUser) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) && Objects.equals(projects, that.projects);
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
