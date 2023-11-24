package use_case.signup;

import entity.User;

public interface SignupDataAccessInterface {
    public User getUser(String username);
    public boolean saveUser(User user);
}
