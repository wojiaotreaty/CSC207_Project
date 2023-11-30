package use_case.open_project;

import java.util.ArrayList;

public class OpenProjectOutputData {
    private final String projectId;
    private final String name;
    private final String description;
    private final ArrayList<String> tasks;
    public OpenProjectOutputData(String projectId, String name, String description,
                                 ArrayList<String> tasks) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.tasks = tasks;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getTasks() {
        return tasks;
    }
}
