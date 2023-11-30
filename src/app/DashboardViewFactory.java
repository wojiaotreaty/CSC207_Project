package app;

import interface_adapter.dashboard.DashboardViewModel;
import view.DashboardView;

import javax.swing.*;
import java.io.IOException;

public class DashboardViewFactory {
    private DashboardViewFactory() {}

    public static DashboardView create(DashboardViewModel dashboardViewModel,
                                       AddProjectDataAccessInterface addProjectDataAccessInterface,
                                       AddProjectOutputBoundary addProjectOutputBoundary) {

        try {
            AddProjectController addProjectController = createAddProjectUseCase(dashboardViewModel, addProjectDataAccessInterface, addProjectOutputBoundary);
            return new DashboardView(dashboardViewModel, addProjectController);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not instantiate DashboardView.");
        }

        return null;
    }

    private static AddProjectController createAddProjectUseCase(DashboardViewModel dashboardViewModel,
                                                            AddProjectDataAccessInterface addProjectDataAccessInterface,
                                                            AddProjectOutputBoundary addProjectOutputBoundary) throws IOException {

        // Notice how we pass this method's parameters to the Presenter.
        AddProjectOutputBoundary addProjectOutputBoundary = new AddProjectPresenter(dashboardViewModel);

        ProjectFactory projectFactory = new ProjectFactory();

        AddProjectInputBoundary addProjectInteractor = new AddProjectInteractor(
                addProjectDataAccessInterface, addProjectOutputBoundary, projectFactory);

        return new AddProjectController(addProjectInteractor);
    }
}
