package entity;

import java.util.ArrayList;
import java.util.HashMap;

public interface Project extends Iterable<Task> {
    String getProjectId();
    String getProjectDescription();
    String getProjectName();
    ArrayList<Task> getTasks();
}

