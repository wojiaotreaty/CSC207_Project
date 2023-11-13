package use_case.add_project;

import java.util.ArrayList;
import java.util.HashMap;

public interface AddProjectDataAccessInterface {
    /**
     * Takes an ArrayList of tasks to put into the calendar. Each task is represented by a HashMap, mapping
     * an attribute of the task to its value. Each task will have the following keys and values:
     *      - TaskName: <name of the task>
     *      - TaskDescription: <description of the task>
     *      - TaskDeadline: <deadline of the task>
     * @param taskMap
     */
    void saveTasks(ArrayList<HashMap<String, String>> tasks);
}
