package use_case.login;

import entity.User;

public interface LoginDataAccessInterface {

    User getUser(String username);
    boolean saveUser(User user);

}
