package use_case.login;

import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class LoginInteractorIntegrationTest {
    private final String PROJECTS_PATH = "./projects_test.csv";
    private final String USERS_PATH = "./users_test.csv";
    private final UserFactory USER_FACTORY = new CommonUserFactory();
    private final ProjectFactory PROJECT_FACTORY = new CommonProjectFactory();
    private final TaskFactory TASK_FACTORY = new CommonTaskFactory();

    @Test
    public void successTest() throws IOException {
        LoginInputData inputData = new LoginInputData("Daniel", "Password");
        ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
        LoginDataAccessInterface userDAO = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);

        // Creates a Daniel User in the User DAO for test
        UserFactory factory = new CommonUserFactory();
        User user = factory.create("Daniel", "Password");
        userDAO.saveUser(user);
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                assertEquals("Daniel", user.getUsername());
                assertNotNull(userDAO.getUser("Daniel"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userDAO, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureWrongPasswordTest() throws IOException {
        LoginInputData inputData = new LoginInputData("Daniel", "Password");
        ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
        LoginDataAccessInterface userDAO = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);

        // Creates a Daniel User in the User DAO for test but with a different password
        UserFactory factory = new CommonUserFactory();
        User user = factory.create("Daniel", "pwd");
        userDAO.saveUser(user);
        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Incorrect password for " + user.getUsername() + ".", error);
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userDAO, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureUserDoesNotExistsTest() throws IOException {
        LoginInputData inputData = new LoginInputData("Daniel", "Password");
        ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
        LoginDataAccessInterface userDAO = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);

        // No User object in the User DAO
        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                System.out.println(userDAO.getUser("Daniel"));
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals(inputData.getUsername() + ": Account does not exist.", error);
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userDAO, failurePresenter);
        interactor.execute(inputData);
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
