package use_case.signup;

public interface SignupDataAccessInterface {
    public User getUser(String username);
    public boolean saveUser(User user);
}
