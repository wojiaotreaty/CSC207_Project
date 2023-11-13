package entity;

import java.util.ArrayList;
import java.util.HashMap;

public interface ProjectFactory {
    /**
     * @param projectTitle - the title of the project
     * @param tasks - an ArrayList representing the subtasks that the project has been broken down to, to put into the
     * calendar. Each task is represented by a HashMap, mapping an attribute of the task to its value. Each subtask will
     * have the following keys and values:
     *      - TaskName: <name of the task>
     *      - TaskDescription: <description of the task>
     *      - TaskDeadline: <deadline of the task>
     */
    Project create(String projectTitle, ArrayList<HashMap<String, String>> tasks);
}
