package use_case.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginInputDataTest {

    private LoginInputData loginInputData;

    @BeforeEach
    void init() {
        loginInputData = new LoginInputData("Daniel", "1234abcd");
    }

    @Test
    void getUsername() {
        assertEquals("Daniel", loginInputData.getUsername());
    }

    @Test
    void getPassword() {
        assertEquals("1234abcd", loginInputData.getPassword());
    }
}