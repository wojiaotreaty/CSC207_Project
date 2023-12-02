package use_case.delete_project;

import org.junit.Test;

import entity.*;

import java.util.ArrayList;
import java.util.Arrays;

public class AddProjectInteractorUnitTest {

    @Test
    public void successTest() {
        UserFactory userFactory = new CommonUserFactory();
        ProjectFactory projectFactory = new CommonProjectFactory();

        DeleteProjectInputData inputData = new DeleteProjectInputData(
                "username1",
                "32");

        DeleteProjectDataAccessInterface dummyUsersDataAccessInterface = new DeleteProjectDataAccessInterface() {
            @Override
            public User getUser(String userId) {
                if (userId == "32") {
                    ArrayList<Project> projectList = new ArrayList<>();
                    projectList.add(projectFactory.create("32", "foobarName",
                            "foobarDesc", null));
                    return userFactory.create("username1", "password1",
                            projectList);
                } else return null;
            }

            @Override
            public boolean saveUser(String userId) {

            }
        }

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

}
