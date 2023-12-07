package entity;

import java.util.ArrayList;

public interface UserFactory {

    User create(String username, String password);
    User create(String username, String password, ArrayList<Project> projects);
}
