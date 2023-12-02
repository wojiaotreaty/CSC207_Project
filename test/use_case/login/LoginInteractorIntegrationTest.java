package use_case.login;

import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class LoginInteractorIntegrationTest {
    private UserFactory userFactory = new CommonUserFactory();
    private ProjectFactory projectFactory = new CommonProjectFactory();
    private TaskFactory taskFactory = new CommonTaskFactory();
    @Test
    public void successTest() throws IOException {
        LoginInputData inputData = new LoginInputData("Daniel", "Password");
        ProjectsDataAccessObject projectsDataAccessObject = new ProjectsDataAccessObject("testProject.csv", projectFactory, taskFactory);
        LoginDataAccessInterface userData = new UsersDataAccessObject("testUser.csv", userFactory, projectsDataAccessObject);

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
        ProjectsDataAccessObject projectsDataAccessObject = new ProjectsDataAccessObject("testProject.csv", projectFactory, taskFactory);
        LoginDataAccessInterface userData = new UsersDataAccessObject("testUser.csv", userFactory, projectsDataAccessObject);

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
        ProjectsDataAccessObject projectsDataAccessObject = new ProjectsDataAccessObject("testProject.csv", projectFactory, taskFactory);
        LoginDataAccessInterface userData = new UsersDataAccessObject("testUser.csv", userFactory, projectsDataAccessObject);

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