package interface_adapter.delete_project;

import use_case.delete_project.DeleteProjectOutputBoundary;
import use_case.delete_project.DeleteProjectOutputData;

public class DeleteProjectPresenter implements DeleteProjectOutputBoundary {

    private final DeleteProjectViewModel deleteProjectViewModel;

    public DeleteProjectPresenter(DeleteProjectViewModel deleteProjectViewModel) {
        this.deleteProjectViewModel = deleteProjectViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteProjectOutputData deleteProjectOutputData) {
//        On success, update deleteProjectViewModel to reflect that.
        DeleteProjectState state = deleteProjectViewModel.getState();
        state.setDeletedProjectId(deleteProjectOutputData.getProjectId());
        state.setDeletedProjectName(deleteProjectOutputData.getProjectName());

        deleteProjectViewModel.setState(state);
        deleteProjectViewModel.firePropertyChanged();
    }
}
