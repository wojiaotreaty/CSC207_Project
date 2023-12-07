package use_case.set_status;

import static org.junit.jupiter.api.Assertions.*;

import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


public class SetStatusIntegrationTest {
    private final String PROJECTS_PATH = "./projects_test.csv";
    private final String USERS_PATH = "./users_test.csv";
    private final CommonProjectFactory PROJECT_FACTORY = new CommonProjectFactory();
    private final CommonTaskFactory TASK_FACTORY = new CommonTaskFactory();
    private final CommonUserFactory USER_FACTORY = new CommonUserFactory();
    private UsersDataAccessObject usersDAO;
    private final String output = "These tasks are due today: \n     For Project: project1\n          Task Name: task1\n          Task Description: task1 desc\n\n" +
            "These tasks are due tomorrow: \n     For Project: project1\n          Task Name: task2\n          Task Description: task2 desc\n\n" +
            "     For Project: project2\n          Task Name: task3\n          Task Description: task3 desc\n\n" +
            "These tasks are due the day after tomorrow: \n     For Project: project2\n          Task Name: task4\n          Task Description: task4 desc\n\n";


    @BeforeEach
    public void init() {
        try {
            ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                    PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
            this.usersDAO = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);

            TaskFactory taskFactory = new CommonTaskFactory();
            Task task1 = taskFactory.create("task1", LocalDate.parse("2024-01-01"), "task1 desc");
            ProjectFactory projectFactory = new CommonProjectFactory();
            ArrayList<Task> tasks = new ArrayList<>();
            tasks.add(task1);
            Project project1 = projectFactory.create("1", "project1", "project1 desc", tasks);
            ArrayList<Project> projects = new ArrayList<>();
            projects.add(project1);

            User user = new CommonUser("foo", "baz", projects);
            usersDAO.saveUser(user);
            usersDAO.saveUser(user);


            Task task2 = taskFactory.create("task2", LocalDate.parse("2024-01-01"), "task2 desc");
            task2.setStatus(true);
            ArrayList<Task> tasks2 = new ArrayList<>();
            tasks.add(task2);
            Project project2 = projectFactory.create("2", "project2", "project2 desc", tasks2);
            ArrayList<Project> projects2 = new ArrayList<>();
            projects.add(project2);

            User user2 = new CommonUser("fee", "baz", projects2);
            usersDAO.saveUser(user2);
            usersDAO.saveUser(user2);

        } catch (IOException error){
            System.out.println("ERROR: IOexception when creating UsersDAO");
        }
    }

    @Test
    void MessageTest() {
        SetStatusInputData inputData = new SetStatusInputData("foo", "1", "task1`task1 desc`2024-01-01`false");

        SetStatusOutputBoundary successPresenter = new SetStatusOutputBoundary() {

            @Override
            public void prepareSuccessView(SetStatusOutputData setStatusOutputData) {
                assertEquals("1", setStatusOutputData.getProjectId());
                assertEquals("task1`task1 desc`2024-01-01`false", setStatusOutputData.getTaskString());
            }
        };

        SetStatusInputBoundary SetStatusInteractor = new SetStatusInteractor(
                usersDAO,
                successPresenter);
        SetStatusInteractor.execute(inputData);
    }
    @Test
    void BlankTest() {
        SetStatusInputData inputData = new SetStatusInputData("fee", "1", "task1`task1 desc`2024-01-01`true");

        SetStatusOutputBoundary successPresenter = new SetStatusOutputBoundary() {

            @Override
            public void prepareSuccessView(SetStatusOutputData setStatusOutputData) {
                assertEquals("1", setStatusOutputData.getProjectId());
                assertEquals("task1`task1 desc`2024-01-01`true", setStatusOutputData.getTaskString());
            }
        };

        SetStatusInputBoundary SetStatusInteractor = new SetStatusInteractor(
                usersDAO,
                successPresenter);
        SetStatusInteractor.execute(inputData);
    }


    @AfterEach
    public void cleanUp(){
        File testProjectsDatabase = new File(PROJECTS_PATH);
        if (!testProjectsDatabase.delete()){
            System.out.println(PROJECTS_PATH + " did not delete properly after testing.");
        }
        File testUsersDatabase = new File(USERS_PATH);
        if (!testUsersDatabase.delete()){
            System.out.println(USERS_PATH + " did not delete properly after testing.");
        }
    }
}