package interface_adapter;

import use_case.refactor_project.RefactorProjectOutputBoundary;
import use_case.refactor_project.RefactorProjectOutputData;
import interface_adapter.dashboard;
public class Presenter implements RefactorProjectOutputBoundary {
    private final DashboardViewModel dashboardViewModel;

    public Presenter(DashBoardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
    }

    @Override
    public void prepareSuccessView(RefactorProjectOutputData response) {
        DashboardState dashboardState = dashboardViewModel.getState();
        projectState.setRefactorProjectError(null);

        ProjectState.setTasks(response.getTask());

        projectViewModel.firePropertyChanged();
    }
    @Override
    public void prepareFailView(String error) {
        ProjectState projectState = projectViewModel.getState();
        projectState.setRefactorProjectError(error);

        projectViewModel.firePropertyChanged();
    }

}
