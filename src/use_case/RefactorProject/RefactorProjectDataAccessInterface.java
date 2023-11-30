package use_case.RefactorProject;

import entity.Project;

import java.time.LocalDate;
import java.util.ArrayList;

public interface RefactorProjectDataAccessInterface {
    ArrayList<Project> getProjects(String[] Ids);
    void setTaskDeadline(String projectId, String taskId, LocalDate deadline);
}
