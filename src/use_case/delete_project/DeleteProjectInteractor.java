package use_case.delete_project;

import entity.Project;
import entity.User;
import interface_adapter.delete_project.DeleteProjectPresenter;

public class DeleteProjectInteractor implements DeleteProjectInputBoundary {

    final private DeleteProjectDataAccessInterface deleteDataAccessObject;
    final private DeleteProjectOutputBoundary deletePresenter;

    public DeleteProjectInteractor(DeleteProjectDataAccessInterface deleteDataAccessObject, DeleteProjectOutputBoundary deletePresenter) {
        this.deleteDataAccessObject = deleteDataAccessObject;
        this.deletePresenter = deletePresenter;
    }

    @Override
    public void execute(DeleteProjectInputData deleteProjectInputData) {
        String projectId = deleteProjectInputData.getProjectId();
        User currentUser = deleteDataAccessObject.getCurrentUser();

        Project deletedProject = currentUser.deleteProject(projectId);

//        TODO: delete this block after testing; THIS SHOULD NEVER HAPPEN
        if (deletedProject == null){
            throw new RuntimeException("Issue occurred while deleting project");
        }

        deleteDataAccessObject.saveUser();

        String projectName = deletedProject.getProjectName();
        DeleteProjectOutputData deleteProjectOutputData = new DeleteProjectOutputData(projectId, projectName);
        deletePresenter.prepareSuccessView(deleteProjectOutputData);
    }
}
