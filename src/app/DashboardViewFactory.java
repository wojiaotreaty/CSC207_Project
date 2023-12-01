package app;

import entity.CommonProjectFactory;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.send_notification.NotificationController;
import interface_adapter.send_notification.NotificationPresenter;
import use_case.send_notification.NotificationInputBoundary;
import use_case.send_notification.NotificationInteractor;
import use_case.send_notification.NotificationOutputBoundary;
import use_case.send_notification.NotificationUsersDataAccessInterface;
import view.DashboardView;
import entity.ProjectFactory;

import javax.swing.*;
import java.io.IOException;

public class DashboardViewFactory {
    private DashboardViewFactory() {}

    public static DashboardView create(DashboardViewModel dashboardViewModel,
                                       AddProjectDataAccessInterface addProjectDataAccessInterface,
                                       NotificationUsersDataAccessInterface notificationUsersDataAccessInterface) {

        try {
            AddProjectController addProjectController = createAddProjectUseCase(dashboardViewModel, addProjectDataAccessInterface);
            NotificationController notificationController = createNotificationUseCase(dashboardViewModel, notificationUsersDataAccessInterface);
            return new DashboardView(dashboardViewModel, addProjectController, notificationController);
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

        AddProjectInputBoundary addProjectInteractor = new AddProjectInteractor(
                addProjectDataAccessInterface, addProjectOutputBoundary, projectFactory);

        return new AddProjectController(addProjectInteractor);
    }
    private static NotificationController createNotificationUseCase(DashboardViewModel dashboardViewModel,
                                                                    NotificationUsersDataAccessInterface notificationUsersDataAccessInterface) throws IOException {
        NotificationOutputBoundary notificationOutputBoundary = new NotificationPresenter(dashboardViewModel);

        NotificationInputBoundary notificationInteractor = new NotificationInteractor(notificationUsersDataAccessInterface, notificationOutputBoundary);

        return new NotificationController(notificationInteractor);
    }
}
