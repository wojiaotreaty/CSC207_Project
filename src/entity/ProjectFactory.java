package entity;

import java.util.ArrayList;

public interface ProjectFactory {

    Project create(String projectId, String projectName, String projectDesc, ArrayList<Task> tasks);
}
