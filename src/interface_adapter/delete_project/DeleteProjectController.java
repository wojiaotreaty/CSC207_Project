package interface_adapter.delete_project;

import use_case.delete_project.DeleteProjectInputBoundary;
import use_case.delete_project.DeleteProjectInputData;

public class DeleteProjectController {

    final DeleteProjectInputBoundary deleteProjectUseCaseInteractor;
    final DeleteProjectInputData deleteProjectInputData;

    public DeleteProjectController(DeleteProjectInputBoundary deleteProjectUseCaseInteractor, DeleteProjectInputData deleteProjectInputData){
        this.deleteProjectUseCaseInteractor = deleteProjectUseCaseInteractor;
        this.deleteProjectInputData = deleteProjectInputData;
    }

    public void execute(){
        deleteProjectUseCaseInteractor.execute(deleteProjectInputData);
    }
}
