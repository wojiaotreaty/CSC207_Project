package view;

import app.DashboardViewFactory;
import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.delete_project.DeleteProjectViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RefactorProjectEndtoEndTest {
    private final String USERNAME = "username1";
    private final String PROJECT_ID = "32";
    private final String PROJECT_NAME = "foobarProjName";

    private UsersDataAccessObject usersDataAccessObject;

    private ArrayList<String> projectData;

    private DashboardViewModel dashboardViewModel;

    @BeforeEach
    public void init(){
//        Create the factories
        UserFactory userFactory = new CommonUserFactory();
        ProjectFactory projectFactory = new CommonProjectFactory();
        TaskFactory taskFactory = new CommonTaskFactory();

//        Initialize the testUser
        User testUser = userFactory.create(USERNAME, "password1", new ArrayList<>());

//        Create the DAOs
        try {
            ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject("./projects_test.csv",
                    projectFactory, taskFactory);
            this.usersDataAccessObject = new UsersDataAccessObject("./users_test.csv",
                    userFactory, projectsDAO);
        } catch (IOException e) {
            System.out.println("ERROR: IOexception when creating UsersDAO");
        }

//        Save the testUser (so that it exists)
        this.usersDataAccessObject.saveUser(testUser);

//        Add the project to be refactored to the user and save it
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(taskFactory.create(
                "foobarTaskName1", LocalDate.parse("2023-12-22"), "foobarTaskDesc1"));
        taskList.get(0).setStatus(true);
        taskList.add(taskFactory.create(
                "foobarTaskName2", LocalDate.parse("2023-12-23"), "foobarTaskDesc2"));
        taskList.add(taskFactory.create(
                "foobarTaskName3", LocalDate.parse("2023-12-24"), "foobarTaskDesc3"));
        testUser.addProject(projectFactory.create(PROJECT_ID, PROJECT_NAME,
                "foobarProjDesc", taskList));

        this.usersDataAccessObject.saveUser(testUser);

        this.projectData = new ArrayList<>();
        this.projectData.add(PROJECT_ID);
        this.projectData.add(PROJECT_NAME);
        this.projectData.add("foobarProjDesc");
        StringBuilder taskString = new StringBuilder();
        for (Task task : taskList) {
            taskString.append(task.toString()).append("|uwu|");
        }
        this.projectData.add(String.valueOf(taskString));
    }

    private void goToDashboardView() {
        JFrame application = new JFrame("Dashboard Example");

        CardLayout cardLayout = new CardLayout();
        // The various View objects. Only one view is visible at a time.
        JPanel views = new JPanel(cardLayout);
        application.add(views);

        // This keeps track of and manages which view is currently showing.
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        // For the Delete Project End-to-End test, we skip the login and signup views
        dashboardViewModel = new DashboardViewModel();
        dashboardViewModel.getState().setUsername(USERNAME);
        dashboardViewModel.getState().addProjectData(projectData);

        DeleteProjectViewModel deleteProjectViewModel = new DeleteProjectViewModel();
        DashboardView dashboardView = DashboardViewFactory.create(dashboardViewModel, deleteProjectViewModel, viewManagerModel,
                usersDataAccessObject, usersDataAccessObject, usersDataAccessObject,usersDataAccessObject,usersDataAccessObject);
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
    private JPanel getFirstProjectPanel() {
        JFrame dashboardWindow = null;
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JFrame jframe) {
                if (jframe.getTitle().equals("Dashboard Example")) {
                    dashboardWindow = jframe;
                }
            }
        }

        assertNotNull(dashboardWindow); // found the window?

        Component root = dashboardWindow.getComponent(0);
        Component cp = ((JRootPane) root).getContentPane();
        JPanel jp = (JPanel) cp;
        JPanel views = (JPanel) jp.getComponent(0); //this returns views

        DashboardView dv = (DashboardView) views.getComponent(0);
        JScrollPane projectScrollPane = (JScrollPane) dv.getComponent(1);
        JViewport projectScrollPaneViewPort = (JViewport) projectScrollPane.getComponent(0);
        JPanel projectPanels = (JPanel) projectScrollPaneViewPort.getComponent(0);

        return (JPanel) projectPanels.getComponent(0); // this should be the first project panel
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
    private JButton getRefactorProjectButton(JFrame projPopup) {

        assert(projPopup.getTitle().equals("Project"));

        JRootPane root = (JRootPane) projPopup.getComponent(0);
        JPanel contentPane = (JPanel) root.getContentPane();
        JPanel buttonPanel = (JPanel) contentPane.getComponent(2);
        JButton refactorButton = (JButton) buttonPanel.getComponent(0);

        assertEquals("Refactor Project Deadlines", refactorButton.getText());
        return refactorButton;
    }

    /**
     * Test that the Refactor Project button is present in the project popup window.
     */
    @Test
    public void testRefactorProjectButtonPresent() {
        goToDashboardView();
        JPanel firstProjectPanel = getFirstProjectPanel();
        assertNotNull(firstProjectPanel);

        MouseEvent me = new MouseEvent(
                firstProjectPanel, 0, 0, 0, 50, 50, 1, true);
        for(MouseListener ml: firstProjectPanel.getMouseListeners()){
            ml.mouseClicked(me);
        }

        JFrame projectPopup = getProjectPopup();
        assertNotNull(projectPopup);

        JButton refactorProjectButton = getRefactorProjectButton(projectPopup);
        assertNotNull(refactorProjectButton);
    }

    /**
     //     * Test that clicking the Refactor Project button refreshes the original project pop up.
     //     * This test navigates to an existing project popup, then clicks the Refactor Project button.
     //     */
    @Test
    public void testRefactorProjectProjectPopup() {
        goToDashboardView();
        DashboardView.ProjectPanel firstProjectPanel = (DashboardView.ProjectPanel) getFirstProjectPanel();

        MouseEvent me = new MouseEvent(
                firstProjectPanel, 0, 0, 0, 50, 50, 1, true);
        for (MouseListener ml: firstProjectPanel.getMouseListeners()){
            ml.mouseClicked(me);
        }

        JFrame projectPopup = getProjectPopup();
        JButton refactorProjectButton = getRefactorProjectButton(projectPopup);
        refactorProjectButton.doClick();
        JFrame newProjectPopup = getProjectPopup();
        assertNotNull(newProjectPopup);
        assert(newProjectPopup.getTitle().equals("Project"));
        Component[] task_components=newProjectPopup.getComponents();
        JRootPane first_component =(JRootPane)task_components[0];
        JLabel name = (JLabel)first_component.getContentPane().getComponent(0);
        assertEquals(name.getText(),PROJECT_NAME);
    }

    public void cleanUp() {
        File testProjectsDatabase = new File("./projects_test.csv");
        if (!testProjectsDatabase.delete()) {
            System.out.println("./projects_test.csv did not delete properly after testing.");
        }
        File testUsersDatabase = new File("./users_test.csv");
        if (!testUsersDatabase.delete()) {
            System.out.println("./users_test.csv did not delete properly after testing.");
        }

        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window.isDisplayable()) {
                window.dispose();
            }
        }
    }
}
