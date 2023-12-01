package interface_adapter.dashboard;

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

        addProjectViewModel.firePropertyChanged();
    }
    @Override
    public void prepareFailView(String error) {
        DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setAddProjectError(error);

        addProjectViewModel.firePropertyChanged();
    }
}
