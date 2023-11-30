package app;

import interface_adapter.dashboard.DashboardViewModel;

import javax.swing.*;
import java.awt.*;

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



        ProjectsDataAccessObject(String projectsCsvPath, ProjectFactory projectFactory,
                TaskFactory taskFactory);

        UsersDataAccessObject(String usersCsvPath, UserFactory userFactory, ProjectsDataAccessObject projectsDAO)
    }
}
