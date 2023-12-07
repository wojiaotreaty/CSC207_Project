package interface_adapter.refactor_project;

import interface_adapter.ViewManagerModel;

import interface_adapter.dashboard.DashboardState;

import interface_adapter.dashboard.DashboardViewModel;

import use_case.refactor_project.RefactorProjectOutputBoundary;

import use_case.refactor_project.RefactorProjectOutputData;

import java.util.ArrayList;

public class RefactorProjectPresenter implements RefactorProjectOutputBoundary {
    private final DashboardViewModel dashboardViewModel;

    private final ViewManagerModel viewManagerModel;

    private final RefactorProjectViewModel refactorProjectViewModel;

    public RefactorProjectPresenter( RefactorProjectViewModel refactorProjectViewModel,

                                     DashboardViewModel dashboardViewModel,

                                     ViewManagerModel viewManagerModel) {
        this.dashboardViewModel = dashboardViewModel;

        this.viewManagerModel = viewManagerModel;

        this.refactorProjectViewModel=refactorProjectViewModel;
    }

    @Override
    public void prepareSuccessView(RefactorProjectOutputData refactorProjectOutputData) {
        RefactorProjectState refactorProjectState = refactorProjectViewModel.getState();

        String projectID = refactorProjectOutputData.getProjectId();

        String Tasks =  refactorProjectOutputData.getTasks();

        refactorProjectState.setRefactoredProjectId(projectID);

        refactorProjectState.setRefactoredProjectTasks(Tasks);

        refactorProjectViewModel.setState(refactorProjectState);

        refactorProjectViewModel.firePropertyChanged();

        DashboardState dashboardState = dashboardViewModel.getState();

        dashboardState.deleteProjectData(refactorProjectOutputData.getProjectId());

        ArrayList<String>project=new ArrayList<String>();

        // Adding the project ID
        project.add(refactorProjectOutputData.getProjectId());

        // Adding the project name
        project.add(refactorProjectOutputData.getProjectName());

        // Adding the project description
        project.add(refactorProjectOutputData.getProjectDescription());


        // adding the tasks
        project.add(refactorProjectOutputData.getTasks());
        //adding the project data to the project

        dashboardState.addProjectData(project);

        dashboardViewModel.setState(dashboardState);

        dashboardViewModel.firePropertyChanged();
// I need to change the active view to the dashboard view
        viewManagerModel.setActiveView(dashboardViewModel.getViewName());

        viewManagerModel.firePropertyChanged();
    }
 // fail view for the presenter which gets called only when the refactor project fails
    public void prepareFailView(String error){
         DashboardState dashboardState = dashboardViewModel.getState();

         dashboardState.setAddProjectError(error);

         dashboardViewModel.firePropertyChanged();
     }
 }


