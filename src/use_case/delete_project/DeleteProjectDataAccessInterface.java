package use_case.delete_project;

import entity.User;

public interface DeleteProjectDataAccessInterface {

    User getCurrentUser();

    void saveUser();
}
