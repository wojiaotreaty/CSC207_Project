package entity;

import java.util.ArrayList;

public class UserFactory implements UserFactoryInterface {
    User create(String username, String password, ArrayList<Project> projects) {
        return new User(username, password, projects);
    }
}
