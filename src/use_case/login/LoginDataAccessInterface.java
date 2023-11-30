package use_case.login;

public interface LoginDataAccessInterface {

    User getUser(String username);
    boolean saveUser(User user);

}
