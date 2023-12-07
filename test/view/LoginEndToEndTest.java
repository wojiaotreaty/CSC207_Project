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
import interface_adapter.refactor_project.RefactorProjectViewModel;
import interface_adapter.signup.SignupViewModel;
import org.junit.After;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginEndToEndTest {
    private UsersDataAccessObject usersDataAccessObject;
    private ViewManagerModel viewManagerModel;
    private UserFactory userFactory;
    private String PROJECTS_PATH = "./projects_test.csv";
    private String USERS_PATH = "./users_test.csv";

//    public void addUser() {
//        UserFactory userFactory = new CommonUserFactory();
//        ProjectFactory projectFactory = new CommonProjectFactory();
//        TaskFactory taskFactory = new CommonTaskFactory();
//
////        Initialize the testUser
//        User testUser = userFactory.create("Daniel", "Password", new ArrayList<>());
//
////        Create the DAOs
//        try {
//            ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(PROJECTS_PATH,
//                    projectFactory, taskFactory);
//           usersDataAccessObject = new UsersDataAccessObject(USERS_PATH, userFactory, projectsDAO);
//        } catch (IOException e) {
//            System.out.println("ERROR: IOexception when creating UsersDAO");
//        }
//        usersDataAccessObject.saveUser(testUser);
//    }
//    @Before
    public void setUpLoginView() {
        JFrame application = new JFrame("WorkFlo");
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
        RefactorProjectViewModel refactorProjectViewModel = new RefactorProjectViewModel();

        userFactory = new CommonUserFactory();
        ProjectFactory projectFactory = new CommonProjectFactory();
        TaskFactory taskFactory = new CommonTaskFactory();


        ProjectsDataAccessObject projectsDAO;

        try {
            projectsDAO = new ProjectsDataAccessObject(PROJECTS_PATH,
                    projectFactory, taskFactory);
            usersDataAccessObject = new UsersDataAccessObject(USERS_PATH,
                    userFactory, projectsDAO);
            User testUser = userFactory.create("Daniel", "Password", new ArrayList<>());
            usersDataAccessObject.saveUser(testUser);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SignupView signupView = SignupViewFactory.create(viewManagerModel, loginViewModel, signupViewModel,
                usersDataAccessObject);
        views.add(signupView, signupView.viewName);

        LoginView loginView = LoginViewFactory.create(viewManagerModel, loginViewModel, dashboardViewModel, usersDataAccessObject);
        views.add(loginView, loginView.viewName);

        DashboardView dashboardView = DashboardViewFactory.create(dashboardViewModel, deleteProjectViewModel, refactorProjectViewModel, viewManagerModel, usersDataAccessObject, usersDataAccessObject, usersDataAccessObject, usersDataAccessObject, usersDataAccessObject);
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

    @Test
    public void loginUserTest() {
        setUpLoginView();
        Window[] windows = Window.getWindows();
        JFrame app =null;
        for (Window window : windows) {
            if (window instanceof JFrame){
                app = (JFrame) window;
            }
        }
        Component root = app.getComponent(0);

        Component cp = ((JRootPane) root).getContentPane();

        JPanel jp = (JPanel) cp;
        JPanel jp2 = (JPanel) jp.getComponent(0);

        LoginView lv = (LoginView) jp2.getComponent(1);
        LabelTextPanel username = (LabelTextPanel) lv.getComponent(1);
        LabelTextPanel password = (LabelTextPanel) lv.getComponent(3);

        // username field
        JTextField usernameInputField = (JTextField) username.getComponent(1);
        usernameInputField.setText("Daniel");
//        System.out.println(lv.getLoginViewModel().getState().getUsername());

        // password field
        JTextField passwordInputField = (JTextField) password.getComponent(1);
        passwordInputField.setText("Password");
//        System.out.println(lv.getLoginViewModel().getState().getPassword());

        JPanel buttons = (JPanel) lv.getComponent(5);
        JButton login = (JButton) buttons.getComponent(0); // login button
        assertEquals(login.getText(),"Log in");

        // input information
//        System.out.println(usernameInputField.getText());
//        System.out.println(passwordInputField.getText());
//        System.out.println(usersDataAccessObject.getUser("Daniel"));
        login.doClick();
//        System.out.println(usernameInputField.getText());
//        System.out.println(passwordInputField.getText());

        String currentView = viewManagerModel.getActiveView();
        assertEquals("Project Dashboard", currentView); // correct login should move user to dashboard view

    }
    @After
    public void deleteTestDAOs() {
        File testProjectsDatabase = new File(PROJECTS_PATH);
        if (!testProjectsDatabase.delete()) {
            System.out.println(PROJECTS_PATH + " did not delete properly after testing.");
        }
        File testUsersDatabase = new File(USERS_PATH);
        if (!testUsersDatabase.delete()) {
            System.out.println(USERS_PATH + " did not delete properly after testing.");
        }
    }

    private JButton getsignupButton() {
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

        return (JButton) buttons.getComponent(1);
    }
}
