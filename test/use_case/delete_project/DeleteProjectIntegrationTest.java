package use_case.delete_project;

import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DeleteProjectIntegrationTest {

    private final UserFactory userFactory = new CommonUserFactory();
    private final ProjectFactory projectFactory = new CommonProjectFactory();
    private final TaskFactory taskFactory = new CommonTaskFactory();
    private UsersDataAccessObject usersDataAccessObject;

    static User testUser;

    static final String USERNAME = "username1";
    static final String PROJECT_ID = "32";
    static final String PROJECT_NAME = "foobarProjName";

    private final String PROJECTS_PATH = "./projects_test.csv";
    private final String USERS_PATH = "./users_test.csv";

    @Before
    public void init(){
        try {
            ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(PROJECTS_PATH, projectFactory, taskFactory);
            this.usersDataAccessObject = new UsersDataAccessObject(USERS_PATH, userFactory, projectsDAO);
        } catch (IOException e){
            throw new RuntimeException("ERROR: IOexception when creating projectsDAO and UsersDAO");
        }

        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(this.taskFactory.create(
                "foobarTaskName1", LocalDate.parse("2022-12-22"), "foobarTaskDesc1"));
        taskList.add(this.taskFactory.create(
                "foobarTaskName2", LocalDate.parse("2022-12-23"), "foobarTaskDesc2"));

        testUser = this.userFactory.create(USERNAME, "password1", new ArrayList<>());
        usersDataAccessObject.saveUser(testUser);

        testUser.addProject(this.projectFactory.create(PROJECT_ID, PROJECT_NAME,
                "foobarProjDesc", taskList));
        usersDataAccessObject.saveUser(testUser);
    }

    @Test
    public void successTest() {

        DeleteProjectInputData inputData = new DeleteProjectInputData(USERNAME, PROJECT_ID);

        DeleteProjectOutputBoundary successPresenter = deleteProjectOutputData -> {
            assertEquals(deleteProjectOutputData.getProjectId(), DeleteProjectInteractorUnitTest.PROJECT_ID);
            assertEquals(deleteProjectOutputData.getProjectName(), DeleteProjectInteractorUnitTest.PROJECT_NAME);

            User resultUser = usersDataAccessObject.getUser(DeleteProjectInteractorUnitTest.USERNAME);
            assertNotNull("ERROR: user not found.", resultUser);
            for (Project project : resultUser.getProjects()){
                assertNotEquals("ERROR: Project not deleted.",
                        DeleteProjectInteractorUnitTest.PROJECT_ID, project.getProjectId());
            }

        };

        DeleteProjectInputBoundary testDeleteProjectInteractor =
                new DeleteProjectInteractor(usersDataAccessObject, successPresenter);
        testDeleteProjectInteractor.execute(inputData);
    }

    @After
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

