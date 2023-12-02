package use_case.login;

import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class LoginInteractorIntegrationTest {
    private String PROJECTS_PATH = "./projects_test.csv";
    private String USERS_PATH = "./users_test.csv";
    private UserFactory USER_FACTORY = new CommonUserFactory();
    private ProjectFactory PROJECT_FACTORY = new CommonProjectFactory();

    private TaskFactory TASK_FACTORY = new CommonTaskFactory();
    private UsersDataAccessObject usersDAO;
    private ProjectsDataAccessObject projectsDAO;

    public void init() {
        try {
            ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                    PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
        } catch (IOException error){
            System.out.println("ERROR: IOexception when creating UsersDAO");
        }
    }
    @Test
    public void successTest() throws IOException {
        LoginInputData inputData = new LoginInputData("Daniel", "Password");
        LoginDataAccessInterface userData = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);

        // Creates a Daniel User in the User DAO for test
        UserFactory factory = new CommonUserFactory();
        User user = factory.create("Daniel", "Password");
        userData.saveUser(user);
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                assertEquals("Daniel", user.getUsername());
                assertNotNull(userData.getUser("Daniel"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userData, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureWrongPasswordTest() throws IOException {
        LoginInputData inputData = new LoginInputData("Daniel", "Password");
        LoginDataAccessInterface userData = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);

        // Creates a Daniel User in the User DAO for test but with a different password
        UserFactory factory = new CommonUserFactory();
        User user = factory.create("Daniel", "pwd");
        userData.saveUser(user);
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

        LoginInputBoundary interactor = new LoginInteractor(userData, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureUserDoesNotExistsTest() throws IOException {
        LoginInputData inputData = new LoginInputData("Daniel", "Password");
        LoginDataAccessInterface userData = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);

        // No User object in the User DAO
        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals(inputData.getUsername() + ": Account does not exist.", error);
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userData, failurePresenter);
        interactor.execute(inputData);
    }

}