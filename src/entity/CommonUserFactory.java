package entity;

import java.util.ArrayList;

public class CommonUserFactory implements UserFactory {
    User create(String username, String password, ArrayList<Project> projects) {
        return new CommonUser(username, password, projects);
    }
    User create(String username, String password) {
        return new CommonUser(username, password, projects);
    }
}
