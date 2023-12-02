package use_case.login;

import entity.CommonUserFactory;
import entity.User;
import entity.UserFactory;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LoginInteractorUnitTest {
    class DummyUsersDataAccessObject implements LoginDataAccessInterface {
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
    public void successTest() {
        LoginInputData inputData = new LoginInputData("Daniel", "Password");
        LoginDataAccessInterface userData = new DummyUsersDataAccessObject();

        // Creates a gDaniel User in the User DAO for test
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
    public void failureWrongPasswordTest() {
        LoginInputData inputData = new LoginInputData("Daniel", "Password");
        LoginDataAccessInterface userData = new DummyUsersDataAccessObject();

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
    public void failureUserDoesNotExistsTest() {
        LoginInputData inputData = new LoginInputData("Daniel", "Password");
        LoginDataAccessInterface userData = new DummyUsersDataAccessObject();

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
