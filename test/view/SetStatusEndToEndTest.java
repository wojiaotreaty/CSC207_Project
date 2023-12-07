package view;

import app.DashboardViewFactory;
import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.delete_project.DeleteProjectViewModel;
import static org.junit.jupiter.api.Assertions.*;

import interface_adapter.refactor_project.RefactorProjectViewModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SetStatusEndToEndTest {


    public void openProjectPopupView() {
        JFrame application = new JFrame("WorkFlo");
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
        RefactorProjectViewModel refactorProjectViewModel = new RefactorProjectViewModel();

        UserFactory userFactory = new CommonUserFactory();
        ProjectFactory projectFactory = new CommonProjectFactory();
        TaskFactory taskFactory = new CommonTaskFactory();

        ProjectsDataAccessObject projectsDAO;
        UsersDataAccessObject usersDataAccessObject = null;
        try {
            projectsDAO = new ProjectsDataAccessObject("./projects_test.csv",
                    projectFactory, taskFactory);
            usersDataAccessObject = new UsersDataAccessObject("./users_test.csv",
                    userFactory, projectsDAO);


            Task task1 = taskFactory.create("task1", LocalDate.now(), "task1 desc");
            Task task2 = taskFactory.create("task2", LocalDate.now().plusDays(1), "task2 desc");
            task2.setStatus(true);
            Task task3 = taskFactory.create("task3", LocalDate.now().plusDays(1), "task3 desc");
            Task task4 = taskFactory.create("task4", LocalDate.now().plusDays(2), "task4 desc");

            Project project1 = projectFactory.create("1", "project1", "project1 desc", new ArrayList<Task>(java.util.List.of(new Task[]{task1, task2})));
            Project project2 = projectFactory.create("2", "project2", "project2 desc", new ArrayList<Task>(java.util.List.of(new Task[]{task3, task4})));

            User user  = userFactory.create("foo", "baz", new ArrayList<Project>(List.of(new Project[]{project1, project2})));
            usersDataAccessObject.saveUser(user);
            usersDataAccessObject.saveUser(user);

            ArrayList<String> project1ArrayList = new ArrayList<>();
            project1ArrayList.add("1");
            project1ArrayList.add("project1");
            project1ArrayList.add("project1 desc");
            project1ArrayList.add(task1.toString() + "|uwu|" + task2.toString());

            ArrayList<String> project2ArrayList = new ArrayList<>();
            project2ArrayList.add("2");
            project2ArrayList.add("project2");
            project2ArrayList.add("project2 desc");
            project2ArrayList.add(task2.toString() + "|uwu|" + task3.toString());

            dashboardViewModel.getState().addProjectData(project1ArrayList);
            dashboardViewModel.getState().addProjectData(project2ArrayList);


            dashboardViewModel.getState().setUsername("foo");

        } catch (IOException e) {
            System.out.println("ERROR: IOexception when creating UsersDAO");
        }

        DashboardView dashboardView = DashboardViewFactory.create(dashboardViewModel, deleteProjectViewModel, refactorProjectViewModel, viewManagerModel, usersDataAccessObject, usersDataAccessObject, usersDataAccessObject, usersDataAccessObject, usersDataAccessObject);

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

        DashboardView.ProjectPanel firstProjectPanel = (DashboardView.ProjectPanel) getFirstProjectPanel();

        MouseEvent me = new MouseEvent(
                firstProjectPanel, 0, 0, 0, 50, 50, 1, true);
        for (MouseListener ml: firstProjectPanel.getMouseListeners()){
            ml.mouseClicked(me);
        }


    }
    private JFrame getProjectPopup(){
        JFrame projectPopup = null;
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JFrame jframe) {
                // Ensures we are on the right window.
                if (jframe.getTitle().equals("Project") && jframe.isDisplayable()) {
                    projectPopup = (JFrame) window;
                }
            }
        }
        return projectPopup;
    }


    private JPanel getFirstProjectPanel() {
        JFrame app = null;
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JFrame) {
                if (!Objects.equals(((JFrame) window).getTitle(), "Add Project")) {
                    app = (JFrame) window;
                }
            }
        }

        assertNotNull(app); // found the window?

        Component root = app.getComponent(0);

        Component cp = ((JRootPane) root).getContentPane();

        JPanel jp = (JPanel) cp;

        JPanel jp2 = (JPanel) jp.getComponent(0);

        DashboardView dv = (DashboardView) jp2.getComponent(0);

        JScrollPane projectScrollPane = (JScrollPane) dv.getComponent(1);
        JViewport projectScrollPaneViewPort = (JViewport) projectScrollPane.getComponent(0);
        JPanel projectPanel = (JPanel) projectScrollPaneViewPort.getComponent(0);

        return (JPanel) projectPanel.getComponent(0); // this should be the first project panel
    }



    private static JPanel getTaskPane(JFrame projectPopup, int num) {
        JRootPane root = (JRootPane) projectPopup.getComponent(0);
        JPanel contentPane = (JPanel) root.getContentPane();
        JScrollPane scrollPanel = (JScrollPane) contentPane.getComponent(1);
        JViewport viewport = scrollPanel.getViewport();
        JPanel view = (JPanel) viewport.getView();

        return (JPanel) view.getComponent(num);
    }


    @Test
    public void TestSetTrueNotification() {
        openProjectPopupView();
        JFrame projectPopup = getProjectPopup();
        JPanel taskPane = getTaskPane(projectPopup, 2);

        JLabel taskLabel = (JLabel) ((JPanel) taskPane.getComponent(0)).getComponent(1);
        assert taskLabel.getText().equals("task1");

        JCheckBox checkBox = (JCheckBox) taskPane.getComponent(2);

        assert(!checkBox.isSelected());

        checkBox.doClick();

        assert(checkBox.isSelected());
    }

    @Test
    public void TestSetFalseNotification() {
        openProjectPopupView();
        JFrame projectPopup = getProjectPopup();
        JPanel taskPane = getTaskPane(projectPopup, 2);

        JLabel taskLabel = (JLabel) ((JPanel) taskPane.getComponent(0)).getComponent(1);
        assert taskLabel.getText().equals("task1");

        JCheckBox checkBox = (JCheckBox) taskPane.getComponent(2);

        assert (!checkBox.isSelected());

        checkBox.doClick();
        checkBox.doClick();

        assert (!checkBox.isSelected());
    }


    @AfterEach
    public void cleanUp(){
        File testProjectsDatabase = new File("./projects_test.csv");
        if (!testProjectsDatabase.delete()){
            System.out.println("./projects_test.csv did not delete properly after testing.");
        }
        File testUsersDatabase = new File("./users_test.csv");
        if (!testUsersDatabase.delete()){
            System.out.println("./users_test.csv did not delete properly after testing.");
        }
    }
}