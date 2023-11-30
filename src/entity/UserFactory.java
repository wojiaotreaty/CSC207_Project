package entity;

import java.util.ArrayList;

/**
 * This file is a placeholder for DAO functionality.
 */
public interface UserFactory {

    User create(String username, String password, ArrayList<Project> projects);
}
