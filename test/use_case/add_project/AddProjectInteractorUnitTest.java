package use_case.add_project;

import static org.junit.jupiter.api.Assertions.*;

import entity.Project;
import entity.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class AddProjectInteractorUnitTest {

    @Test
    void successTest() {
        AddProjectInputData inputData = new AddProjectInputData(
                "CSC207 Group Project",
                "Create an application, which makes use of Clean Architecture and SOLID Design principles, " +
                        "which serves as an aid for time management and organizing deadlines. Upon adding a project, the" +
                        "application will split the project into smaller deadlines which help the user to stay on track.",
                "2023-12-04",
                "foobar");

        AddProjectDataAccessInterface dummyUsersDataAccessInterface = new DummyUsersDataAccessObject();

        AddProjectOutputBoundary successPresenter = new AddProjectOutputBoundary() {
            @Override
            public void prepareSuccessView(AddProjectOutputData addProjectOutputData) {
                assertEquals("100", addProjectOutputData.getProjectID());
            }
//            AddProjectOutputData addProjectOutputData = new AddProjectOutputData(
//                    projectID,
//                    addProjectInputData.getProjectTitle(),
//                    addProjectInputData.getProjectDetails(),
//                    String.valueOf(tasksString)
//            );

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(userRepository, successPresenter, new CommonUserFactory());
        interactor.execute(inputData);
    }

    @Test
    void failurePasswordMismatchTest() {
        SignupInputData inputData = new SignupInputData("Paul", "password", "wrong");
        SignupUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

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
    void failureUserExistsTest() {
        SignupInputData inputData = new SignupInputData("Paul", "password", "wrong");
        SignupUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // Add Paul to the repo so that when we check later they already exist
        UserFactory factory = new CommonUserFactory();
        User user = factory.create("Paul", "pwd",  LocalDateTime.now());
        userRepository.save(user);

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

    private class DummyUsersDataAccessObject implements AddProjectDataAccessInterface {

        @Override
        public User getUser(String username) {
            return new DummyUser();
        }

        @Override
        public boolean saveUser(User user) {
            return true;
        }

        @Override
        public String generateNewProjectId() {
            return "100";
        }
    }

    private class DummyUser {
        public void addProject(Project project) {
        }
    }
}