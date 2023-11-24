package use_case.login;

import entity.CommonUser;

public interface LoginDataAccessInterface {
    boolean existsByName(String identifier);

    void save(CommonUser user);

    CommonUser get(String username);
}
