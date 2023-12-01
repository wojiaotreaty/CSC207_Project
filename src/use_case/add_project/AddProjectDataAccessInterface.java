package use_case.add_project;

import entity.User;

public interface AddProjectDataAccessInterface {
    User getUser(String username);
    boolean saveUser(User user);
    String generateNewProjectId();
}
