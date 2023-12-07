package use_case.set_status;

import static org.junit.jupiter.api.Assertions.*;

import entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetStatusInteractorUnitTest {

    @Test
    void SetTrueTest() {
        SetStatusInputData inputData = new SetStatusInputData("foo", "1", "task1`task1 desc`2024-01-01`false");

        SetStatusUsersDataAccessInterface dummyUsersDataAccessInterface = new DummyUsersDataAccessObject();

        SetStatusOutputBoundary successPresenter = new SetStatusOutputBoundary() {

            @Override
            public void prepareSuccessView(SetStatusOutputData setStatusOutputData) {
                assertEquals("1", setStatusOutputData.getProjectId());
                assertEquals("task1`task1 desc`2024-01-01`false", setStatusOutputData.getTaskString());
            }
        };

        SetStatusInputBoundary SetStatusInteractor = new SetStatusInteractor(
                dummyUsersDataAccessInterface,
                successPresenter);
        SetStatusInteractor.execute(inputData);
    }

    // Dummy DAO created for the purpose of the Interactor Unit Test.
    private static class DummyUsersDataAccessObject implements SetStatusUsersDataAccessInterface {

        @Override
        public User getUser(String username) {
            TaskFactory taskFactory = new CommonTaskFactory();
            Task task1 = taskFactory.create("task1", LocalDate.parse("2024-01-01"), "task1 desc");
            ProjectFactory projectFactory = new CommonProjectFactory();
            ArrayList<Task> tasks = new ArrayList<>();
            tasks.add(task1);
            Project project1 = projectFactory.create("1", "foo", "project1 desc", tasks);
            ArrayList<Project> projects = new ArrayList<>();
            projects.add(project1);
            return new CommonUser("foo", "baz", projects);
        }

        @Override
        public boolean saveUser(User user) {
            return true;
        }

    }

}