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
import interface_adapter.refactor_project.RefactorProjectController;
import interface_adapter.refactor_project.RefactorProjectPresenter;
import interface_adapter.refactor_project.RefactorProjectViewModel;
import interface_adapter.send_notification.NotificationController;
import interface_adapter.send_notification.NotificationPresenter;
import use_case.add_project.AddProjectDataAccessInterface;
import use_case.add_project.AddProjectInputBoundary;
import use_case.add_project.AddProjectInteractor;
import use_case.add_project.AddProjectOutputBoundary;
import use_case.delete_project.DeleteProjectDataAccessInterface;
import use_case.delete_project.DeleteProjectInputBoundary;
import use_case.delete_project.DeleteProjectInteractor;
import use_case.delete_project.DeleteProjectOutputBoundary;
import use_case.refactor_project.RefactorProjectDataAccessInterface;
import use_case.refactor_project.RefactorProjectInputBoundary;
import use_case.refactor_project.RefactorProjectInteractor;
import use_case.refactor_project.RefactorProjectOutputBoundary;
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
                                       DeleteProjectViewModel deleteProjectViewModel,
                                       RefactorProjectViewModel refactorProjectViewModel,
                                       ViewManagerModel viewManagerModel,
                                       AddProjectDataAccessInterface addProjectDataAccessInterface,
                                       NotificationUsersDataAccessInterface notificationUsersDataAccessInterface,
                                       DeleteProjectDataAccessInterface deleteProjectDataAccessInterface,RefactorProjectDataAccessInterface refactorProjectDataAccessInterface) {

        try {
            AddProjectController addProjectController = createAddProjectUseCase(dashboardViewModel, addProjectDataAccessInterface);
            NotificationController notificationController = createNotificationUseCase(dashboardViewModel, notificationUsersDataAccessInterface);
            DeleteProjectController deleteProjectController = createDeleteProjectUseCase(deleteProjectViewModel, dashboardViewModel, viewManagerModel, deleteProjectDataAccessInterface);
            RefactorProjectController refactorProjectController = createRefactorProjectUseCase(refactorProjectViewModel, dashboardViewModel, viewManagerModel, refactorProjectDataAccessInterface);
            return new DashboardView(dashboardViewModel, deleteProjectViewModel, refactorProjectController, addProjectController, notificationController, deleteProjectController);
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
    private static RefactorProjectController createRefactorProjectUseCase(RefactorProjectViewModel refactorprojectViewModel,
                                                                          DashboardViewModel dashboardViewModel,
                                                                          ViewManagerModel viewManagerModel,
                                                                          RefactorProjectDataAccessInterface refactorProjectDataAccessInterface) throws IOException {
        RefactorProjectOutputBoundary refactorProjectOutputBoundary = new RefactorProjectPresenter(refactorprojectViewModel, dashboardViewModel, viewManagerModel);
        ProjectFactory projectFactory = new CommonProjectFactory();
        TaskFactory taskFactory = new CommonTaskFactory();

        RefactorProjectInputBoundary refactorProjectInteractor = new RefactorProjectInteractor(refactorProjectDataAccessInterface, refactorProjectOutputBoundary,projectFactory,taskFactory);

        return new RefactorProjectController(refactorProjectInteractor);
    }
}
