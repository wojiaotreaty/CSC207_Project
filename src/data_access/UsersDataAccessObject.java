package data_access;

import entity.User;
import use_case.login.LoginDataAccessInterface;
import use_case.signup.SignupDataAccessInterface;

/**
 * This DAO interacts with information of users (username, password, list of projects).
 * It DOES NOT have project information.
 */

/*
Copied from Bing's Dao implementation with signupDataAccessInterface and LoginDataAccessInterface
 */

public class UsersDataAccessObject implements SignupDataAccessInterface, LoginDataAccessInterface {

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public boolean saveUser(User user) {
        return false;
    }
}