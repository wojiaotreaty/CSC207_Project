package use_case.RefactorProject;

import entity.Project;
import entity.User;

import java.time.LocalDate;
import java.util.ArrayList;

public interface RefactorProjectDataAccessInterface {
    // getting the user from the dao
    User getUser(String userName);
    boolean saveUser(User user);
}
