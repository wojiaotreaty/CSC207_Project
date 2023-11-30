package use_case.open_project;

import entity.Project;
import entity.Task;

import java.util.ArrayList;

public class OpenProjectInteractor implements OpenProjectInputBoundary {
    final OpenProjectProjectsDataAccessInterface projectsDataAccessObject;
    final OpenProjectOutputBoundary openProjectPresenter;
    public OpenProjectInteractor(OpenProjectProjectsDataAccessInterface projectsDataAccessObject,
                                 OpenProjectOutputBoundary openProjectPresenter) {
        this.projectsDataAccessObject = projectsDataAccessObject;
        this.openProjectPresenter = openProjectPresenter;
    }
    @Override
    public void execute(OpenProjectInputData openProjectInputData) {
        // Retrieves relevant project from DAO
        Project project = projectsDataAccessObject.getProjects(new String[]{openProjectInputData.getProjectId()}).get(0);
        ArrayList<Task> tasks = project.getTasks();
        // creates ArrayList to store tasks in proper format for OpenProjectOutputData
        ArrayList<String> outputTasks = new ArrayList<String>();
        // Note that since the tasks are ordered chronologically in the project entity,
        // they will automatically be ordered when added this way.
        for (Task task : tasks) {
            outputTasks.add(task.toStringUwu());
        }
        OpenProjectOutputData openProjectOutputData = new OpenProjectOutputData(project.getId(), project.getName(), project.getDesc(), outputTasks);
        openProjectPresenter.prepareView(openProjectOutputData);
    }
}
