package app;

import entity.CommonProjectFactory;
import entity.CommonTaskFactory;
import entity.TaskFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_project.AddProjectController;
import interface_adapter.add_project.AddProjectPresenter;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.delete_project.DeleteProjectController;
import interface_adapter.delete_project.DeleteProjectPresenter;
import interface_adapter.delete_project.DeleteProjectViewModel;
import interface_adapter.send_notification.NotificationController;
import interface_adapter.send_notification.NotificationPresenter;
import interface_adapter.set_status.SetStatusController;
import interface_adapter.set_status.SetStatusPresenter;
import use_case.add_project.AddProjectDataAccessInterface;
import use_case.add_project.AddProjectInputBoundary;
import use_case.add_project.AddProjectInteractor;
import use_case.add_project.AddProjectOutputBoundary;
import use_case.delete_project.DeleteProjectDataAccessInterface;
import use_case.delete_project.DeleteProjectInputBoundary;
import use_case.delete_project.DeleteProjectInteractor;
import use_case.delete_project.DeleteProjectOutputBoundary;
import use_case.send_notification.NotificationInputBoundary;
import use_case.send_notification.NotificationInteractor;
import use_case.send_notification.NotificationOutputBoundary;
import use_case.send_notification.NotificationUsersDataAccessInterface;
import use_case.set_status.SetStatusInputBoundary;
import use_case.set_status.SetStatusInteractor;
import use_case.set_status.SetStatusOutputBoundary;
import use_case.set_status.SetStatusUsersDataAccessInterface;
import view.DashboardView;
import entity.ProjectFactory;

import javax.swing.*;
import java.io.IOException;

public class DashboardViewFactory {
    private DashboardViewFactory() {}

    public static DashboardView create(DashboardViewModel dashboardViewModel,
                                       DeleteProjectViewModel deleteProjectViewModel,
                                       ViewManagerModel viewManagerModel,
                                       AddProjectDataAccessInterface addProjectDataAccessInterface,
                                       NotificationUsersDataAccessInterface notificationUsersDataAccessInterface,
                                       DeleteProjectDataAccessInterface deleteProjectDataAccessInterface,
                                       SetStatusUsersDataAccessInterface setStatusUsersDataAccessInterface) {

        try {
            AddProjectController addProjectController = createAddProjectUseCase(dashboardViewModel, addProjectDataAccessInterface);
            NotificationController notificationController = createNotificationUseCase(dashboardViewModel, notificationUsersDataAccessInterface);
            DeleteProjectController deleteProjectController = createDeleteProjectUseCase(deleteProjectViewModel, dashboardViewModel, viewManagerModel, deleteProjectDataAccessInterface);
            SetStatusController setStatusController = createSetStatusUseCase(dashboardViewModel, setStatusUsersDataAccessInterface);
            return new DashboardView(dashboardViewModel, deleteProjectViewModel, addProjectController, notificationController, deleteProjectController, setStatusController);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not instantiate DashboardView.");
        }

        return null;
    }

    private static AddProjectController createAddProjectUseCase(DashboardViewModel dashboardViewModel,
                                                            AddProjectDataAccessInterface addProjectDataAccessInterface) throws IOException {

        // Notice how we pass this method's parameters to the Presenter.
        AddProjectOutputBoundary addProjectOutputBoundary = new AddProjectPresenter(dashboardViewModel);

        ProjectFactory projectFactory = new CommonProjectFactory();
        TaskFactory taskFactory = new CommonTaskFactory();

        AddProjectInputBoundary addProjectInteractor = new AddProjectInteractor(
                addProjectDataAccessInterface, addProjectOutputBoundary, projectFactory, taskFactory);

        return new AddProjectController(addProjectInteractor);
    }
    private static NotificationController createNotificationUseCase(DashboardViewModel dashboardViewModel,
                                                                    NotificationUsersDataAccessInterface notificationUsersDataAccessInterface) throws IOException {
        NotificationOutputBoundary notificationOutputBoundary = new NotificationPresenter(dashboardViewModel);

        NotificationInputBoundary notificationInteractor = new NotificationInteractor(notificationUsersDataAccessInterface, notificationOutputBoundary);

        return new NotificationController(notificationInteractor);
    }
    private static DeleteProjectController createDeleteProjectUseCase(DeleteProjectViewModel deleteprojectViewModel,
                                                                      DashboardViewModel dashboardViewModel,
                                                                      ViewManagerModel viewManagerModel,
                                                                      DeleteProjectDataAccessInterface deleteProjectDataAccessInterface) throws IOException {
        DeleteProjectOutputBoundary deleteProjectOutputBoundary = new DeleteProjectPresenter(deleteprojectViewModel, dashboardViewModel, viewManagerModel);

        DeleteProjectInputBoundary deleteprojectInteractor = new DeleteProjectInteractor(deleteProjectDataAccessInterface, deleteProjectOutputBoundary);

        return new DeleteProjectController(deleteprojectInteractor);
    }
    private static SetStatusController createSetStatusUseCase(DashboardViewModel dashboardViewModel,
                                                                    SetStatusUsersDataAccessInterface setStatusUsersDataAccessInterface) throws IOException {
        SetStatusOutputBoundary setStatusOutputBoundary = new SetStatusPresenter(dashboardViewModel);

        SetStatusInputBoundary setStatusInteractor = new SetStatusInteractor(setStatusUsersDataAccessInterface, setStatusOutputBoundary);

        return new SetStatusController(setStatusInteractor);
    }
}
