package interface_adapter.open_project;

import use_case.open_project.OpenProjectOutputBoundary;
import use_case.open_project.OpenProjectOutputData;

public class OpenProjectPresenter implements OpenProjectOutputBoundary {
    private final ProjectViewModel projectViewModel;
    private ViewManagerModel viewManagerModel;
    @Override
    public void prepareView(OpenProjectOutputData openProjectOutputData) {
        ProjectState projectState = ProjectViewModel.getState();
        projectState.setProjectTitle(openProjectOutputData.getName());
        projectState.setProjectDescription(openProjectOutputData.getDescription());
        projectState.setProjectId(openProjectOutputData.getProjectId());
        projectState.setTasks(openProjectOutputData.getTasks());
        viewManagerModel.setActiveView(ProjectViewModel.getViewName());
        projectViewModel.firePropertyChanged();
    }
}
