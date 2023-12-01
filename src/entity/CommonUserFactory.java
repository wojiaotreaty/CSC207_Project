package entity;

import java.util.ArrayList;

public class CommonUserFactory implements UserFactory {
    public User create(String username, String password, ArrayList<Project> projects) {
        return new CommonUser(username, password, projects);
    }
    public User create(String username, String password) {
        return new CommonUser(username, password);
    }
}
