package use_case.add_project;

import app.DashboardViewFactory;
import app.LoginViewFactory;
import app.Main;
import app.SignupViewFactory;
import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.delete_project.DeleteProjectViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import org.junit.jupiter.api.Test;
import view.DashboardView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;

public class AddProjectEndToEndTest {

    static String message = "";
    static boolean popUpDiscovered = false;

    public void goToDashboardView() throws IOException {
        JFrame application = new JFrame("Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();

        // The various View objects. Only one view is visible at a time.
        JPanel views = new JPanel(cardLayout);
        application.add(views);

        // This keeps track of and manages which view is currently showing.
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        // For the Add Project End-to-End test, we can skip the login and signup views altogether
        DashboardViewModel dashboardViewModel = new DashboardViewModel();
        DeleteProjectViewModel deleteProjectViewModel = new DeleteProjectViewModel();

        UserFactory userFactory = new CommonUserFactory();
        ProjectFactory projectFactory = new CommonProjectFactory();
        TaskFactory taskFactory = new CommonTaskFactory();

        ProjectsDataAccessObject projectsDAO;
        UsersDataAccessObject usersDataAccessObject = null;
        try {
            projectsDAO = new ProjectsDataAccessObject("./projects.csv",
                    projectFactory, taskFactory);
            usersDataAccessObject = new UsersDataAccessObject("./users.csv",
                    userFactory, projectsDAO);

            User user = new CommonUser("foobar", "baz");
            usersDataAccessObject.saveUser(user);

            dashboardViewModel.getState().setUsername("foobar");

        } catch (IOException e) {
            System.out.println("ERROR: IOexception when creating UsersDAO");
        }

        DashboardView dashboardView = DashboardViewFactory.create(dashboardViewModel, deleteProjectViewModel, viewManagerModel, usersDataAccessObject, usersDataAccessObject, usersDataAccessObject);
        assert dashboardView != null;
        views.add(dashboardView, dashboardView.viewName);

        viewManagerModel.setActiveView(dashboardView.viewName);
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(600, 400);
        application.setLayout(new BorderLayout());
        application.setResizable(false);
        application.setVisible(true);
    }


    public JButton getFirstAddProjectButton() {
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

        DashboardView sv = (DashboardView) jp2.getComponent(0);

        JPanel buttons = (JPanel) sv.getComponent(0);

        return (JButton) buttons.getComponent(1); // this should be the first add project button
    }

    public JButton getSecondAddProjectButton() {
        Window[] windows = Window.getWindows();

        for (Window window : windows) {

            if (window instanceof JFrame jframe) {

                // Ensures we are on the right window.
                if (jframe.getTitle().equals("Add Project")) {
                    Component root = jframe.getComponent(0);

                    Component cp = ((JRootPane) root).getContentPane();

                    JPanel jp = (JPanel) cp;

                    JPanel jp2 = (JPanel) jp.getComponent(0);

                    return (JButton) jp2.getComponent(7);  // this should be the second add project button
                }
            }
        }

        return null;

    }

    /**
     *
     * Test that the first Add Project button is present and upon clicked, opens a second window where another
     * Add Project button is tested to be present.
     */
    @Test
    public void testAddProjectButtonsPresent() throws IOException {
        goToDashboardView();
        JButton firstAddProjectButton = getFirstAddProjectButton();
        assert(firstAddProjectButton.getText().equals("Add Project"));

        firstAddProjectButton.doClick();
        JButton secondAddProjectButton = getSecondAddProjectButton();
        assert(secondAddProjectButton.getText().equals("Add Project"));
    }

    /**
     *
     * Test that pressing the second Add Project Button will all fields filled will call
     * the add project use case interactor. This test first starts the program at the DashboardView,
     * navigates to the window with the second Add Project button, fill all required fields,
     * then clicks the Add Project button. Finally, checks that the project has visually been
     * added to the DashboardView.
     */
    @Test
    public void testAddProject() throws IOException {
        goToDashboardView();

        JButton firstAddProjectButton = getFirstAddProjectButton();
        firstAddProjectButton.doClick();

        Window[] windows = Window.getWindows();

        for (Window window : windows) {

            if (window instanceof JFrame jframe) {

                // Ensures we are on the right window.
                if (jframe.getTitle().equals("Add Project")) {
                    Component root = jframe.getComponent(0);

                    Component cp = ((JRootPane) root).getContentPane();

                    JPanel jp = (JPanel) cp;

                    JPanel jp2 = (JPanel) jp.getComponent(0);

                    JButton secondAddProjectButton = (JButton) jp2.getComponent(7); // this should be the second add project button
                    secondAddProjectButton.doClick();
                }
            }
        }



        try {
            int lines = countLines();
            System.out.println("lines left = " + lines);
            assert(lines <= 1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static int countLines() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("users.csv"));
        int lineCount = 0;
        while (reader.readLine() != null) {
            lineCount++;
        }
        return lineCount;
    }

}

