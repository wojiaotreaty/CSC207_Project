package entity;

import java.util.ArrayList;
/*
Temporary User file that will be replaced with actual entity
 */
public class User implements UserInterface {

    private final String name;
    private final String password;
    public ArrayList<Project> projects;

    /**
     * Requires: password is valid.
     */
    User(String name, String password, ArrayList<Project> projects) {
        this.name = name;
        this.password = password;
        this.projects = projects;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public ArrayList<Project> getProjects() {
        return null;
    }
}
