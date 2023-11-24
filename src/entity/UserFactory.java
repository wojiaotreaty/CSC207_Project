package entity;

import java.util.ArrayList;

public class CommonUserFactory implements UserFactory {

    @Override
    public User create(String username, String password, ArrayList<Project> projects) {
        return new CommonUser(username, password, projects);
    }
}
