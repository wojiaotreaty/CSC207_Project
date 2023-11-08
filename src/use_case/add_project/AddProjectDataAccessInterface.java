package use_case.add_project;

import java.util.HashMap;

public interface AddProjectDataAccessInterface {
    void saveTasks(HashMap<String, String> taskMap);
}
