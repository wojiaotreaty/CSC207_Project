package interface_adapter.open_project;

import use_case.open_project.OpenProjectInputBoundary;
import use_case.open_project.OpenProjectInputData;

public class OpenProjectController {
    final OpenProjectInputBoundary openProjectInteractor;
    public OpenProjectController(OpenProjectInputBoundary openProjectInteractor) {
        this.openProjectInteractor = openProjectInteractor;
    }
    public void execute(String projectId) {
        OpenProjectInputData openProjectInputData = new OpenProjectInputData(projectId);
        openProjectInteractor.execute(openProjectInputData);
    }
}
