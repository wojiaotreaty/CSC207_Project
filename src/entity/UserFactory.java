package entity;

import java.util.ArrayList;

public class UserFactory implements UserFactoryInterface {

    @Override
    public User create(String username, String password) {
        return new User(username, password, projects);
    }
}
