package use_case.delete_project;

import entity.Project;
import entity.User;

public class DeleteProjectInteractor implements DeleteProjectInputBoundary {

    final private DeleteProjectDataAccessInterface userDataAccessObject;
    final private DeleteProjectOutputBoundary deleteProjectPresenter;

    public DeleteProjectInteractor(DeleteProjectDataAccessInterface userDataAccessObject, DeleteProjectOutputBoundary deleteProjectPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.deleteProjectPresenter = deleteProjectPresenter;
    }

    @Override
    public void execute(DeleteProjectInputData deleteProjectInputData) {
        String projectId = deleteProjectInputData.getProjectId();
        User user = userDataAccessObject.getUser(deleteProjectInputData.getUsername());

        Project deletedProject = user.deleteProject(projectId);

        userDataAccessObject.saveUser(user);

        String projectName = deletedProject.getProjectName();
        DeleteProjectOutputData deleteProjectOutputData = new DeleteProjectOutputData(projectId, projectName);
        deleteProjectPresenter.prepareSuccessView(deleteProjectOutputData);
    }
}
