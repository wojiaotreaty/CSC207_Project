package use_case.delete_project;

import org.junit.Before;
import org.junit.Test;

import entity.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DeleteProjectInteractorUnitTest {

    private final UserFactory userFactory = new CommonUserFactory();
    private final ProjectFactory projectFactory = new CommonProjectFactory();

    static User testUser;
    static final String USERNAME = "username1";
    static final String PROJECT_ID = "32";
    static final String PROJECT_NAME = "foobarProjName";

    @Before
    public void init(){
        ArrayList<Task> taskList = new ArrayList<>();
        TaskFactory taskFactory = new CommonTaskFactory();
        taskList.add(taskFactory.create(
                "foobarTaskName1", LocalDate.parse("2022-12-22"), "foobarTaskDesc1"));
        taskList.add(taskFactory.create(
                "foobarTaskName2", LocalDate.parse("2022-12-23"), "foobarTaskDesc2"));

        ArrayList<Project> projectList = new ArrayList<>();
        projectList.add(this.projectFactory.create(PROJECT_ID, PROJECT_NAME,
                "foobarProjDesc", taskList));
        testUser = this.userFactory.create(USERNAME, "password1", projectList);
    }

    @Test
    public void successTest() {

        DeleteProjectInputData inputData = new DeleteProjectInputData(USERNAME, PROJECT_ID);

        DeleteProjectDataAccessInterface dummyUsersDataAccessInterface = new DeleteProjectDataAccessInterface() {
            @Override
            public User getUser(String username) {
                if (username.equals(DeleteProjectInteractorUnitTest.USERNAME)) {
                    return DeleteProjectInteractorUnitTest.testUser;
                } else return null;
            }

            @Override
            public boolean saveUser(User user) {
                return user.equals(DeleteProjectInteractorUnitTest.testUser);
            }
        };

        DeleteProjectOutputBoundary successPresenter = deleteProjectOutputData -> {
            assertEquals(deleteProjectOutputData.getProjectId(), DeleteProjectInteractorUnitTest.PROJECT_ID);
            assertEquals(deleteProjectOutputData.getProjectName(), DeleteProjectInteractorUnitTest.PROJECT_NAME);

            User resultUser = dummyUsersDataAccessInterface.getUser(DeleteProjectInteractorUnitTest.USERNAME);
            assertNotNull("ERROR: user not found.", resultUser);
            for (Project project : resultUser.getProjects()){
                assertNotEquals("ERROR: Project not deleted.",
                        DeleteProjectInteractorUnitTest.PROJECT_ID, project.getProjectId());
            }



        };

        DeleteProjectInputBoundary testDeleteProjectInteractor = new DeleteProjectInteractor(
                dummyUsersDataAccessInterface, successPresenter);
        testDeleteProjectInteractor.execute(inputData);
    }

}
