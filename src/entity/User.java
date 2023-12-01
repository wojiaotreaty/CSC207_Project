package entity;

import java.util.ArrayList;

public interface User {
    String getUsername();
    String getPassword();
    ArrayList<Project> getProjects();
    Project getProject(String id);

    // NOTE: we can either do this, or make the deleteProject() in User take a Project.
    // I think this approach makes more sense but am amenable to changes

    /**
     * removes the project that has the given id from this User's projects.
     * @param projectId id of the project to be deleted
     * @return the deleted project if successful, null if unsuccessful
     */
    Project deleteProject(String projectId);
}
