package interface_adapter.add_project;

import use_case.add_project.AddProjectInputBoundary;
import use_case.add_project.AddProjectInputData;

public class AddProjectController {

    final AddProjectInputBoundary addProjectUseCaseInteractor;
    public AddProjectController(AddProjectInputBoundary addProjectUseCaseInteractor) {
        this.addProjectUseCaseInteractor = addProjectUseCaseInteractor;
    }

    public void execute(String projectTitle, String projectDetails, String projectDeadline, String username) {
        AddProjectInputData addProjectInputData = new AddProjectInputData(
                projectTitle, projectDetails, projectDeadline, username);

        addProjectUseCaseInteractor.execute(addProjectInputData);
    }
}
