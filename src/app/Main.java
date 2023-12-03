package app;

import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.delete_project.DeleteProjectViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.refactor_project.RefactorProjectViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginDataAccessInterface;
import use_case.signup.SignupDataAccessInterface;
import view.DashboardView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        JFrame application = new JFrame("Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();

        // The various View objects. Only one view is visible at a time.
        JPanel views = new JPanel(cardLayout);
        application.add(views);

        // This keeps track of and manages which view is currently showing.
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        DashboardViewModel dashboardViewModel = new DashboardViewModel();
        SignupViewModel signupViewModel = new SignupViewModel();
        LoginViewModel loginViewModel = new LoginViewModel();
        DeleteProjectViewModel deleteProjectViewModel = new DeleteProjectViewModel();
        RefactorProjectViewModel refactorProjectViewModel = new RefactorProjectViewModel();
        UserFactory userFactory = new CommonUserFactory();
        ProjectFactory projectFactory = new CommonProjectFactory();
        TaskFactory taskFactory = new CommonTaskFactory();


        ProjectsDataAccessObject projectsDAO;
        UsersDataAccessObject usersDataAccessObject;
        try {
            projectsDAO = new ProjectsDataAccessObject("./projects.csv",
                    projectFactory, taskFactory);
            usersDataAccessObject = new UsersDataAccessObject("./users.csv",
                    userFactory, projectsDAO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SignupView signupView = SignupViewFactory.create(viewManagerModel, loginViewModel, signupViewModel,
                usersDataAccessObject);
        views.add(signupView, signupView.viewName);

        LoginView loginView = LoginViewFactory.create(viewManagerModel, loginViewModel, dashboardViewModel, usersDataAccessObject);
        views.add(loginView, loginView.viewName);

        DashboardView dashboardView = DashboardViewFactory.create(dashboardViewModel, deleteProjectViewModel,refactorProjectViewModel, viewManagerModel, usersDataAccessObject, usersDataAccessObject, usersDataAccessObject,usersDataAccessObject);
        views.add(dashboardView, dashboardView.viewName);


        viewManagerModel.setActiveView(loginView.viewName);
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(600, 400);
        application.setLayout(new BorderLayout());
        application.setResizable(false);
        application.setVisible(true);
        }
}
