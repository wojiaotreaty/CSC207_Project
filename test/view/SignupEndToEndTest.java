package view;

import app.DashboardViewFactory;
import app.LoginViewFactory;
import app.SignupViewFactory;
import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.delete_project.DeleteProjectViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import org.junit.After;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SignupEndToEndTest {
    static String message;
    static boolean popUpDiscovered;

    // test for window y
    // test for button's existence y
    // test to click 'Go to Signup' button y
    // test that view changed to signupview y
    // test for user's inputs:
        /*
        1. user inputs 2 different passwords
        2. user inputs username that already exists in the system
        3. user successfully signs up
         */
    // test to see that view changes back to loginview
    // check that username field is auto filled up from signup
    private String PROJECTS_PATH = "./projects.csv";
    private String USERS_PATH = "./users.csv";
    private ViewManagerModel viewManagerModel;
    public void SetUpSignupViewTest() {
        JFrame application = new JFrame("Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();

        // The various View objects. Only one view is visible at a time.
        JPanel views = new JPanel(cardLayout);
        application.add(views);

        // This keeps track of and manages which view is currently showing.
        this.viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        DashboardViewModel dashboardViewModel = new DashboardViewModel();
        SignupViewModel signupViewModel = new SignupViewModel();
        LoginViewModel loginViewModel = new LoginViewModel();
        DeleteProjectViewModel deleteProjectViewModel = new DeleteProjectViewModel();

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

        SignupView signupView = SignupViewFactory.create(
                viewManagerModel, loginViewModel, signupViewModel,
                usersDataAccessObject);
        views.add(signupView, signupView.viewName);

        LoginView loginView = LoginViewFactory.create(
                viewManagerModel, loginViewModel, dashboardViewModel, usersDataAccessObject);
        views.add(loginView, loginView.viewName);

        DashboardView dashboardView = DashboardViewFactory.create(
                dashboardViewModel,
                deleteProjectViewModel,
                viewManagerModel,
                usersDataAccessObject,
                usersDataAccessObject,
                usersDataAccessObject,
                usersDataAccessObject);
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
    public JButton getButton() {
        JFrame app = null;
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JFrame) {
                app = (JFrame) window;
            }
        }

        assertNotNull(app); // found the window?

        Component root = app.getComponent(0);

        Component cp = ((JRootPane) root).getContentPane();

        JPanel jp = (JPanel) cp;

        JPanel jp2 = (JPanel) jp.getComponent(0);

        LoginView lv = (LoginView) jp2.getComponent(1);

        JPanel buttons = (JPanel) lv.getComponent(5);

        return (JButton) buttons.getComponent(1); // this should be the Go To Signup button
    }

    @org.junit.Test
    public void testSignupButtonPresent() {
        SetUpSignupViewTest();
        JButton button = getButton();
        assert(button.getText().equals("Go to Signup"));
    }

    @org.junit.Test
    public void testSignupButtonChangeView() {
        SetUpSignupViewTest();
        JButton button = getButton();
        button.doClick();

        // check that the view moves to signup view
        String currentView = viewManagerModel.getActiveView();

        assertEquals("Signup Page", currentView);
    }

    @org.junit.Test
    public void testSignup() {
        SetUpSignupViewTest();
        JButton button = getButton();
        button.doClick();
        message = "";
        popUpDiscovered = false;
        JFrame app = null;
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JFrame) {
                app = (JFrame) window;
            }
        }
        Component root = app.getComponent(0);
        Component cp = ((JRootPane) root).getContentPane();
        JPanel jp = (JPanel) cp;
        JPanel jp2 = (JPanel) jp.getComponent(0);
        SignupView sv = (SignupView) jp2.getComponent(0);
        LabelTextPanel username = (LabelTextPanel) sv.getComponent(1);
        LabelTextPanel password = (LabelTextPanel) sv.getComponent(2);
        LabelTextPanel repeatPassword = (LabelTextPanel) sv.getComponent(3);

        JTextField usernameInputField = (JTextField) username.getComponent(1);
        usernameInputField.setText("Daniel");

        JTextField passwordInputField = (JTextField) password.getComponent(1);
        passwordInputField.setText("Password");

        JTextField repeatPasswordInputField = (JTextField) repeatPassword.getComponent(1);
        repeatPasswordInputField.setText("Password");

        // Now I want to click signup.

        JPanel buttons = (JPanel) sv.getComponent(4);
        JButton signup = (JButton) buttons.getComponent(0);
        signup.doClick();

        // Check that I successfully moved back to login view

        String currentView = viewManagerModel.getActiveView();

        assertEquals("log in", currentView);
    }

    @After
    public void deleteTestDAOs(){
        File testProjectsDatabase = new File(PROJECTS_PATH);
        if (!testProjectsDatabase.delete()){
            System.out.println(PROJECTS_PATH + " did not delete properly after testing.");
        }
        File testUsersDatabase = new File(USERS_PATH);
        if (!testUsersDatabase.delete()){
            System.out.println(USERS_PATH + " did not delete properly after testing.");
        }
    }

}
