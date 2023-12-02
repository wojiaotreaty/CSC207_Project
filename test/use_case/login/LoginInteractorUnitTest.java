package use_case.login;

import entity.CommonUser;
import entity.CommonUserFactory;
import entity.User;
import entity.UserFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginInteractorUnitTest {
    class DummyUsersDataAccessObject implements LoginDataAccessInterface {
        @Override
        public User getUser(String username) {
            return new CommonUser(username, "pwd");
        }

        @Override
        public boolean saveUser(User user) {
            return true;
        }
    }


    @Test
    void successTest() {
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
    void failureWrongPasswordTest() {
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
    void failureUserDoesNotExistsTest() {
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