package entity;

import java.util.ArrayList;

/**
 * This file is a placeholder for DAO functionality.
 */
public interface UserFactory {

    User create(String email, ArrayList<Project> projects);
}
