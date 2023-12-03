package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class CommonUser implements User, Iterable<Project> {
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
        return projects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonUser that = (CommonUser) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) &&
                Objects.equals(projects, that.projects);
    }

    @Override
    public String toString(){
        String result = "";
        result += "Username: " + this.username + "\n";
        StringBuilder projectList = new StringBuilder();
        for (Project project : this.projects){
            projectList.append("\t").append(project.getProjectId()).append(": ").append(project.getProjectName());
        }
        result += projectList;

        return result;
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

    @Override
    public Iterator<Project> iterator() {
        return new Iter(projects);
    }
    private class Iter implements Iterator<Project> {
        int cursor = 0;
        ArrayList<Project> p;

        public Iter(ArrayList<Project> p) {
            this.p = p;
        }

        @Override
        public boolean hasNext() {
            return cursor < p.size();
        }

        @Override
        public Project next() {
            if (cursor >= p.size()) {
                throw new NoSuchElementException();
            }
            cursor = cursor + 1;
            return p.get(cursor - 1);
        }
    }
}
