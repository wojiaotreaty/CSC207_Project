package entity;

import java.util.ArrayList;
import java.util.HashMap;

public interface Project {
    String getProjectID();
    String getProjectDesc();

    ArrayList<Task> getTasks();
}
