package use_case.delete_project;

import entity.User;

public interface DeleteProjectDataAccessInterface {

    User getUser(String userId);

    void saveUser();
}
