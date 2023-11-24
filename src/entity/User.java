package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CommonUser implements User {

    private final String name;
    private final String password;
    public ArrayList<Project> projects;

    /**
     * Requires: password is valid.
     * @param name
     * @param password
     */
    CommonUser(String name, String password, ArrayList<Project> projects) {
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
