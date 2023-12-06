package interface_adapter.add_project;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import use_case.add_project.AddProjectOutputBoundary;
import use_case.add_project.AddProjectOutputData;

public class AddProjectPresenter implements AddProjectOutputBoundary {

    private final DashboardViewModel dashboardViewModel;

    public AddProjectPresenter(DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
    }

    @Override
    public void prepareSuccessView(AddProjectOutputData response) {
        DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setAddProjectError(null);
        dashboardState.addProjectData(response.getProject());

        dashboardViewModel.firePropertyChanged();
    }
    @Override
    public void prepareFailView(String error) {
        DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setAddProjectError(error);

        dashboardViewModel.firePropertyChanged();
    }
}
