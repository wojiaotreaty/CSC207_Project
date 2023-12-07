package use_case.signup;

import entity.User;
public interface SignupDataAccessInterface {
    User getUser(String username);
    boolean saveUser(User user);
}
