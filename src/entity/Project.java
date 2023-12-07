package entity;

import java.util.ArrayList;

public interface Project extends Iterable<Task> {
    String getProjectId();
    String getProjectDescription();
    String getProjectName();
    ArrayList<Task> getTasks();
}

