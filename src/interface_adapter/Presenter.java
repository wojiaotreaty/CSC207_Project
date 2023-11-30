package interface_adapter;

import use_case.RefactorProject.RefactorProjectOutputBoundary;
import use_case.RefactorProject.RefactorProjectOutputData;

public class Presenter implements RefactorProjectOutputBoundary {
    private final ProjectViewModel projectViewModel;

    public Presenter(ProjectViewModel projectViewModel) {
        this.projectViewModel = projectViewModel;
    }

    @Override
    public void prepareSuccessView(RefactorProjectOutputData response) {
        ProjectState projectState = projectViewModel.getState();
        projectState.setRefactorProjectError(null);

        ProjectState.setTasks(response.getTask());

        projectViewModel.firePropertyChanged();
    }
}
