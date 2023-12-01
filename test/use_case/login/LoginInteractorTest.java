package use_case.login;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

class LoginInteractorTest {

    private LoginInteractor loginInteractor;
    private LoginInputData loginInputData;

    @BeforeEach
    void setUp() {

        loginInputData = new LoginInputData("Daniel", "1234abcd");
        loginInteractor = new LoginInteractor();
    }
    @Test
    void execute() {

    }
}