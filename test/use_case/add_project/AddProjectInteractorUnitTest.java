package use_case.add_project;

import static org.junit.jupiter.api.Assertions.*;

import entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
                assertEquals("100", addProjectOutputData.getProject().get(0));
                assertEquals("CSC207 Group Project", addProjectOutputData.getProject().get(1));
                assertEquals("Create an application, which makes use of Clean Architecture and SOLID Design principles, " +
                        "which serves as an aid for time management and organizing deadlines. Upon adding a project, the" +
                        "application will split the project into smaller deadlines which help the user to stay on track.",
                        addProjectOutputData.getProject().get(2));

                try {
                    ArrayList<ArrayList<String>> projectTasks = new ArrayList<>();

                    String[] tasks = addProjectOutputData.getProject().get(3).split("[|]uwu[|]");
                    for (String task : tasks) {
                        String[] taskComponents = task.split("`");
                        projectTasks.add((ArrayList<String>) Arrays.asList(taskComponents));
                    }
                } catch (Error e) {
                    fail("Text completion generated did not match the given restrictions.");
                }
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        AddProjectInputBoundary addProjectInteractor = new AddProjectInteractor(
                dummyUsersDataAccessInterface,
                successPresenter,
                new CommonProjectFactory(),
                new CommonTaskFactory()
        );
        addProjectInteractor.execute(inputData);
    }

    private static class DummyUsersDataAccessObject implements AddProjectDataAccessInterface {

        @Override
        public User getUser(String username) {
            return new CommonUser("foobar", "baz");
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

}