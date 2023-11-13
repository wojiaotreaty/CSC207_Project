package use_case.add_project;

import entity.Project;

public interface AddProjectDataAccessInterface {
    /**
     * @param project - a project object containing all information related to the project
     * given a Project object containing all details related to a given project, saveTasks will save the project into
     * the calendar database
     */
    void saveTasks(Project project);
}
