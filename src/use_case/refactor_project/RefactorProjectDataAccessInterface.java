package use_case.refactor_project;

import entity.User;

public interface RefactorProjectDataAccessInterface {
    // getting the user from the dao
    User getUser(String userName);
    boolean saveUser(User user);
}
