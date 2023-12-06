package interface_adapter.refactor_project;

import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.delete_project.DeleteProjectState;
import interface_adapter.delete_project.DeleteProjectViewModel;
import use_case.delete_project.DeleteProjectOutputData;
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
        refactorProjectState.setRefactoredProjectId(refactorProjectOutputData.getProjectId());
        refactorProjectState.setRefactoredProjectTasks(refactorProjectOutputData.getTasks());
        refactorProjectViewModel.setState(refactorProjectState);
        refactorProjectViewModel.firePropertyChanged();
        DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.deleteProjectData(refactorProjectOutputData.getProjectId());
        ArrayList<String>project=new ArrayList<String>();
        project.add(refactorProjectOutputData.getProjectId());
        project.add(refactorProjectOutputData.getProjectName());
        project.add(refactorProjectOutputData.getProjectDescription());
        project.add(refactorProjectOutputData.getTasks());
        System.out.println(project);
        dashboardState.addProjectData(project);

        dashboardViewModel.setState(dashboardState);
        dashboardViewModel.firePropertyChanged();

        viewManagerModel.setActiveView(dashboardViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
 public void prepareFailView(String error){
         DashboardState dashboardState = dashboardViewModel.getState();
         dashboardState.setAddProjectError(error);

         dashboardViewModel.firePropertyChanged();
     }
 }


