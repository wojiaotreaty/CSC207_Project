package interface_adapter.dashboard;

import java.util.ArrayList;
import java.util.Arrays;

public class ProjectData {
    private final String projectTitle;
    private final String projectID;
    private final String projectDescription;
    private final ArrayList<ArrayList<String>> projectTasks = new ArrayList<>();

    public ProjectData(String projectID, String projectTitle, String projectDescription, String projectTasks) {
        this.projectTitle = projectTitle;
        this.projectID = projectID;
        this.projectDescription = projectDescription;

        String[] tasks = projectTasks.split("[|]uwu[|]");
        for (String task : tasks) {
            String[] taskComponents = task.split("`");

            this.projectTasks.add(new ArrayList<>(Arrays.asList(taskComponents)));
        }
    }

    public String getProjectDescription() {
        return projectDescription;
    }
    public String getProjectTitle() {
        return projectTitle;
    }
    public String getProjectID() {
        return projectID;
    }
    public ArrayList<ArrayList<String>> getProjectTasks() {
        return projectTasks;
    }
}
