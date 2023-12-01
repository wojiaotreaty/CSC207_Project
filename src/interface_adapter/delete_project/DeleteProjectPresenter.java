package interface_adapter.delete_project;

import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.dashboard.DashboardState;
import use_case.delete_project.DeleteProjectOutputBoundary;
import use_case.delete_project.DeleteProjectOutputData;

public class DeleteProjectPresenter implements DeleteProjectOutputBoundary {

    private final DeleteProjectViewModel deleteProjectViewModel;
    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;

    public DeleteProjectPresenter(DeleteProjectViewModel deleteProjectViewModel,
                                  DashboardViewModel dashboardViewModel,
                                  ViewManagerModel viewManagerModel) {
        this.deleteProjectViewModel = deleteProjectViewModel;
        this.dashboardViewModel = dashboardViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(DeleteProjectOutputData deleteProjectOutputData) {
//        On success, update deleteProjectViewModel to reflect that.
        DeleteProjectState deleteProjectState = deleteProjectViewModel.getState();
        deleteProjectState.setDeletedProjectId(deleteProjectOutputData.getProjectId());
        deleteProjectState.setDeletedProjectName(deleteProjectOutputData.getProjectName());

        deleteProjectViewModel.setState(deleteProjectState);
        deleteProjectViewModel.firePropertyChanged();

        DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.deleteProjectData(deleteProjectOutputData.getProjectId());

        dashboardViewModel.setState(dashboardState);
        dashboardViewModel.firePropertyChanged();

        viewManagerModel.setActiveView(dashboardViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
