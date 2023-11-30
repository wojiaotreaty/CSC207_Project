package entity;

import java.util.ArrayList;

public class ProjectFactory implements ProjectFactoryInterface {
    Project create(String projectId, String projectName, String projectDesc, ArrayList<Task> tasks) {
        return new Project(projectId, projectName, projectDesc, tasks);
    }
}
