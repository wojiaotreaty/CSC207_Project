package use_case.signup;

import entity.CommonUserFactory;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SignupInteractorUnitTest {
    class DummyUsersDataAccessObject implements SignupDataAccessInterface {

        private ArrayList<User> users = new ArrayList<User>();
        @Override
        public User getUser(String username) {
            for (User user: users) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
            return null;
        }

        @Override
        public boolean saveUser(User user) {
            users.add(user);
            return true;
        }
    }

    @Test
    void successTest() {
        SignupInputData inputData = new SignupInputData("Daniel", "Password", "Password");
        SignupDataAccessInterface userDAO = new DummyUsersDataAccessObject();

        // This creates a successPresenter that tests whether the test case is as we expect.
        SignupOutputBoundary successPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                assertEquals("Daniel", user.getUsername());
                assertNotNull(userDAO.getUser("Daniel"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected." + error);
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(userDAO, successPresenter, new CommonUserFactory());
        interactor.execute(inputData);
    }

    @Test
    void failurePasswordMismatchTest() {
        SignupInputData inputData = new SignupInputData("Daniel", "Password", "WrongPwd");
        SignupDataAccessInterface userDAO = new DummyUsersDataAccessObject();

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

        SignupInputBoundary interactor = new SignupInteractor(userDAO, failurePresenter, new CommonUserFactory());
        interactor.execute(inputData);
    }

    @Test
    void failureUserExistsTest() {
        SignupInputData inputData = new SignupInputData("Daniel", "Password", "WrongPwd");
        SignupDataAccessInterface userDAO = new DummyUsersDataAccessObject();

        // Add Paul to the repo so that when we check later they already exist
        UserFactory factory = new CommonUserFactory();
        User user = factory.create("Daniel", "pwd");
        userDAO.saveUser(user);

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

        SignupInputBoundary interactor = new SignupInteractor(userDAO, failurePresenter, new CommonUserFactory());
        interactor.execute(inputData);
    }
}
