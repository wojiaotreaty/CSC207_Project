package entity;

import java.util.ArrayList;

/**
 * This file is a placeholder for DAO functionality.
 */
public interface UserFactoryInterface {

    UserInterface create(String username, String password, ArrayList<Project> projects);
}
