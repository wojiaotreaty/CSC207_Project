package entity;

import java.util.ArrayList;

public class CommonProjectFactory implements ProjectFactory {
    Project create(String projectId, String projectName, String projectDesc, ArrayList<Task> tasks) {
        return new CommonProject(projectId, projectName, projectDesc, tasks);
    }
}
