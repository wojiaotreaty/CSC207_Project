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
        Project project = projectsDataAccessObject.getProjects(new String[]{openProjectInputData.getProjectId()}).get(0);
        ArrayList<Task> tasks = project.getTasks();
        ArrayList<String[]> outputTasks = new ArrayList<String[]>();
        for (Task task : tasks) {
            outputTasks.add(new String[]{task.getName(), task.getDeadline().toString(), task.getDescription(), String.valueOf(task.getStatus())});
        }
        OpenProjectOutputData openProjectOutputData = new OpenProjectOutputData(project.getId(), project.getName(), project.getDesc(), outputTasks);
        openProjectPresenter.prepareView(openProjectOutputData);
    }
}
