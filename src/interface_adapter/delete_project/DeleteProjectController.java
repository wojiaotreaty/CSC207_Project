package interface_adapter.delete_project;

import use_case.delete_project.DeleteProjectInputBoundary;
import use_case.delete_project.DeleteProjectInputData;

public class DeleteProjectController {

    final DeleteProjectInputBoundary deleteProjectUseCaseInteractor;

    public DeleteProjectController(DeleteProjectInputBoundary deleteProjectUseCaseInteractor){
        this.deleteProjectUseCaseInteractor = deleteProjectUseCaseInteractor;
    }

    public void execute(String projectId){
        deleteProjectUseCaseInteractor.execute(new DeleteProjectInputData(projectId));
    }
}
