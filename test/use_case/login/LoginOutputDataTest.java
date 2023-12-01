package use_case.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LoginOutputDataTest {
    private LoginOutputData loginOutputData;
    private ArrayList<ArrayList<String>> projectdata;

    @BeforeEach
    void init() {
        projectdata = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> project: projectdata) {
            project.add("a");
            project.add("b");
            project.add("c");
        }
        loginOutputData = new LoginOutputData("Daniel", projectdata, false);
    }

    @Test
    void getUsername() {
        assertEquals("Daniel", loginOutputData.getUsername());
    }

    @Test
    void getProjectData() {
        ArrayList<ArrayList<String>> testData;
        testData = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> project: testData) {
            project.add("a");
            project.add("b");
            project.add("c");
        }
        assertEquals(testData, loginOutputData.getProjectData());
    }
}