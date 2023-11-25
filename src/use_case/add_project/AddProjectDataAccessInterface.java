package use_case.add_project;

import entity.Project;

public interface AddProjectDataAccessInterface {
    User getCurrentUser();
    boolean saveUser(User user);
    String generateNewProjectId();
}
