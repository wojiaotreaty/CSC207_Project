package use_case.add_project;

import static org.junit.jupiter.api.Assertions.*;

import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AddProjectIntegrationTest {
    private final String PROJECTS_PATH = "./projects_test.csv";
    private final String USERS_PATH = "./users_test.csv";
    private final CommonProjectFactory PROJECT_FACTORY = new CommonProjectFactory();
    private final CommonTaskFactory TASK_FACTORY = new CommonTaskFactory();
    private final CommonUserFactory USER_FACTORY = new CommonUserFactory();
    private UsersDataAccessObject usersDAO;

    @BeforeEach
    public void init() {
        try {
            ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                    PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
            this.usersDAO = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);

            User user = new CommonUser("foobar", "baz");
            usersDAO.saveUser(user);

        } catch (IOException error){
            System.out.println("ERROR: IOexception when creating UsersDAO");
        }
    }

    @Test
    void successTest() {
        AddProjectInputData inputData = new AddProjectInputData(
                "CSC207 Group Project",
                "Create an application, which makes use of Clean Architecture and SOLID Design principles, " +
                        "which serves as an aid for time management and organizing deadlines. Upon adding a project, the " +
                        "application will split the project into smaller deadlines which help the user to stay on track.",
                "2023-12-04",
                "foobar");

        AddProjectOutputBoundary successPresenter = new AddProjectOutputBoundary() {
            @Override
            public void prepareSuccessView(AddProjectOutputData addProjectOutputData) {
                assertEquals("1", addProjectOutputData.getProject().get(0));
                assertEquals("CSC207 Group Project", addProjectOutputData.getProject().get(1));
                assertEquals("Create an application, which makes use of Clean Architecture and SOLID Design principles, " +
                                "which serves as an aid for time management and organizing deadlines. Upon adding a project, the " +
                                "application will split the project into smaller deadlines which help the user to stay on track.",
                        addProjectOutputData.getProject().get(2));

                try {
                    ArrayList<ArrayList<String>> projectTasks = new ArrayList<>();

                    String[] tasks = addProjectOutputData.getProject().get(3).split("[|]uwu[|]");
                    for (String task : tasks) {
                        String[] taskComponents = task.split("`");

                        projectTasks.add(new ArrayList<>(Arrays.asList(taskComponents)));
                    }
                } catch (Error e) {
                    fail("Text completion generated did not match the given restrictions.");
                }

                assertEquals(usersDAO.getUser("foobar").getProjects().size(), 1);

                Project addedProject = usersDAO.getUser("foobar").getProjects().get(0);
                assertEquals(addedProject.getProjectId(), "1");
                assertEquals(addedProject.getProjectDescription(), "Create an application, which makes use of " +
                        "Clean Architecture and SOLID Design principles, which serves as an aid for time management and " +
                        "organizing deadlines. Upon adding a project, the application will split the project into " +
                        "smaller deadlines which help the user to stay on track.");
                assertEquals(addedProject.getProjectName(), "CSC207 Group Project");
                assertNotNull(addedProject.getTasks());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        AddProjectInputBoundary addProjectInteractor = new AddProjectInteractor(
                this.usersDAO,
                successPresenter,
                new CommonProjectFactory(),
                new CommonTaskFactory()
        );

        addProjectInteractor.execute(inputData);
    }

    @AfterEach
    public void cleanUp(){
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