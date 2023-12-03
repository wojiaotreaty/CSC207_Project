package use_case.RefactorProject;

import entity.Project;
import entity.User;

import java.time.LocalDate;
import java.util.ArrayList;

public interface RefactorProjectDataAccessInterface {
    ArrayList<Project> getProjects(String[] Ids);
    void setTaskDeadline(String projectId, String taskId, LocalDate deadline);
    User getUser(String userName);
    boolean saveUser(User user);
}
