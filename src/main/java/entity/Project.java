package entity;

import java.util.ArrayList;
import java.util.HashMap;

public interface Project {
    String getProjectID();
    String getProjectDesc();

    ArrayList<HashMap<String, String>> getTasks();

    void addTask(ArrayList<HashMap<String, String>> task);
}
