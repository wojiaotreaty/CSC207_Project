package use_case.refactor_project;

import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import use_case.delete_project.DeleteProjectDataAccessInterface;
import use_case.delete_project.DeleteProjectInteractorUnitTest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RefactorProjectInteractorUnitTest {

        private final UserFactory userFactory = new CommonUserFactory();
        private final ProjectFactory projectFactory = new CommonProjectFactory();
        private final TaskFactory taskFactory = new CommonTaskFactory();
        private UsersDataAccessObject usersDataAccessObject;

        static User testUser;

        static final String USERNAME = "username1";
        static final String PROJECT_ID = "32";
        static final String PROJECT_NAME = "foobarProjName";

        private final String PROJECTS_PATH = "./projects_test.csv";
        private final String USERS_PATH = "./users_test.csv";

        @Before
        public void init() {
            try {
                ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(PROJECTS_PATH, projectFactory, taskFactory);
                this.usersDataAccessObject = new UsersDataAccessObject(USERS_PATH, userFactory, projectsDAO);
            } catch (IOException e) {
                throw new RuntimeException("ERROR: IOexception when creating projectsDAO and UsersDAO");
            }

            ArrayList<Task> taskList = new ArrayList<>();

            taskList.add(this.taskFactory.create(
                    "foobarTaskName1", LocalDate.parse("2023-12-22"), "foobarTaskDesc1"));
            taskList.get(0).setStatus(true);
            taskList.add(this.taskFactory.create(
                    "foobarTaskName2", LocalDate.parse("2023-12-30"), "foobarTaskDesc2"));
            taskList.add(this.taskFactory.create(
                    "foobarTaskName3", LocalDate.parse("2024-01-01"), "foobarTaskDesc3"));
            testUser = this.userFactory.create(USERNAME, "password1", new ArrayList<>());
            usersDataAccessObject.saveUser(testUser);

            testUser.addProject(this.projectFactory.create(PROJECT_ID, PROJECT_NAME,
                    "foobarProjDesc", taskList));
            usersDataAccessObject.saveUser(testUser);
        }
        @Test
        public void successTest() {
            RefactorProjectInputData refactorProjectInputData = new RefactorProjectInputData(PROJECT_ID, USERNAME);
            RefactorProjectDataAccessInterface dummyUsersDataAccessInterface = new RefactorProjectDataAccessInterface() {
                @Override
                public User getUser(String username) {
                    if (username.equals(RefactorProjectInteractorUnitTest.USERNAME)) {
                        return RefactorProjectInteractorUnitTest.testUser;
                    } else return null;
                }

                @Override
                public boolean saveUser(User user) {
                    return user.equals(RefactorProjectInteractorUnitTest.testUser);
                }
            };
            RefactorProjectOutputBoundary successPresenter = new RefactorProjectOutputBoundary() {

                @Override

                public void prepareSuccessView(RefactorProjectOutputData refactorProjectOutputData) {
                    assertEquals(refactorProjectOutputData.getProjectId(), PROJECT_ID);
                    assertEquals(refactorProjectOutputData.getProjectName(), PROJECT_NAME);
                    User resultUser = dummyUsersDataAccessInterface.getUser(RefactorProjectInteractorUnitTest.USERNAME);
                    Project p =  resultUser.getProjects().get(0);
                    ArrayList<Task> user_tasks = p.getTasks();
                    String output_tasks = refactorProjectOutputData.getTasks();
                    String[] tasks = output_tasks.split("[|]uwu[|]");
                    String final_task = tasks[2];
                    String[] components = final_task.split("`");
                    assertEquals(components[2],user_tasks.get(2).getDeadline().toString());
                    String incomplete_task = tasks[1];
                    String[] incomplete_components = incomplete_task.split("`");
                    assertNotEquals(incomplete_components[2], user_tasks.get(1).getDeadline().toString());

                }

                @Override
                public void prepareFailView(String error) {
                    fail("Use case failure is unexpected.");
                }
            };
            RefactorProjectInputBoundary refactorProjectInteractor = new RefactorProjectInteractor(
                    usersDataAccessObject,
                    successPresenter,
                    new CommonProjectFactory(),
                    new CommonTaskFactory()
            );
            refactorProjectInteractor.execute(refactorProjectInputData);
        }
    }




