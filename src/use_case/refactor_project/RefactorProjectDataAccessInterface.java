package use_case.refactor_project;

import entity.Project;
import entity.User;

import java.time.LocalDate;
import java.util.ArrayList;

public interface RefactorProjectDataAccessInterface {
    User getUser(String userName);
    boolean saveUser(User user);
}
