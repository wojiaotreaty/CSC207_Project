package use_case.signup;

import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SignupInteractorIntegrationTest {
    private final String PROJECTS_PATH = "./projects_test.csv";
    private final String USERS_PATH = "./users_test.csv";
    private final UserFactory USER_FACTORY = new CommonUserFactory();
    private final ProjectFactory PROJECT_FACTORY = new CommonProjectFactory();
    private final TaskFactory TASK_FACTORY = new CommonTaskFactory();

    @Test
    void successTest() throws IOException {
        SignupInputData inputData = new SignupInputData("Daniel", "Password", "Password");
        ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
        SignupDataAccessInterface userRepository = new UsersDataAccessObject(
                USERS_PATH, USER_FACTORY, projectsDAO);

        // This creates a successPresenter that tests whether the test case is as we expect.
        SignupOutputBoundary successPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                assertEquals("Daniel", user.getUsername());
                assertNotNull(userRepository.getUser("Daniel"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(userRepository, successPresenter, new CommonUserFactory());
        interactor.execute(inputData);
    }

    @Test
    void failurePasswordMismatchTest() throws IOException {
        SignupInputData inputData = new SignupInputData("Daniel", "password", "wrong");
        ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
        SignupDataAccessInterface userRepository = new UsersDataAccessObject(
                USERS_PATH, USER_FACTORY, projectsDAO);

        // This creates a presenter that tests whether the test case is as we expect.
        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Passwords don't match.", error);
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(userRepository, failurePresenter, new CommonUserFactory());
        interactor.execute(inputData);
    }

    @Test
    void failureUserExistsTest() throws IOException{
        SignupInputData inputData = new SignupInputData("Daniel", "Password", "wrong");
        ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
        SignupDataAccessInterface userRepository = new UsersDataAccessObject(
                USERS_PATH, USER_FACTORY, projectsDAO);

        // Add Paul to the repo so that when we check later they already exist
        UserFactory factory = new CommonUserFactory();
        User user = factory.create("Daniel", "pwd");
        userRepository.saveUser(user);

        // This creates a presenter that tests whether the test case is as we expect.
        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("User already exists.", error);
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(userRepository, failurePresenter, new CommonUserFactory());
        interactor.execute(inputData);
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
