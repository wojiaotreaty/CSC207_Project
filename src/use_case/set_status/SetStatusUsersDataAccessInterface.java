package use_case.set_status;

import entity.User;

public interface SetStatusUsersDataAccessInterface {
    User getUser(String username);
}
